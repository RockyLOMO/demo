package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStore;
import com.cowell.core.Selector;
import com.cowell.core.QueueElement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultSelector implements Selector {
    public static final DefaultSelector INSTANCE = new DefaultSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerStore<T> store, T element) {
        for (Consumer<T> consumer : store.findByTags(element.preferTags())) {
            if (consumer.accept(element)) {
                return consumer;
            }
        }
        return store.next();
    }
}
