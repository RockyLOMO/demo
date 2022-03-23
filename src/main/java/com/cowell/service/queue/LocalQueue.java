package com.cowell.service.queue;

import com.cowell.core.*;
import com.cowell.core.Queue;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rx.bean.DateTime;
import org.rx.core.*;
import org.rx.io.EntityDatabase;
import org.rx.io.EntityQueryLambda;

import java.sql.Connection;
import java.util.*;

import static org.rx.core.App.proxy;
import static org.rx.core.Extends.ifNull;
import static org.rx.core.Extends.quietly;

@SuppressWarnings(Constants.NON_UNCHECKED)
@Slf4j
public class LocalQueue<T extends QueueElement> implements Queue<T> {
    final String queueId;
    @Getter
    final long capacity;
    final EntityDatabase db = EntityDatabase.DEFAULT;
    @Getter
    @Setter
    String name;
    @Setter
    long writeBackDelay = 500;

    @Override
    public QueueKind getKind() {
        return QueueKind.LOCAL_H2;
    }

    public LocalQueue(@NonNull String queueId, long capacity) {
        db.createMapping(ElementEntity.class);
        this.queueId = queueId;
        this.capacity = capacity;
    }

    @Override
    public long size() {
        return db.count(query());
    }

    EntityQueryLambda<ElementEntity<T>> query() {
        return (EntityQueryLambda<ElementEntity<T>>) (EntityQueryLambda) new EntityQueryLambda<>(ElementEntity.class).eq(ElementEntity::getQueueId, queueId);
    }

    T wrap(T elm) {
        return (T) proxy(elm.getClass(), (m, p) -> {
            if (m.getName().startsWith("set")) {
                Tasks.setTimeout(() -> db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
                    ElementEntity<T> r = db.findById(ElementEntity.class, elm.getId());
                    if (r == null) {
                        r = new ElementEntity<>();
                        r.setId(elm.getId());
                        r.setQueueId(queueId);
                        r.setCreateTime(DateTime.now());
                    }
                    r.setContent(elm);
                    db.save(r);
                }), writeBackDelay, elm, TimeoutFlag.REPLACE);
            }
            return p.fastInvoke(elm);
        });
    }

    @Override
    public boolean offer(T element) {
        return offer(element, false);
    }

    public boolean offer(@NonNull T element, boolean putFirst) {
        return ifNull(db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            if (db.existsById(ElementEntity.class, element.getId()) || size() > capacity) {
                return false;
            }

            ElementEntity<T> entity = new ElementEntity<>();
            entity.setId(element.getId());
            entity.setQueueId(queueId);
            entity.setContent(element);
            if (putFirst) {
                entity.setCreateTime(DateTime.now().getDateComponent());
            } else {
                entity.setCreateTime(DateTime.now());
            }
            db.save(entity, true);
            synchronized (db) {
                db.notifyAll();
            }
            return true;
        }), false);
    }

    @SneakyThrows
    @Override
    public T take() {
        T elm;
        while ((elm = poll()) == null) {
            synchronized (db) {
                db.wait();
            }
        }
        return wrap(elm);
    }

    T poll() {
        return quietly(() -> db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            List<ElementEntity<T>> rs = db.findBy(query().orderBy(ElementEntity::getCreateTime).limit(1));
            if (CollectionUtils.isEmpty(rs)) {
                return null;
            }
            ElementEntity<T> first = rs.get(0);
            db.deleteById(ElementEntity.class, first.getId());
            return first.content;
        }));
    }

    @Override
    public List<T> peek(int size) {
        return NQuery.of(db.findBy(query().orderBy(ElementEntity::getCreateTime).limit(size))).select(p -> wrap(p.content)).toList();
    }

    @Override
    public T pollById(long id) {
        return db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            ElementEntity<T> elm = (ElementEntity<T>) db.findById(ElementEntity.class, id);
            if (elm == null) {
                return null;
            }
            db.deleteById(ElementEntity.class, id);
            return wrap(elm.content);
        });
    }

    @Override
    public int getOrdinal(@NonNull T element) {
        List<ElementEntity<T>> rs = db.findBy(query().orderBy(ElementEntity::getCreateTime));
        for (int i = 0; i < rs.size(); i++) {
            if (rs.get(i).id == element.getId()) {
                return i;
            }
        }
        return -1;
    }
}
