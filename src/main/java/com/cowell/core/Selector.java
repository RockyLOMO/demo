package com.cowell.core;

public interface Selector {
    <T extends QueueElement> Consumer<T> select(ConsumerStore<T> store, T element);
}
