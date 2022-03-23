package com.cowell.core;

import com.cowell.config.Consts;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.rx.core.*;
import org.rx.core.Delegate;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static org.rx.core.Extends.*;

@Slf4j
@RequiredArgsConstructor
public class DefaultDispatcher<T extends QueueElement> extends Disposable implements Dispatcher<T>, EventTarget<DefaultDispatcher<T>> {
    @Data
    @EqualsAndHashCode(callSuper = true)
    static class DiscardEventArgs<T> extends EventArgs {
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

    @Setter
    long maxWaitInvalidMillis = 10 * 1000;
    @Setter
    boolean putFirstOnReValid;
    @Setter
    long maxCheckValidMillis = 5 * 60 * 1000;

    @Setter
    long maxWaitAcceptMillis = 5 * 1000;
    @Setter
    long switchAsyncThreshold = 10 * 1000;
    @Setter
    long maxSelectMillis = 60 * 1000;
    CompletableFuture<Void> future;

    @Override
    protected void freeObjects() {
        tryClose(queue);
        tryClose(group);
    }

    public synchronized CompletableFuture<Void> startAsync() {
        if (future != null) {
            return future;
        }
        return future = Tasks.run(() -> {
            while (!isClosed()) {
                quietly(() -> {
                    T elm = queue.take();
                    DispatchContext.set(this);
                    long start = System.currentTimeMillis();
                    dispatch(elm);
                    log.info("dispatch elapsed: {}ms", System.currentTimeMillis() - start);
                });
            }
        });
    }

    @Override
    public void dispatch(T element) {
        if (!keepaliveManager.isValid(element.getId())) {
            try {
                FluentWait.newInstance(maxWaitInvalidMillis).until(s -> keepaliveManager.isValid(element.getId()));
            } catch (TimeoutException e) {
                AtomicLong sum = new AtomicLong();
                Tasks.timer().setTimeout(() -> {
                    if (!keepaliveManager.isValid(element.getId())) {
                        return true;
                    }
                    queue.offer(element, putFirstOnReValid);
                    return false;
                }, d -> {
                    long nd = d >= 5000L ? 5000L : Math.max(d * 2L, 100L);
                    log.debug("reValid check: {} | {}", nd, sum);
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
            long selectElapsed = System.currentTimeMillis() - start;
            if (!BooleanUtils.isTrue(element.attr(Consts.ATTR_ASYNC_DISPATCH)) && selectElapsed > switchAsyncThreshold) {
                element.attr(Consts.ATTR_ASYNC_DISPATCH, true);
                Tasks.run(() -> {
                    log.info("async dispatch {}", element);
                    dispatch(element);
                });
                return;
            }
            if (selectElapsed > maxSelectMillis) {
                raiseEvent(onDiscard, new DiscardEventArgs<>(element, DiscardReason.SELECT_TIMEOUT));
                return;
            }
            Consumer<T> finalConsumer = consumer;
            if (!consumer.accept(element)) {
                if (maxWaitAcceptMillis == Constants.TIMEOUT_INFINITE) {
                    sleep(0);
                    continue;
                }
                try {
                    FluentWait.newInstance(maxWaitAcceptMillis).until(s -> finalConsumer.accept(element));
                } catch (TimeoutException e) {
                    continue;
                }
            }
            element.attr(Consts.ATTR_DISPATCH_END_NAME, System.currentTimeMillis());
            Tasks.run(() -> finalConsumer.consume(element));
            return;
        }
        element.onDiscard(DiscardReason.CONSUMER_INVALID);
    }

    @Override
    public boolean accept(Consumer<T> consumer, T element) {
        return false;
    }

    @Override
    public List<T> getAcceptedList(Consumer<T> consumer) {
        return null;
    }

    @Override
    public void consume(Consumer<T> consumer, T element) {

    }
}
