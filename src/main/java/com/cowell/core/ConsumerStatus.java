package com.cowell.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rx.bean.NEnum;

@Getter
@RequiredArgsConstructor
public enum ConsumerStatus implements NEnum<ConsumerStatus> {
    IDLE(0),
    BUSY(1);

    final int value;
}
