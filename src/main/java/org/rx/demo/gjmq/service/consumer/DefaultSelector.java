package org.rx.demo.gjmq.service.consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.Linq;
import org.rx.demo.gjmq.config.Consts;
import org.rx.demo.gjmq.core.*;

import static org.rx.core.Extends.eq;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultSelector implements Selector {
    public static final DefaultSelector INSTANCE = new DefaultSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element) {
        DispatchContext ctx = DispatchContext.current();
        Dispatcher<T> dispatcher = ctx.getDispatcher();

        Tag idTag = Linq.from(element.getPreferTags()).firstOrDefault(p -> eq(p.getName(), Consts.ID_TAG_NAME));
        Consumer<T> first = null;
        for (Consumer<T> consumer : store.findByTags(element.getPreferTags())) {
            log.debug("tag capacity: {} -> {}", consumer, element);
            if (first == null) {
                first = consumer;
            }
            if (dispatcher.isConsumerValid(consumer)) {
                return consumer;
            }
        }
        if (idTag != null) {
            return first;
        }
        return store.next();
    }
}
