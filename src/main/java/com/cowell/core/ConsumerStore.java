package com.cowell.core;

import java.util.List;
import java.util.Set;

public interface ConsumerStore<T extends QueueElement> {
    int size();

    boolean add(Consumer<T> consumer);

    boolean remove(Consumer<T> consumer);

    Consumer<T> next();

    Set<Consumer<T>> nextSet(int takeCount);

    Set<Consumer<T>> findByTags(List<Tag> tags);
}
