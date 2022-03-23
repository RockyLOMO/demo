package com.cowell.service.biz;

import com.cowell.core.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.Disposable;
import org.rx.exception.InvalidException;

import java.util.List;

import static org.rx.core.Extends.sleep;

//todo keepalive
@Slf4j
@RequiredArgsConstructor
public class DefaultHandler<T extends QueueElement> extends Disposable implements ConsumerHandler<T> {
    final Lock lock;
    @Setter
    int maxPeekSize = 100;
    ConsumerGroup<T> group;

    ConsumerGroup<T> getGroup() {
        if (group == null) {
            throw new InvalidException("Group not found");
        }
        return group;
    }

    @Override
    protected void freeObjects() {
    }

    @Override
    public boolean testAccept(long consumerId, T element) {
        Dispatcher<T> dispatcher = DispatchContext.current().getDispatcher();
        Consumer<T> consumer = dispatcher.getGroup().getById(consumerId);
        return consumer.getStatus() == ConsumerStatus.IDLE;
    }

    //不加锁
    @Override
    public boolean accept(long consumerId, T element) {
        Dispatcher<T> dispatcher = DispatchContext.current().getDispatcher();
        Queue<T> queue = (group = dispatcher.getGroup()).getConsumerQueue(consumerId);
        if (!queue.offer(element)) {
            return false;
        }
        element.setStatus(QueueElementStatus.ACCEPTED);
        return true;
    }

    @Override
    public List<T> getAcceptedList(long consumerId) {
        return getGroup().getConsumerQueue(consumerId).peek(maxPeekSize);
    }

//    public void setPreferId(Consumer<T> consumer, long elementId) {
//        Dispatcher<T> dispatcher = DispatchContext.current().getDispatcher();
//        dispatcher.getGroup().getConsumerQueue(consumer.getId()).setPreferId(elementId);
//    }

    public void suspend(long consumerId) {
        lock.lock();
        try {
            getGroup().setById(consumerId, p -> p.setSuspend(true));
        } finally {
            lock.unlock();
        }
    }

    public void resume(long consumerId) {
        lock.lock();
        try {
            getGroup().setById(consumerId, p -> p.setSuspend(false));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void beginConsume(long consumerId, Long preferElementId) {
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
            elm = preferElementId != null ? queue.pollById(preferElementId) : queue.poll();
            if (elm == null) {
                return;
            }
            consumer.setStatus(ConsumerStatus.BUSY);
            group.setById(consumer.getId(), p -> p.setStatus(consumer.getStatus()));
        } finally {
            lock.unlock();
        }

        //自定义业务
        elm.setStatus(QueueElementStatus.DETACHED);
        log.info("consume {} -> {}", consumer, elm);
        sleep(2000);
    }

    @Override
    public void endConsume(long consumerId) {
        lock.lock();
        try {
            Consumer<T> consumer = getGroup().getById(consumerId);
            if (consumer.getStatus() != ConsumerStatus.BUSY) {
                throw new InvalidException("Invalid consumer status");
            }
            consumer.setStatus(ConsumerStatus.IDLE);
            group.setById(consumer.getId(), p -> p.setStatus(consumer.getStatus()));
        } finally {
            lock.unlock();
        }

        //自定义业务

    }
}
