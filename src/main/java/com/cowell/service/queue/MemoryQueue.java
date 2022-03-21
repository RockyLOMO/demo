package com.cowell.service.queue;

import com.cowell.core.*;
import com.cowell.core.Queue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rx.bean.DateTime;
import org.rx.core.Constants;
import org.rx.core.FluentWait;
import org.rx.core.Tasks;
import org.rx.core.TimeoutFlag;

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
    final Map<T, ElementContext> map = new ConcurrentHashMap<>();
    final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public QueueKind getKind() {
        return QueueKind.MEMORY;
    }

    @Override
    public int getOffset(T element) {
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
        if (map.containsKey(element)) {
            return false;
        }

        lock.writeLock().lock();
        try {
            if (queue.size() >= capacity) {
                return false;
            }

            map.computeIfAbsent(element, k -> new ElementContext()).setOfferTime(DateTime.now());
            if (putFirst) {
                queue.add(0, element);
            } else {
                queue.add(element);
            }
            Tasks.run(() -> element.onOffer(this));
            return true;
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
        T finalElm = elm;
        Tasks.run(() -> finalElm.onTake(this));
        return elm;
    }

    T poll() {
        lock.writeLock().lock();
        try {
            if (queue.isEmpty()) {
                return null;
            }
            T first = queue.remove(0);
            if (first != null) {
                map.remove(first);
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
            }
            return first;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
