package com.cowell.service.keepalive;

import com.cowell.core.KAEntity;
import com.cowell.core.Keepalive;
import com.cowell.core.KeepaliveKind;
import com.cowell.core.KeepaliveManager;
import io.netty.util.concurrent.FastThreadLocal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rx.exception.InvalidException;
import org.rx.io.EntityDatabase;

import java.sql.Connection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalKeepaliveManager implements KeepaliveManager {
    //    public static final FastThreadLocal<Class> ENTITY_TYPE = new FastThreadLocal<>();
    final EntityDatabase db = EntityDatabase.DEFAULT;

//    <T extends KAEntity> Class<T> entityType() {
//        Class<T> type = ENTITY_TYPE.get();
//        if (type == null) {
//            throw new InvalidException("No entity type from context");
//        }
//        return type;
//    }

    public <T extends KAEntity> boolean isValid(Class<T> entityType, long id) {
        T r = db.findById(entityType, id);
        return r != null && r.getTtl() >= System.currentTimeMillis();
    }

    public <T extends KAEntity> void receiveAck(Class<T> entityType, long id, long maxMissDuration) {
        db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            T r = db.findById(entityType, id);
            if (r == null) {
                return;
            }
            r.setTtl(System.currentTimeMillis() + maxMissDuration);
            db.save(r);
        });
    }

    @Override
    public <T extends KAEntity> Keepalive newKeepalive(KeepaliveKind kind, T entity, long maxMissDuration) {
        switch (kind) {
            case TCP:
                return new TcpKeepalive(this, entity, maxMissDuration);
            case WEB_SOCKET:
                return new WebSocketKeepalive(this, entity, maxMissDuration);
            default:
                return new HttpPassiveKeepalive(this, entity, maxMissDuration);
        }
    }
}
