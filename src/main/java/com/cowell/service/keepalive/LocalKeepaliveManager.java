package com.cowell.service.keepalive;

import com.cowell.core.KAEntity;
import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveKind;
import com.cowell.core.KeepaliveManager;
import com.cowell.service.consumer.ConsumerEntity;
import com.cowell.service.queue.ElementEntity;
import org.rx.io.EntityDatabase;

import java.sql.Connection;

public class LocalKeepaliveManager implements KeepaliveManager {
    final EntityDatabase db = EntityDatabase.DEFAULT;

    <T extends KAEntity> Class<T> entityType(Region region) {
        switch (region) {
            case CONSUMER:
                return (Class<T>) ConsumerEntity.class;
            default:
                return (Class<T>) ElementEntity.class;
        }
    }

    @Override
    public boolean isValid(Region region, long id) {
        Class<KAEntity> entityType = entityType(region);
        KAEntity r = db.findById(entityType, id);
        return r != null && r.getTtl() >= System.currentTimeMillis();
    }

    @Override
    public void receiveAck(Region region, long id, long ttl) {
        Class<KAEntity> entityType = entityType(region);
        db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            KAEntity r = db.findById(entityType, id);
            if (r == null) {
                return;
            }
            r.setTtl(System.currentTimeMillis() + ttl);
            db.save(r);
        });
    }

    @Override
    public Keepalive newKeepalive(KeepaliveKind kind, Region region, long id, long maxMissDuration) {
        switch (kind) {
            case TCP:
                return new TcpKeepalive(this, region, id, maxMissDuration);
            case WEB_SOCKET:
                return new WebSocketKeepalive(this, region, id, maxMissDuration);
            default:
                return new HttpPassiveKeepalive(this, region, id, maxMissDuration);
        }
    }
}
