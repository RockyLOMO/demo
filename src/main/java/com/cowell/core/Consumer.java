package com.cowell.core;

import java.io.Serializable;
import java.util.List;

public interface Consumer<T extends QueueElement> extends Serializable {
    boolean isValid();

    default int matchTags(List<Tag> tags) {
        return 0;
    }

    boolean isAcceptable();

    boolean accept(T element);

    void consume(T element);
}
