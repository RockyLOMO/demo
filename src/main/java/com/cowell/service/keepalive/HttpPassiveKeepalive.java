package com.cowell.service.keepalive;

import com.cowell.core.KAEntity;
import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;

//client ajax ack -> server
//server feign -> server
@RequiredArgsConstructor
public class HttpPassiveKeepalive implements Keepalive {
    final KeepaliveManager manager;
    final KAEntity entity;
    final long maxMissDuration;

    @Override
    public synchronized boolean isValid() {
        return manager.isValid(entity.getClass(), entity.getId());
    }

    public synchronized void passiveAck() {
        manager.receiveAck(entity.getClass(), entity.getId(), maxMissDuration);
    }

    @Override
    public boolean sendAck() {
        return isValid();
    }
}
