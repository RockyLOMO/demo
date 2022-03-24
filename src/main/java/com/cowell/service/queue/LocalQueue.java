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
import org.rx.bean.DynamicProxy;
import org.rx.core.*;
import org.rx.io.EntityDatabase;
import org.rx.io.EntityQueryLambda;
import org.rx.io.IOStream;
import org.rx.util.function.Func;

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
    @Setter
    long capacity;
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

    public void setPreferId(long preferId) {
        db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            long id = computeId(preferId);
            ElementEntity<T> r = db.findById(ElementEntity.class, id);
            if (r == null) {
                return;
            }
            r.setCreateTime(DateTime.MIN);
            db.save(r, false);
        });
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
        return (EntityQueryLambda<ElementEntity<T>>) (EntityQueryLambda) new EntityQueryLambda<>(ElementEntity.class)
                .eq(ElementEntity::getQueueId, queueId)
                .eq(ElementEntity::getStatus, QueueElementStatus.QUEUED);
    }

    @Override
    public long computeId(long elementId) {
        return IOStream.checksum(queueId, String.valueOf(elementId));
    }

    T wrap(T elm) {
        if (elm instanceof DynamicProxy.Proxifier) {
            return elm;
        }
        return proxy(elm.getClass(), (m, p) -> {
            if (m.getName().startsWith("set")) {
                Tasks.setTimeout(() -> db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
                    long id = computeId(elm.getId());
                    ElementEntity<T> r = db.findById(ElementEntity.class, id);
                    boolean doInsert = r == null;
                    if (doInsert) {
                        r = new ElementEntity<>();
                        r.setId(id);
                        r.setQueueId(queueId);
                        r.setCreateTime(DateTime.now());
                    }
                    log.debug("{} status change {} -> {}", elm.getId(), r.getStatus(), elm.getStatus());
                    r.setStatus(elm.getStatus());
                    r.setContent(elm);
                    db.save(r, doInsert);
                }), writeBackDelay, elm, TimeoutFlag.REPLACE);
            }
            return p.fastInvoke(elm);
        }, elm, false);
    }

    @Override
    public boolean offer(@NonNull T element, boolean putFirst) {
        return ifNull(db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            long id = computeId(element.getId());
            ElementEntity<T> r = db.findById(ElementEntity.class, id);
            log.debug("{}.offer: {} {}", queueId, r, element);
            if ((r != null && !CAN_OFFER_STATUS.contains(r.getStatus())) || size() > capacity) {
                return false;
            }

            T elm = element instanceof DynamicProxy.Proxifier ? ((DynamicProxy.Proxifier) element).rawObject() : element;
            boolean doInsert = r == null;
            if (doInsert) {
                r = new ElementEntity<>();
                r.setId(id);
                r.setQueueId(queueId);
                if (putFirst) {
                    r.setCreateTime(DateTime.MIN);
                } else {
                    r.setCreateTime(DateTime.now());
                }
            }
            r.setStatus(QueueElementStatus.QUEUED);
            elm.setStatus(r.getStatus());
            r.setContent(elm);
            db.save(r, doInsert);
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
        while ((elm = quietly((Func<T>) this::poll)) == null) {
            synchronized (db) {
                db.wait(writeBackDelay);
            }
        }
        return elm;
    }

    @Override
    public T poll(Long elementId) {
        return db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            ElementEntity<T> elm = first(elementId);
            if (elm == null) {
                return null;
            }
            elm.setStatus(QueueElementStatus.TAKEN);
            elm.content.setStatus(elm.getStatus());
            db.save(elm, false);
            return wrap(elm.content);
        });
    }

    ElementEntity<T> first(Long elementId) {
        ElementEntity<T> elm;
        if (elementId != null) {
            long id = computeId(elementId);
            if ((elm = (ElementEntity<T>) db.findById(ElementEntity.class, id)) == null) {
                return null;
            }
        } else {
            List<ElementEntity<T>> rs = db.findBy(query().orderBy(ElementEntity::getCreateTime).limit(1));
            if (CollectionUtils.isEmpty(rs)) {
                return null;
            }
            elm = rs.get(0);
        }
        return elm;
    }

    @Override
    public T peek(Long elementId) {
        ElementEntity<T> elm = first(elementId);
        if (elm == null) {
            return null;
        }
        return wrap(elm.content);
    }

    @Override
    public List<T> peekList(int size) {
        return NQuery.of(db.findBy(query().orderBy(ElementEntity::getCreateTime).limit(size))).select(p -> wrap(p.content)).toList();
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
