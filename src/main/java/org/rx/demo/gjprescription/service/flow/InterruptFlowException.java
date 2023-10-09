package org.rx.demo.gjprescription.service.flow;

import org.rx.exception.ExceptionLevel;
import org.rx.exception.InvalidException;

/**
 * 中断流程特定异常
 */
public class InterruptFlowException extends InvalidException {
    protected InterruptFlowException(Throwable e) {
        super(e);
    }

    public InterruptFlowException(String messagePattern, Object... args) {
        super(messagePattern, args);
    }

    public InterruptFlowException(ExceptionLevel level, String messagePattern, Object... args) {
        super(level, messagePattern, args);
    }
}
