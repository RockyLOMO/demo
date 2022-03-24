package com.cowell.core;

import org.rx.util.function.BiAction;

import java.util.List;
import java.util.Set;

public interface ConsumerGroup<T extends QueueElement> {
    QueueKind getKind();

    default long computeId(long consumerId) {
        return consumerId;
    }

    long size();

    boolean add(Consumer<T> consumer);

    boolean remove(Consumer<T> consumer);

    Consumer<T> getById(long consumerId);

    void setById(long consumerId, BiAction<Consumer<T>> fn);

    void setConsumerQueueCapacity(long capacity);

    Queue<T> getConsumerQueue(long consumerId);

    Consumer<T> next();

    List<Consumer<T>> nextList(int size);

    Set<Tag> getTagCapacities();

    List<Consumer<T>> findByTags(List<Tag> tags);
}
