package com.cowell.core;

public interface KeepaliveManager {
    boolean isValid(long id);

    void receiveAck(long id, long maxMissDuration);

    default Keepalive newKeepalive(KeepaliveKind kind, long id) {
        return newKeepalive(kind, id, 30000);
    }

    default Keepalive newKeepalive(KeepaliveKind kind, long id, long interval, int retry) {
        return newKeepalive(kind, id, interval * retry);
    }

    Keepalive newKeepalive(KeepaliveKind kind, long id, long maxMissDuration);
}
