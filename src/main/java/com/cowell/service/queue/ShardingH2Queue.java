package com.cowell.service.queue;

import com.cowell.core.Queue;
import com.cowell.core.QueueElement;
import com.cowell.core.QueueKind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
public class ShardingH2Queue<T extends QueueElement> implements Queue<T> {
    final long capacity;
    @Getter
    @Setter
    String name;

    @Override
    public QueueKind getKind() {
        return QueueKind.SHARDING_H2;
    }

    @Override
    public long getCapacity() {
        return 0;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean offer(T element, boolean putFirst) {
        return false;
    }

    @Override
    public T take() {
        return null;
    }

    @Override
    public List<T> peek(int size) {
        return null;
    }

    @Override
    public T pollById(long id) {
        return null;
    }

    @Override
    public int getOrdinal(T element) {
        return 0;
    }
}
