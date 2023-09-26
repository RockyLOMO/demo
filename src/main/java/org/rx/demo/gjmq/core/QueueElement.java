package org.rx.demo.gjmq.core;

import org.rx.core.Extends;

import java.util.Collections;
import java.util.List;

public interface QueueElement extends Extends {
    long getId();

    QueueElementStatus getStatus();

    void setStatus(QueueElementStatus status);

    default List<Tag> getPreferTags() {
        return Collections.emptyList();
    }
}
