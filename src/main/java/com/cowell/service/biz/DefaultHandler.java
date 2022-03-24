package com.cowell.service.biz;

import com.cowell.core.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.Disposable;
import org.rx.exception.InvalidException;

import java.util.List;

import static org.rx.core.Extends.sleep;

@Slf4j
@RequiredArgsConstructor
public class DefaultHandler<T extends QueueElement> extends Disposable implements ConsumerHandler<T> {
    final ConsumerGroup<T> group;
    final Lock lock;
    @Setter
    int maxPeekSize = 100;

    ConsumerGroup<T> getGroup() {
        DispatchContext ctx = DispatchContext.get();
        if (ctx != null) {
            return (ConsumerGroup<T>) ctx.getDispatcher().getGroup();
        }
        return group;
    }

    @Override
    protected void freeObjects() {
    }

    @Override
    public boolean testAccept(long consumerId, T element) {
        ConsumerGroup<T> group = getGroup();
        Consumer<T> consumer = group.getById(consumerId);
        return consumer.getStatus() == ConsumerStatus.IDLE;
    }

    //不加锁
    @Override
    public boolean accept(long consumerId, T element) {
        ConsumerGroup<T> group = getGroup();
        Queue<T> queue = group.getConsumerQueue(consumerId);
        if (!queue.offer(element)) {
            return false;
        }
        element.setStatus(QueueElementStatus.ACCEPTED);
        log.debug("accept {}", element);
        return true;
    }

    @Override
    public List<T> getAcceptedList(long consumerId) {
        ConsumerGroup<T> group = getGroup();
        return group.getConsumerQueue(consumerId).peekList(maxPeekSize);
    }

    public void suspend(long consumerId) {
        ConsumerGroup<T> group = getGroup();
        lock.lock();
        try {
            group.setById(consumerId, p -> p.setSuspend(true));
        } finally {
            lock.unlock();
        }
    }

    public void resume(long consumerId) {
        ConsumerGroup<T> group = getGroup();
        lock.lock();
        try {
            group.setById(consumerId, p -> p.setSuspend(false));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T beginConsume(long consumerId, Long preferElementId) {
        ConsumerGroup<T> group = getGroup();
        if (!lock.tryLock()) {
            throw new InvalidException("Consumer locked");
        }

        Consumer<T> consumer;
        T elm;
        try {
            consumer = group.getById(consumerId);
            log.debug("IsBusy: {} ", consumer);
            if (consumer.getStatus() != ConsumerStatus.IDLE) {
                throw new InvalidException("Invalid consumer status");
            }

            Queue<T> queue = group.getConsumerQueue(consumer.getId());
            elm = queue.peek(preferElementId);
            if (elm == null) {
                return null;
            }
            consumer.setStatus(ConsumerStatus.BUSY);
            group.setById(consumer.getId(), p -> p.setStatus(consumer.getStatus()));
        } finally {
            lock.unlock();
        }

        //自定义业务
        elm.setStatus(QueueElementStatus.CONSUMED);
        log.info("beginConsume {} -> {}", consumer, elm);
        sleep(2000);
        return elm;
    }

    @Override
    public void endConsume(long consumerId) {
        ConsumerGroup<T> group = getGroup();
        Consumer<T> consumer;
        lock.lock();
        try {
            consumer = group.getById(consumerId);
            if (consumer.getStatus() != ConsumerStatus.BUSY) {
                throw new InvalidException("Invalid consumer status");
            }

            consumer.setStatus(ConsumerStatus.IDLE);
            group.setById(consumer.getId(), p -> p.setStatus(consumer.getStatus()));
        } finally {
            lock.unlock();
        }

        //自定义业务
        log.info("endConsume {}", consumer);
    }
}
