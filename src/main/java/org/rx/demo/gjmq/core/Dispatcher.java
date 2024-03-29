package org.rx.demo.gjmq.core;

import java.util.concurrent.CompletableFuture;

public interface Dispatcher<T extends QueueElement> {
    KeepaliveManager getKeepaliveManager();

    Queue<T> getQueue();

    ConsumerGroup<T> getGroup();

    Selector getSelector();

    ConsumerHandler<T> getHandler();

    void dispatch(T element);

    boolean isElementValid(T t);

    void renewElementTtl(long id);

    boolean isConsumerValid(Consumer<T> t);

    void renewConsumerTtl(long id);

    CompletableFuture<Void> startAsync();
}
