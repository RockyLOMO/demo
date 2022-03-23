package com.cowell.core;

public interface KeepaliveManager {
    <T extends KAEntity> boolean isValid(Class<T> entityType, long id);

    <T extends KAEntity> void receiveAck(Class<T> entityType, long id, long maxMissDuration);

    default <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, T entity) {
        return newKeepalive(kind, entity, 30000);
    }

    default <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, T entity, long interval, int retry) {
        return newKeepalive(kind, entity, interval * retry);
    }

    <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, T entity, long maxMissDuration);
}
