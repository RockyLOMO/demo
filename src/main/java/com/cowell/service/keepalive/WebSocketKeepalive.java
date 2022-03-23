package com.cowell.service.keepalive;

import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;

//todo
//client ws -> server
//server ack -> client
@RequiredArgsConstructor
public class WebSocketKeepalive implements Keepalive {
    final KeepaliveManager manager;
    final long id;
    final long maxMissDuration;

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean sendAck() {
        return false;
    }
}
