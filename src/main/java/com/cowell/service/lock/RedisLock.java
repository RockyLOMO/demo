package com.cowell.service.lock;

import com.cowell.core.Lock;

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
