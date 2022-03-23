package com.cowell.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public interface Consumer<T extends QueueElement> extends Serializable {
    long getId();

    boolean isSuspend();

    void setSuspend(boolean suspend);

    ConsumerStatus getStatus();

    void setStatus(ConsumerStatus status);

    default List<Tag> getTags() {
        return Collections.emptyList();
    }
}
