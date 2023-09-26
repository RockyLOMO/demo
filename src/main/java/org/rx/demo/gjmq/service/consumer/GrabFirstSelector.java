package org.rx.demo.gjmq.service.consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rx.core.Linq;
import org.rx.demo.gjmq.core.*;

import static org.rx.core.Extends.ifNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrabFirstSelector implements Selector {
    public static GrabFirstSelector INSTANCE = new GrabFirstSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element) {
        Dispatcher<T> dispatcher = DispatchContext.current().getDispatcher();

        return ifNull(Linq.from(store.nextList(20), true).firstOrDefault(p -> dispatcher.getHandler().testAccept(p.getId(), element)), store.next());
    }
}
