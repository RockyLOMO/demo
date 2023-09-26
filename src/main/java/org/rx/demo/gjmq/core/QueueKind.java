package org.rx.demo.gjmq.core;

import org.rx.demo.gjmq.service.consumer.LocalGroup;
import org.rx.demo.gjmq.service.consumer.ShardingH2Store;
import org.rx.demo.gjmq.service.queue.LocalQueue;
import org.rx.demo.gjmq.service.queue.ShardingH2Queue;

public enum QueueKind {
    LOCAL_H2,
    SHARDING_H2,
    MYSQL;

    public <T extends QueueElement> Queue<T> newQueue(String queueId, long capacity) {
        switch (this) {
            case SHARDING_H2:
                return new ShardingH2Queue<>(capacity);
            default:
                return new LocalQueue<>(queueId, capacity);
        }
    }

    public <T extends QueueElement> ConsumerGroup<T> newGroup(String groupId) {
        switch (this) {
            case SHARDING_H2:
                return new ShardingH2Store<>();
            default:
                return new LocalGroup<>(groupId);
        }
    }
}
