package com.cowell.service.consumer;

import com.cowell.core.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.NQuery;

import static com.cowell.config.Consts.ID_TAG_NAME;
import static org.rx.core.Extends.eq;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultSelector implements Selector {
    public static final DefaultSelector INSTANCE = new DefaultSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element) {
        DispatchContext ctx = DispatchContext.current();
        Dispatcher<T> dispatcher = ctx.getDispatcher();
//        ctx.setEntityType(ConsumerEntity.class);

        Tag idTag = NQuery.of(element.getPreferTags()).firstOrDefault(p -> eq(p.getName(), ID_TAG_NAME));
        Consumer<T> first = null;
        for (Consumer<T> consumer : store.findByTags(element.getPreferTags())) {
            log.debug("tag capacity: {} -> {}", consumer, element);
            if (first == null) {
                first = consumer;
            }
            if (dispatcher.getKeepaliveManager().isValid((Class) consumer.getClass(), consumer.getId())) {
                return consumer;
            }
        }
        if (idTag != null) {
            return first;
        }
        return store.next();
    }
}
