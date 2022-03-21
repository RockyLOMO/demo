package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStore;
import com.cowell.core.Selector;
import com.cowell.core.QueueElement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rx.core.NQuery;

import java.util.Set;

import static org.rx.core.Extends.ifNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrabFirstSelector implements Selector {
    public static GrabFirstSelector INSTANCE = new GrabFirstSelector();

    @Override
    public <T extends QueueElement> Consumer<T> select(ConsumerStore<T> store, T element) {
        Set<Consumer<T>> list = store.nextSet(100);
        return ifNull(NQuery.of(list, true).firstOrDefault(p -> p.accept(element)), store.next());
    }
}
