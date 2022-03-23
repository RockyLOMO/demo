package com.cowell.service.keepalive;

import com.cowell.core.KAEntity;
import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveKind;
import com.cowell.core.KeepaliveManager;

public class NonKeepaliveManager implements KeepaliveManager {
    @Override
    public <T extends KAEntity> boolean isValid(Class<T> entityType, long id) {
        return true;
    }

    @Override
    public <T extends KAEntity> void receiveAck(Class<T> entityType, long id, long maxMissDuration) {
    }

    @Override
    public <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, T entity, long maxMissDuration) {
        return new HttpPassiveKeepalive(this, entity, maxMissDuration);
    }
}
