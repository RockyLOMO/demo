package com.cowell.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.Constants;
import org.rx.core.FluentWait;
import org.rx.core.Tasks;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.rx.core.Extends.sleep;

@Slf4j
@Getter
@RequiredArgsConstructor
public class DefaultDispatcher<T extends QueueElement> implements Dispatcher<T>, Consumer<T> {
    final ConsumerStore<T> store;
    final Selector selector;
    @Setter
    long maxWaitInvalidMillis = 10 * 1000;

    @Override
    public void dispatch(T element) {
        Consumer<T> consumer;
        while ((consumer = selector.select(store, element)) != null) {
            Consumer<T> finalConsumer = consumer;
            if (!consumer.isAcceptable()) {
                if (maxWaitInvalidMillis == Constants.TIMEOUT_INFINITE) {
                    sleep(0);
                    continue;
                }
                try {
                    FluentWait.newInstance(maxWaitInvalidMillis).until(s -> finalConsumer.isAcceptable());
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

    @Override
    public boolean isValid() {
        return isAcceptable();
    }

    @Override
    public int matchTags(List<Tag> tags) {
        return store.findByTags(tags).size();
    }

    @Override
    public boolean isAcceptable() {
        return store.size() > 0;
    }

    @Override
    public boolean accept(T element) {
        return selector.select(store, element) != null;
    }

    @Override
    public void consume(T element) {
        long start = System.currentTimeMillis();
        dispatch(element);
        log.info("dispatcher elapsed: {}ms", System.currentTimeMillis() - start);
    }
}
