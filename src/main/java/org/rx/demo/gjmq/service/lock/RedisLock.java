package org.rx.demo.gjmq.service.lock;

import org.rx.demo.gjmq.core.Lock;

public class RedisLock implements Lock {
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }
}
