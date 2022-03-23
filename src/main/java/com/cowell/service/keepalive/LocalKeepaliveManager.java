package com.cowell.service.keepalive;

import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveKind;
import com.cowell.core.KeepaliveManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rx.io.EntityDatabase;

import java.sql.Connection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalKeepaliveManager implements KeepaliveManager {
    final EntityDatabase db = EntityDatabase.DEFAULT;

    public boolean isValid(long id) {
        KAEntity entity = db.findById(KAEntity.class, id);
        return entity != null && (entity.lastAckTime - System.currentTimeMillis()) <= entity.maxMissDuration;
    }

    public void receiveAck(long id, long maxMissDuration) {
        db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            KAEntity entity = db.findById(KAEntity.class, id);
            if (entity == null) {
                entity = new KAEntity();
                entity.setId(id);
                entity.setMaxMissDuration(maxMissDuration);
            }
            entity.setLastAckTime(System.currentTimeMillis());
            db.save(entity);
        });
    }

    @Override
    public Keepalive newKeepalive(KeepaliveKind kind, long id, long maxMissDuration) {
        switch (kind) {
            case TCP:
                return new TcpKeepalive(this, id, maxMissDuration);
            case WEB_SOCKET:
                return new WebSocketKeepalive(this, id, maxMissDuration);
            default:
                return new HttpPassiveKeepalive(this, id, maxMissDuration);
        }
    }
}
