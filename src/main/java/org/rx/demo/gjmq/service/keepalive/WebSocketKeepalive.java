package org.rx.demo.gjmq.service.keepalive;

import org.rx.demo.gjmq.core.Keepalive;
import org.rx.demo.gjmq.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;

//todo
//client ws -> server
//server ack -> client
@RequiredArgsConstructor
public class WebSocketKeepalive implements Keepalive {
    final KeepaliveManager manager;
    final KeepaliveManager.Region region;
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
