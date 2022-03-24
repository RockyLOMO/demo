package com.cowell.core;

import com.cowell.config.Consts;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.rx.core.*;
import org.rx.core.Delegate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static org.rx.core.Extends.*;

@Slf4j
@RequiredArgsConstructor
public class DefaultDispatcher<T extends QueueElement> extends Disposable implements Dispatcher<T>, EventTarget<DefaultDispatcher<T>> {
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DiscardEventArgs<T> extends EventArgs {
        final T element;
        final DiscardReason reason;
    }

    public final Delegate<DefaultDispatcher<T>, DiscardEventArgs<T>> onDiscard = Delegate.create();
    @Getter
    final KeepaliveManager keepaliveManager;
    @Getter
    final Queue<T> queue;
    @Getter
    final ConsumerGroup<T> group;
    @Getter
    final Selector selector;
    @Getter
    final ConsumerHandler<T> handler;

    @Setter
    long renewTtl = 30 * 1000;
    @Setter
    long maxWaitInvalidMillis = 5 * 1000;
    @Setter
    boolean putFirstOnReValid;
    @Setter
    long maxCheckValidMillis = 5 * 60 * 1000;

    @Setter
    long switchAsyncThreshold = 4 * 1000;
    @Setter
    long maxAcceptMillis = 5 * 1000;
    @Setter
    long maxDispatchMillis = 60 * 1000;
    CompletableFuture<Void> future;

    @Override
    protected void freeObjects() {
        tryClose(queue);
        tryClose(group);
        tryClose(handler);
    }

    public synchronized CompletableFuture<Void> startAsync() {
        if (future != null) {
            return future;
        }
        return future = Tasks.run(() -> {
            while (!isClosed()) {
                quietly(() -> {
                    T elm = queue.take();
                    long start = System.currentTimeMillis();
                    dispatch(elm);
                    log.info("dispatch elapsed: {}ms", System.currentTimeMillis() - start);
                });
            }
        });
    }

    @Override
    public void dispatch(T element) {
        DispatchContext.set(this);
        if (!isElementValid(element)) {
//            log.debug("element invalid: {}", element);
            try {
                FluentWait.newInstance(maxWaitInvalidMillis).until(s -> isElementValid(element));
            } catch (TimeoutException e) {
                AtomicLong sum = new AtomicLong();
                Tasks.timer().setTimeout(() -> {
                    if (!isElementValid(element)) {
                        return true;
                    }
                    queue.offer(element, putFirstOnReValid);
                    return false;
                }, d -> {
                    long nd = d >= 5000L ? 5000L : Math.max(d * 2L, 100L);
//                    log.debug("reValid check: {} | {}", nd, sum);
                    if (sum.addAndGet(nd) > maxCheckValidMillis) {
                        raiseEvent(onDiscard, new DiscardEventArgs<>(element, DiscardReason.CHECK_VALID_TIMEOUT));
                        return Constants.TIMEOUT_INFINITE;
                    }
                    return nd;
                }, null, TimeoutFlag.PERIOD);
                return;
            }
        }

        long start = System.currentTimeMillis();
        element.attr(Consts.ATTR_DISPATCH_START_NAME, start);
        element.attr(Consts.ATTR_DISPATCH_END_NAME, null);
        Consumer<T> consumer;
        while ((consumer = selector.select(group, element)) != null) {
            Long endTime = element.attr(Consts.ATTR_DISPATCH_END_NAME);
            if (endTime != null) {
                return;
            }
            long elapsed = System.currentTimeMillis() - start;
            if (!BooleanUtils.isTrue(element.attr(Consts.ATTR_ASYNC_DISPATCH)) && elapsed > switchAsyncThreshold) {
                element.attr(Consts.ATTR_ASYNC_DISPATCH, true);
                Tasks.run(() -> {
                    log.info("async dispatch {}", element);
                    dispatch(element);
                });
                return;
            }
            if (elapsed > maxDispatchMillis) {
                raiseEvent(onDiscard, new DiscardEventArgs<>(element, DiscardReason.DISPATCH_TIMEOUT));
                return;
            }

            long consumerId = consumer.getId();
            if (!handler.accept(consumerId, element)) {
                if (maxAcceptMillis == Constants.TIMEOUT_INFINITE) {
                    sleep(0);
                    continue;
                }
                try {
                    FluentWait.newInstance(maxAcceptMillis).until(s -> handler.accept(consumerId, element));
                } catch (TimeoutException e) {
                    continue;
                }
            }
            element.attr(Consts.ATTR_DISPATCH_END_NAME, System.currentTimeMillis());
            return;
        }
        raiseEvent(onDiscard, new DiscardEventArgs<>(element, DiscardReason.CONSUMER_INVALID));
    }

    @Override
    public boolean isElementValid(T t) {
        return keepaliveManager.isValid(KeepaliveManager.Region.ELEMENT, queue.computeId(t.getId()));
    }

    @Override
    public void renewElementTtl(long id) {
        keepaliveManager.receiveAck(KeepaliveManager.Region.ELEMENT, queue.computeId(id), renewTtl);
    }

    @Override
    public boolean isConsumerValid(Consumer<T> t) {
        return keepaliveManager.isValid(KeepaliveManager.Region.CONSUMER, group.computeId(t.getId()));
    }

    @Override
    public void renewConsumerTtl(long id) {
        keepaliveManager.receiveAck(KeepaliveManager.Region.CONSUMER, group.computeId(id), renewTtl);
    }
}
