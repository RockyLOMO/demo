package com.cowell.core;

public interface Queue<T extends QueueElement> {
    QueueKind getKind();

    String getName();

    int getCapacity();

    int getOffset(T element);

    int size();

    boolean offer(T element);

    T take();
}
