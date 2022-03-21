package com.cowell.core;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rx.util.function.Action;
import org.rx.util.function.Func;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public abstract class AbstractConsumer<T extends QueueElement> implements Consumer<T> {
    final Lock lock = new ReentrantLock();
    protected boolean acceptable;

    @Override
    public boolean isAcceptable() {
        if (!lock.tryLock()) {
            return false;
        }
        try {
            return acceptable;
        } finally {
            lock.unlock();
        }
    }

    public void setAcceptable(boolean acceptable) {
        lock.lock();
        try {
            this.acceptable = acceptable;
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    protected void syncInvoke(Action fn) {
        lock.lock();
        try {
            fn.invoke();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    protected <R> R syncInvoke(Func<R> fn) {
        lock.lock();
        try {
            return fn.invoke();
        } finally {
            lock.unlock();
        }
    }
}
