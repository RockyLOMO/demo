package com.cowell.core;

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
    DETACHED(4);

    final int value;
}
