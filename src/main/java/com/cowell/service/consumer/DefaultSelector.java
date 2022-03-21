package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStore;
import com.cowell.core.Selector;
import com.cowell.core.QueueElement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultSelector implements Selector {
    public static final DefaultSelector INSTANCE = new DefaultSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerStore<T> store, T element) {
        for (Consumer<T> consumer : store.findByTags(element.preferTags())) {
            log.debug("tag consume: {} -> {}", consumer, element);
            if (
//                    consumer.isAcceptable() &&  //不必须等待此医生则放开
                    consumer.accept(element)) {
                return consumer;
            }
        }
        return store.next();
    }
}
