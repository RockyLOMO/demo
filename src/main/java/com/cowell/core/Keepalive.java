package com.cowell.core;

public interface Keepalive {
    boolean isValid();

    boolean sendAck();
}
