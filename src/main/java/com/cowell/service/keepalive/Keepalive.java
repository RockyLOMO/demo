package com.cowell.service.keepalive;

public interface Keepalive {
    boolean isValid();

    boolean sendAck();
}
