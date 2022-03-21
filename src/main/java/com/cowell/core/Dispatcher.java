package com.cowell.core;

public interface Dispatcher<T extends QueueElement> {
    ConsumerStore<T> getStore();

    Selector getSelector();

    void dispatch(T element);
}
