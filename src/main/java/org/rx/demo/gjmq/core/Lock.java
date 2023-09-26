package org.rx.demo.gjmq.core;

public interface Lock {
    boolean tryLock();

    void lock();

    void unlock();
}
