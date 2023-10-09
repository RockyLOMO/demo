package org.rx.demo.gjprescription.service.flow;

public interface ActionInterceptor {
    <T> T doAround(ActionJoinPoint joinPoint) throws Throwable;
}
