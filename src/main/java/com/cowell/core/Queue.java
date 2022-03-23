package com.cowell.core;

import org.rx.core.Arrays;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface Queue<T extends QueueElement> {
    Set<QueueElementStatus> CAN_OFFER_STATUS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.toList(QueueElementStatus.TRANSIENT, QueueElementStatus.TAKEN, QueueElementStatus.DETACHED)));

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

    T pollById(long elementId);

    int getOrdinal(T element);
}
