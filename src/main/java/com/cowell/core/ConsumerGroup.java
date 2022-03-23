package com.cowell.core;

import java.util.List;
import java.util.Set;

public interface ConsumerGroup<T extends QueueElement> {
    QueueKind getKind();

    long size();

    boolean add(Consumer<T> consumer);

    boolean remove(Consumer<T> consumer);

    Queue<T> getConsumerQueue(long id);

    Consumer<T> next();

    List<Consumer<T>> nextList(int size);

    Set<Tag> getTagCapacities();

    List<Consumer<T>> findByTags(List<Tag> tags);
}
