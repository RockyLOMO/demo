package com.cowell.service.consumer;

import com.cowell.core.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.NQuery;

import static com.cowell.config.Constants.ID_TAG_NAME;
import static org.rx.core.Extends.eq;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultSelector implements Selector {
    public static final DefaultSelector INSTANCE = new DefaultSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerStore<T> store, T element) {
        Tag idTag = NQuery.of(element.getTags()).firstOrDefault(p -> eq(p.getName(), ID_TAG_NAME));
        Consumer<T> first = null;
        for (Consumer<T> consumer : store.findByTags(element.getTags())) {
            log.debug("tag capacity: {} -> {}", consumer, element);
            if (first == null) {
                first = consumer;
            }
            if (consumer.isValid()) {
                return consumer;
            }
        }
        if (idTag != null) {
            return first;
        }
        return store.next();
    }
}
