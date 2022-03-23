package com.cowell.core;

import java.util.concurrent.CompletableFuture;

public interface Dispatcher<T extends QueueElement> {
    KeepaliveManager getKeepaliveManager();

    Queue<T> getQueue();

    ConsumerGroup<T> getGroup();

    Selector getSelector();

    ConsumerHandler<T> getHandler();

    void dispatch(T element);

    CompletableFuture<Void> startAsync();
}
