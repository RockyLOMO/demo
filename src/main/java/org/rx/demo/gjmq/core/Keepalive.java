package org.rx.demo.gjmq.core;

public interface Keepalive {
    boolean isValid();

    boolean sendAck();
}
