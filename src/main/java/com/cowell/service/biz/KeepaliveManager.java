package com.cowell.service.biz;

import com.cowell.service.Holder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rx.core.Extends;
import org.rx.io.ShardingEntityDatabase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeepaliveManager {
    @RestController
    public static class Controller {
        @RequestMapping("/_ack")
        public void ack(Long id, Long maxMissDuration) {
            if (id == null) {
                return;
            }
            if (maxMissDuration == null) {
                maxMissDuration = Holder.DEFAULT_MISS_DURATION;
            }
            INSTANCE.receiveAck(id, maxMissDuration);
        }
    }

    @Data
    static class Entity implements Extends {
        long id;
        long maxMissDuration;
        long lastAckTime;
    }

    public static final KeepaliveManager INSTANCE = new KeepaliveManager();
    final ShardingEntityDatabase db = Holder.EDB;

    public boolean isValid(long id) {
        Entity entity = db.findById(Entity.class, id);
        return entity != null && (entity.lastAckTime - System.currentTimeMillis()) <= entity.maxMissDuration;
    }

    public void receiveAck(long id, long maxMissDuration) {
        Entity entity = db.findById(Entity.class, id);
        if (entity == null) {
            entity = new Entity();
            entity.setId(id);
            entity.setMaxMissDuration(maxMissDuration);
        }
        entity.setLastAckTime(System.currentTimeMillis());
        db.save(entity);
    }
}
