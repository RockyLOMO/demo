package com.cowell.service.biz;

import org.rx.core.Extends;

public class NonKeepalive implements Keepalive, Extends {
    boolean isValid;

    @Override
    public synchronized boolean isValid() {
        return isValid;
    }

    @Override
    public synchronized boolean sendAck() {
        return isValid = true;
    }
}
