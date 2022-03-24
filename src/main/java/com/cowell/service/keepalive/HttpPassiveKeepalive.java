package com.cowell.service.keepalive;

import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;

//client ajax ack -> server
//server feign -> server
@RequiredArgsConstructor
public class HttpPassiveKeepalive implements Keepalive {
    final KeepaliveManager manager;
    final KeepaliveManager.Region region;
    final long id;
    final long maxMissDuration;

    @Override
    public synchronized boolean isValid() {
        return manager.isValid(region, id);
    }

    public synchronized void passiveAck() {
        manager.receiveAck(region, id, maxMissDuration);
    }

    @Override
    public boolean sendAck() {
        return isValid();
    }
}
