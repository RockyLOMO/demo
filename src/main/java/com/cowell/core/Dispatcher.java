package com.cowell.core;

import java.util.concurrent.CompletableFuture;

public interface Dispatcher<T extends QueueElement> {
    Queue<T> getQueue();

    ConsumerStore<T> getStore();

    Selector getSelector();

    void dispatch(T element);

    CompletableFuture<Void> startAsync();
}
