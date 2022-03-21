package com.cowell.core;

import com.cowell.service.consumer.GrabFirstSelector;
import com.cowell.service.consumer.DefaultSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SelectStrategy {
    TAG_FIRST(DefaultSelector.INSTANCE),
    GRAB_FIRST(GrabFirstSelector.INSTANCE);

    final Selector selector;
}
