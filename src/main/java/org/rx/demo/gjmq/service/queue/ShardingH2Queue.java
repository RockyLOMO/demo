package org.rx.demo.gjmq.service.queue;

import org.rx.demo.gjmq.core.Queue;
import org.rx.demo.gjmq.core.QueueElement;
import org.rx.demo.gjmq.core.QueueKind;
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
    public void setCapacity(long capacity) {

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
    public T poll(Long elementId) {
        return null;
    }

    @Override
    public T peek(Long elementId) {
        return null;
    }

    @Override
    public List<T> peekList(int size) {
        return null;
    }

    @Override
    public int getOrdinal(T element) {
        return 0;
    }
}
