package org.rx.demo.gjmq.core;

public interface KeepaliveManager {
    enum Region {
        ELEMENT,
        CONSUMER
    }

    boolean isValid(Region region, long id);

    default void receiveAck(Region region, long id) {
        receiveAck(region, id, 30000);
    }

    void receiveAck(Region region, long id, long ttl);

    default <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, Region region, long id) {
        return newKeepalive(kind, region, id, 30000);
    }

    default <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, Region region, long id, long interval, int retry) {
        return newKeepalive(kind, region, id, interval * retry);
    }

    Keepalive newKeepalive(KeepaliveKind kind, Region region, long id, long maxMissDuration);
}
