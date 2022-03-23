package com.cowell.core;

public interface Selector {
    <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element);
}
