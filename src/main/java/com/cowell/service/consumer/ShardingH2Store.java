package com.cowell.service.consumer;

import com.cowell.core.*;
import org.rx.io.ShardingEntityDatabase;

import java.util.List;
import java.util.Set;

public class ShardingH2Store<T extends QueueElement> implements ConsumerStore<T> {
    public static final ShardingEntityDatabase EDB = null;

    @Override
    public QueueKind getKind() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean add(Consumer<T> consumer) {
        return false;
    }

    @Override
    public boolean remove(Consumer<T> consumer) {
        return false;
    }

    @Override
    public Consumer<T> next() {
        return null;
    }

    @Override
    public Set<Consumer<T>> nextSet(int takeCount) {
        return null;
    }

    @Override
    public Set<Tag> getTagCapacities() {
        return null;
    }

    @Override
    public Set<Consumer<T>> findByTags(List<Tag> tags) {
        return null;
    }

    @Override
    public Queue<T> getConsumerQueue(long id) {
        return null;
    }
}
