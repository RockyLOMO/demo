package com.cowell.service.biz;

import lombok.RequiredArgsConstructor;
import org.rx.core.Extends;

//client ajax ack -> server
@RequiredArgsConstructor
public class HttpPassiveKeepalive implements Keepalive, Extends {
    static Keepalive getInstance(long id) {
        return getInstance(id, 30000, 3);
    }

    static Keepalive getInstance(long id, long interval, int retry) {
        return new HttpPassiveKeepalive(id, interval * retry);
    }

    final long id;
    final long maxMissDuration;

    @Override
    public synchronized boolean isValid() {
        return KeepaliveManager.INSTANCE.isValid(id);
    }

    public synchronized void passiveAck() {
        KeepaliveManager.INSTANCE.receiveAck(id, maxMissDuration);
    }

    @Override
    public boolean sendAck() {
        return isValid();
    }
}
