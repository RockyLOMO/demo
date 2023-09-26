package org.rx.demo.gjmq.core;

import org.rx.demo.gjmq.service.consumer.GrabFirstSelector;
import org.rx.demo.gjmq.service.consumer.DefaultSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SelectStrategy {
    DEFAULT(DefaultSelector.INSTANCE),
    GRAB_FIRST(GrabFirstSelector.INSTANCE);

    final Selector selector;
}
