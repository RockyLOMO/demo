package com.cowell.service.consumer;

import com.cowell.core.*;
import org.rx.io.ShardingEntityDatabase;

import java.util.List;
import java.util.Set;

public class ShardingH2Store<T extends QueueElement> implements ConsumerGroup<T> {
    public static final ShardingEntityDatabase EDB = null;

    @Override
    public QueueKind getKind() {
        return null;
    }

    @Override
    public long size() {
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
    public void setConsumerQueueCapacity(long capacity) {
    }

    @Override
    public Queue<T> getConsumerQueue(long consumerId) {
        return null;
    }

    @Override
    public Consumer<T> next() {
        return null;
    }

    @Override
    public List<Consumer<T>> nextList(int takeCount) {
        return null;
    }

    @Override
    public Set<Tag> getTagCapacities() {
        return null;
    }

    @Override
    public List<Consumer<T>> findByTags(List<Tag> tags) {
        return null;
    }
}
