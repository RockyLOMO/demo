package com.cowell.core;

import java.util.List;

public interface Queue<T extends QueueElement> {
    QueueKind getKind();

    String getName();

    void setName(String name);

    long getCapacity();

    long size();

    default boolean offer(T element) {
        return offer(element, false);
    }

    boolean offer(T element, boolean putFirst);

    T take();

    List<T> peek(int size);

    T pollById(long id);

    int getOrdinal(T element);
}
