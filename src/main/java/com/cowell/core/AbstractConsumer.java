package com.cowell.core;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rx.core.Disposable;
import org.rx.util.function.Action;
import org.rx.util.function.Func;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public abstract class AbstractConsumer<T extends QueueElement> extends Disposable implements Consumer<T> {
    final Lock lock = new ReentrantLock();
    private boolean busy;

    public boolean isBusy() {
        if (!lock.tryLock()) {
            return true;
        }
        try {
            return busy;
        } finally {
            lock.unlock();
        }
    }

    protected void setBusy(boolean busy) {
        lock.lock();
        try {
            this.busy = busy;
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
