package com.cowell.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rx.core.Disposable;
import org.rx.core.Tasks;

import java.util.concurrent.CompletableFuture;

import static org.rx.core.Extends.tryClose;

@Getter
@RequiredArgsConstructor
public class Processor<T extends QueueElement> extends Disposable {
    final Queue<T> queue;
    final Consumer<T> consumer;
    CompletableFuture<Void> future;

    @Override
    protected void freeObjects() {
        tryClose(queue);
        tryClose(consumer);
    }

    public synchronized CompletableFuture<Void> startAsync() {
        if (future != null) {
            return future;
        }
        return future = Tasks.run(() -> {
            while (!isClosed()) {
                T elm = queue.take();
                consumer.consume(elm);
            }
        });
    }
}
