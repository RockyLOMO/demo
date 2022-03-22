package com.cowell.core;

import com.cowell.service.consumer.MemoryStore;
import com.cowell.service.consumer.ShardingH2Store;
import com.cowell.service.queue.MemoryQueue;
import com.cowell.service.queue.ShardingH2Queue;

public enum QueueKind {
    MEMORY,
    SHARDING_H2,
    MYSQL;

    public <T extends QueueElement> Queue<T> newQueue(long capacity) {
        switch (this) {
            case SHARDING_H2:
                return new ShardingH2Queue<>(capacity);
            default:
                return new MemoryQueue<>((int) capacity);
        }
    }

    public <T extends QueueElement> ConsumerStore<T> newStore() {
        switch (this) {
            case SHARDING_H2:
                return new ShardingH2Store<>();
            default:
                return new MemoryStore<>();
        }
    }
}
