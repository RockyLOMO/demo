package com.cowell.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
    long maxWaitInvalidMillis = 10 * 1000;
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
//                log.info("dispatch elapsed: {}ms", System.currentTimeMillis() - start);
            }
        });
    }

    @Override
    public void dispatch(T element) {
        Consumer<T> consumer;
        while ((consumer = selector.select(store, element)) != null) {
            Consumer<T> finalConsumer = consumer;
            if (!consumer.accept(element)) {
                if (maxWaitInvalidMillis == Constants.TIMEOUT_INFINITE) {
                    sleep(0);
                    continue;
                }
                try {
                    FluentWait.newInstance(maxWaitInvalidMillis).until(s -> finalConsumer.accept(element));
                } catch (TimeoutException e) {
                    continue;
                }
            }
            Tasks.run(() -> {
                finalConsumer.consume(element);
                element.onConsume(finalConsumer);
            });
            return;
        }
        element.onDiscard(DiscardReason.CONSUMER_INVALID);
    }
}
