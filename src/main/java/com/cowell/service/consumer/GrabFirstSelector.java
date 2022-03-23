package com.cowell.service.consumer;

import com.cowell.core.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rx.core.NQuery;

import static org.rx.core.Extends.ifNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrabFirstSelector implements Selector {
    public static GrabFirstSelector INSTANCE = new GrabFirstSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element) {
        Dispatcher<T> dispatcher = DispatchContext.current().getDispatcher();

        return ifNull(NQuery.of(store.nextList(20), true).firstOrDefault(p -> dispatcher.accept(p, element)), store.next());
    }
}
