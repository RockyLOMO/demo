package com.cowell.service.biz;

public interface Keepalive {
    boolean isValid();

    boolean sendAck();
}
