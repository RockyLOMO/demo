package com.cowell.core;

import org.rx.core.Extends;

import java.util.Collections;
import java.util.List;

public interface QueueElement extends Extends {
    long getId();

    boolean isValid();

    default List<Tag> getTags() {
        return Collections.emptyList();
    }

    default void onDiscard(DiscardReason reason) {
    }
}
