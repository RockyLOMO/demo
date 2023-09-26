package org.rx.demo.gjmq.core;

public interface Selector {
    <T extends QueueElement> Consumer<T> select(ConsumerGroup<T> store, T element);
}
