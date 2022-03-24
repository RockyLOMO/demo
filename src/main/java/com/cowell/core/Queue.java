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

    default long computeId(long elementId) {
        return elementId;
    }

    String getName();

    void setName(String name);

    long getCapacity();

    void setCapacity(long capacity);

    long size();

    default boolean offer(T element) {
        return offer(element, false);
    }

    boolean offer(T element, boolean putFirst);

    default void setPreferId(long elementId) {
    }

    T take();

    default T poll() {
        return poll(null);
    }

    T poll(Long elementId);

    default T peek() {
        return peek(null);
    }

    T peek(Long elementId);

    List<T> peekList(int size);

    int getOrdinal(T element);
}
