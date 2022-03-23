package com.cowell.core;

import com.cowell.service.consumer.LocalGroup;
import com.cowell.service.consumer.ShardingH2Store;
import com.cowell.service.queue.LocalQueue;
import com.cowell.service.queue.ShardingH2Queue;

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
