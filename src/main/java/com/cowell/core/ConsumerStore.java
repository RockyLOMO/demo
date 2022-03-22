package com.cowell.core;

import java.util.List;
import java.util.Set;

public interface ConsumerStore<T extends QueueElement> {
    QueueKind getKind();

    int size();

    boolean add(Consumer<T> consumer);

    boolean remove(Consumer<T> consumer);

    Consumer<T> next();

    Set<Consumer<T>> nextSet(int takeCount);

    Set<Tag> getTagCapacities();

    Set<Consumer<T>> findByTags(List<Tag> tags);

    Queue<T> getConsumerQueue(long id);
}
