package com.cowell.service.consumer;

import com.cowell.core.*;
import com.cowell.core.Queue;
import org.apache.commons.collections4.CollectionUtils;
import org.rx.bean.DateTime;
import org.rx.core.Constants;
import org.rx.core.NQuery;
import org.rx.core.Strings;
import org.rx.io.EntityDatabase;
import org.rx.io.EntityQueryLambda;
import org.rx.io.IOStream;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.rx.core.Extends.*;

@SuppressWarnings(Constants.NON_UNCHECKED)
public class LocalGroup<T extends QueueElement> implements ConsumerGroup<T> {
    final String groupId;
    final EntityDatabase db = EntityDatabase.DEFAULT;
    final Map<Long, Queue<T>> subQueues = new ConcurrentHashMap<>();

    @Override
    public QueueKind getKind() {
        return QueueKind.LOCAL_H2;
    }

    public LocalGroup(String groupId) {
        db.createMapping(ConsumerEntity.class, TagEntity.class);
        this.groupId = groupId;
    }

    EntityQueryLambda<ConsumerEntity<T>> query() {
        return (EntityQueryLambda<ConsumerEntity<T>>) (EntityQueryLambda) new EntityQueryLambda<>(ConsumerEntity.class).eq(ConsumerEntity::getGroupId, groupId);
    }

    EntityQueryLambda<TagEntity> queryTag() {
        return new EntityQueryLambda<>(TagEntity.class).eq(TagEntity::getGroupId, groupId);
    }

    long computeId(long consumerId) {
        return IOStream.checksum(groupId, String.valueOf(consumerId));
    }

    long computeTagId(long consumerId, Tag tag) {
        require(tag, !Strings.isEmpty(tag.getName()));

        return IOStream.checksum(groupId, String.valueOf(consumerId), tag.getName(), tag.getValue());
    }

    String getQueueId(long consumerId) {
        return String.format("CONSUMER-%s", consumerId);
    }

    @Override
    public long size() {
        return db.count(query());
    }

    @Override
    public boolean add(Consumer<T> consumer) {
        return ifNull(db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            long id = computeId(consumer.getId());
            ConsumerEntity<T> r = db.findById(ConsumerEntity.class, id);
            if (r == null) {
                r = new ConsumerEntity<>();
                r.setId(id);
                r.setGroupId(groupId);
                r.setCreateTime(DateTime.now());
            }
            r.setQueueSize(getConsumerQueue(consumer.getId()).size());
            r.setContent(consumer);
            db.save(r);

            for (Tag tag : consumer.getTags()) {
                if (Strings.isEmpty(tag.getName())) {
                    continue;
                }

                TagEntity tr = new TagEntity();
                tr.setId(computeTagId(r.getId(), tag));
                tr.setGroupId(groupId);
                tr.setConsumerId(r.getId());
                tr.setName(tag.getName());
                tr.setValue(tag.getValue());
                db.save(tr);
            }
            return true;
        }), false);
    }

    @Override
    public boolean remove(Consumer<T> consumer) {
        return ifNull(db.transInvoke(Connection.TRANSACTION_NONE, () -> {
            db.delete(queryTag().eq(TagEntity::getConsumerId, consumer.getId()));
            return db.deleteById(ConsumerEntity.class, computeId(consumer.getId()));
        }), false);
    }

    @Override
    public Queue<T> getConsumerQueue(long consumerId) {
        return subQueues.computeIfAbsent(consumerId, k -> getKind().newQueue(getQueueId(consumerId), Integer.MAX_VALUE));
    }

    @Override
    public Consumer<T> next() {
        List<ConsumerEntity<T>> r = db.findBy(query().eq(ConsumerEntity::isValid, true).orderByDescending(ConsumerEntity::getQueueSize).limit(1));
        if (CollectionUtils.isEmpty(r)) {
            return null;
        }
        return r.get(0).content;
    }

    @Override
    public List<Consumer<T>> nextList(int size) {
        List<ConsumerEntity<T>> r = db.findBy(query().eq(ConsumerEntity::isValid, true).orderByDescending(ConsumerEntity::getQueueSize).limit(size));
        if (CollectionUtils.isEmpty(r)) {
            return Collections.emptyList();
        }
        return NQuery.of(r).select(p -> p.content).toList();
    }

    @Override
    public Set<Tag> getTagCapacities() {
        //todo groupby
        return NQuery.of(db.findBy(queryTag())).select(p -> new Tag(p.getName(), p.getValue())).toSet();
    }

    @Override
    public List<Consumer<T>> findByTags(List<Tag> tags) {
        return db.transInvoke(Connection.TRANSACTION_READ_COMMITTED, () -> {
            List<TagEntity> r = db.findBy(queryTag().in(TagEntity::getName, NQuery.of(tags).select(Tag::getName).toArray()));
            if (CollectionUtils.isEmpty(r)) {
                return Collections.emptyList();
            }
            Long[] cIds = NQuery.of(r).join(tags, (p, x) -> eq(p.value, x.getValue()), (p, x) -> p.consumerId).distinct().toArray();
            if (cIds.length == 0) {
                return Collections.emptyList();
            }
            return NQuery.of(db.findBy(query().in(ConsumerEntity::getId, cIds))).select(p -> p.content).toList();
        });
    }
}
