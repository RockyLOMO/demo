package com.cowell.core;

public interface Lock {
    boolean tryLock();

    void lock();

    void unlock();
}
