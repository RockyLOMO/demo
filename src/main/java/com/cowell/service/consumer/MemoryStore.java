package com.cowell.service.consumer;

import com.cowell.core.*;
import com.cowell.core.Queue;
import org.rx.bean.RandomList;
import org.rx.core.NQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MemoryStore<T extends QueueElement> implements ConsumerStore<T> {
    final RandomList<Consumer<T>> list = new RandomList<>();
    //    final List<Consumer<T>> list = new ArrayList<>();
    final Map<Long, Queue<T>> subQueues = new ConcurrentHashMap<>();

    @Override
    public QueueKind getKind() {
        return QueueKind.MEMORY;
    }

    @Override
    public int size() {
        return NQuery.of(list).count(Consumer::isValid);
    }

    @Override
    public boolean add(Consumer<T> consumer) {
        return list.add(consumer);
    }

    @Override
    public boolean remove(Consumer<T> consumer) {
        subQueues.remove(consumer.getId());
        return list.remove(consumer);
    }

    @Override
    public Consumer<T> next() {
        return NQuery.of(list).where(Consumer::isValid)
                .orderBy(p -> ThreadLocalRandom.current().nextInt(0, 100))
                .firstOrDefault(() -> NQuery.of(list).where(Consumer::isValid).first());
    }

    @Override
    public Set<Consumer<T>> nextSet(int takeCount) {
        return NQuery.of(list).where(Consumer::isValid).take(takeCount)
                .orderBy(p -> ThreadLocalRandom.current().nextInt(0, takeCount)).toSet();
    }

    @Override
    public Set<Tag> getTagCapacities() {
        return NQuery.of(list).selectMany(Consumer::getTags).toSet();
    }

    @Override
    public Set<Consumer<T>> findByTags(List<Tag> tags) {
        return NQuery.of(list).where(p ->
//                        p.isValid() &&
                        NQuery.of(p.getTags()).intersection(tags).count() > 0)
                .orderByDescending(p -> NQuery.of(p.getTags()).intersection(tags).count()).toSet();
    }

    @Override
    public Queue<T> getConsumerQueue(long id) {
        return subQueues.computeIfAbsent(id, k -> getKind().newQueue(Integer.MAX_VALUE));
    }
}
