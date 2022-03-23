package com.cowell.core;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Dispatcher<T extends QueueElement> {
    KeepaliveManager getKeepaliveManager();

    Queue<T> getQueue();

    ConsumerGroup<T> getGroup();

    Selector getSelector();

    void dispatch(T element);

    CompletableFuture<Void> startAsync();

    boolean accept(Consumer<T> consumer, T element);

    List<T> getAcceptedList(Consumer<T> consumer);

    void consume(Consumer<T> consumer, T element);
}
