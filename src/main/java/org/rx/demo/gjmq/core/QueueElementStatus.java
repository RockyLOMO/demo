package org.rx.demo.gjmq.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rx.bean.NEnum;

@Getter
@RequiredArgsConstructor
public enum QueueElementStatus implements NEnum<QueueElementStatus> {
    TRANSIENT(0),
    QUEUED(1),
    TAKEN(2),
    ACCEPTED(3),
    CONSUMED(4),
    DETACHED(5);

    final int value;
}
