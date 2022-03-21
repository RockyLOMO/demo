package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStore;
import com.cowell.core.QueueElement;
import com.cowell.core.Tag;
import org.rx.bean.RandomList;
import org.rx.core.NQuery;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MemoryStore<T extends QueueElement> implements ConsumerStore<T> {
    final RandomList<Consumer<T>> list = new RandomList<>();

    @Override
    public int size() {
        return NQuery.of(list).count(Consumer::isAcceptable);
    }

    @Override
    public boolean add(Consumer<T> consumer) {
        return list.add(consumer);
    }

    @Override
    public boolean remove(Consumer<T> consumer) {
        return list.remove(consumer);
    }

    @Override
    public Consumer<T> next() {
        return NQuery.of(list).where(Consumer::isAcceptable)
                .orderBy(p -> ThreadLocalRandom.current().nextInt(0, 100))
                .firstOrDefault(() -> NQuery.of(list).where(Consumer::isValid).first());
    }

    @Override
    public Set<Consumer<T>> nextSet(int takeCount) {
        return NQuery.of(list).where(Consumer::isAcceptable).take(takeCount)
                .orderBy(p -> ThreadLocalRandom.current().nextInt(0, takeCount)).toSet();
    }

    @Override
    public Set<Consumer<T>> findByTags(List<Tag> tags) {
        return NQuery.of(list).where(p -> p.isValid() && p.matchTags(tags) > 0).toSet();
    }
}
