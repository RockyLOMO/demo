package com.cowell.service.lock;

import com.cowell.core.Lock;

import java.util.concurrent.locks.ReentrantLock;

public class LocalLock implements Lock {
    final java.util.concurrent.locks.Lock lock = new ReentrantLock();

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}
