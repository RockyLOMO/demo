package com.cowell.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public interface QueueElement extends Serializable {
    long getId();

    boolean isValid();

    default List<Tag> getTags() {
        return Collections.emptyList();
    }

    default <T extends QueueElement> void onAccept(Consumer<T> consumer) {
    }

    default <T extends QueueElement> void onConsume(Consumer<T> consumer) {
    }

    default void onDiscard(DiscardReason reason) {
    }
}
