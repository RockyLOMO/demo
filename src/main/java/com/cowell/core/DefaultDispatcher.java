package com.cowell.core;

import com.cowell.config.Consts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.rx.core.Constants;
import org.rx.core.Disposable;
import org.rx.core.FluentWait;
import org.rx.core.Tasks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.rx.core.Extends.sleep;
import static org.rx.core.Extends.tryClose;

@Slf4j
@RequiredArgsConstructor
public class DefaultDispatcher<T extends QueueElement> extends Disposable implements Dispatcher<T> {
    @Getter
    final Queue<T> queue;
    @Getter
    final ConsumerStore<T> store;
    @Getter
    final Selector selector;
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
        tryClose(store);
    }

    public synchronized CompletableFuture<Void> startAsync() {
        if (future != null) {
            return future;
        }
        return future = Tasks.run(() -> {
            while (!isClosed()) {
                T elm = queue.take();
                long start = System.currentTimeMillis();
                dispatch(elm);
                log.info("dispatch elapsed: {}ms", System.currentTimeMillis() - start);
            }
        });
    }

    @Override
    public void dispatch(T element) {
        long start = System.currentTimeMillis();
        element.attr(Consts.ATTR_DISPATCH_START_NAME, start);
        element.attr(Consts.ATTR_DISPATCH_END_NAME, null);
        Consumer<T> consumer;
        while ((consumer = selector.select(store, element)) != null) {
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
                element.onDiscard(DiscardReason.SELECT_TIMEOUT);
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
}
