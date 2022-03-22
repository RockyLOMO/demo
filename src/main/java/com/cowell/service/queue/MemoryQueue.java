package com.cowell.service.queue;

import com.cowell.core.*;
import com.cowell.core.Queue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.rx.core.Extends.eq;

@Slf4j
@RequiredArgsConstructor
public class MemoryQueue<T extends QueueElement> extends com.cowell.core.AbstractQueue<T> implements Queue<T> {
    @Getter
    final int capacity;
    final List<T> queue = new ArrayList<>();
    final Set<T> map = ConcurrentHashMap.newKeySet();
    final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public QueueKind getKind() {
        return QueueKind.MEMORY;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return queue.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean offer(T element) {
        return offer(element, false);
    }

    public boolean offer(T element, boolean putFirst) {
        if (map.contains(element)) {
            return false;
        }

        lock.writeLock().lock();
        try {
            if (queue.size() >= capacity) {
                return false;
            }

            map.add(element);
            if (putFirst) {
                queue.add(0, element);
            } else {
                queue.add(element);
            }
            return super.offer(element);
        } finally {
            lock.writeLock().unlock();
            synchronized (queue) {
                queue.notifyAll();
            }
        }
    }

    @SneakyThrows
    @Override
    public T take() {
        T elm;
        while ((elm = poll()) == null) {
            synchronized (queue) {
                queue.wait();
            }
        }
        return elm;
    }

    T poll() {
        T first;
        lock.writeLock().lock();
        try {
            if (queue.isEmpty()) {
                return null;
            }
            first = queue.remove(0);
            if (first != null) {
                map.remove(first);
            }
        } finally {
            lock.writeLock().unlock();
        }

        if (first == null) {
            return null;
        }
        if (!first.isValid()) {
            try {
                FluentWait.newInstance(maxWaitInvalidMillis).until(s -> first.isValid());
            } catch (TimeoutException e) {
                AtomicLong sum = new AtomicLong();
                Tasks.timer().setTimeout(() -> {
                    if (!first.isValid()) {
                        return true;
                    }
                    offer(first, putFirstOnReValid);
                    return false;
                }, d -> {
                    long nd = d >= 5000L ? 5000L : Math.max(d * 2L, 100L);
                    log.debug("reValid check: {} | {}", nd, sum);
                    if (sum.addAndGet(nd) > maxCheckValidMillis) {
                        first.onDiscard(DiscardReason.CHECK_VALID_TIMEOUT);
                        return Constants.TIMEOUT_INFINITE;
                    }
                    return nd;
                }, first, TimeoutFlag.PERIOD);
                return poll();
            }
        }
        return first;
    }

    @Override
    public List<T> peek(int size) {
        lock.readLock().lock();
        try {
            return queue.subList(0, Math.min(queue.size(), size));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public T pollById(long id) {
        lock.writeLock().lock();
        try {
            return NQuery.of(queue).where((p, i) -> {
                if (p.getId() == id) {
                    queue.remove(i);
                    return true;
                }
                return false;
            }).firstOrDefault();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getOrdinal(T element) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < queue.size(); i++) {
                if (eq(queue.get(i), element)) {
                    return i;
                }
            }
            return -1;
        } finally {
            lock.readLock().unlock();
        }
    }
}
