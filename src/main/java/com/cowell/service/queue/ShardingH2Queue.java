package com.cowell.service.queue;

import com.cowell.core.QueueElement;
import com.cowell.core.QueueKind;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShardingH2Queue<T extends QueueElement> extends com.cowell.core.AbstractQueue<T> {
    final long capacity;

    @Override
    public QueueKind getKind() {
        return null;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public int getOrdinal(T element) {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(T element) {
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
}
