package com.cowell.service.keepalive;

import com.cowell.core.KAEntity;
import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;

//todo
@RequiredArgsConstructor
public class TcpKeepalive implements Keepalive {
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
