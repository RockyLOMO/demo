package com.cowell.core;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class AbstractQueue<T extends QueueElement> implements Queue<T> {
    @Setter
    String name;
    @Setter
    protected long maxWaitInvalidMillis = 10 * 1000;
    @Setter
    protected boolean putFirstOnReValid;
    @Setter
    protected long maxCheckValidMillis = 5 * 60 * 1000;
}
