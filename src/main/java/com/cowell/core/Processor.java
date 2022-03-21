package com.cowell.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.Disposable;
import org.rx.core.Tasks;

import java.util.concurrent.CompletableFuture;

import static org.rx.core.Extends.tryClose;

@Slf4j
@RequiredArgsConstructor
public class Processor<T extends QueueElement> extends Disposable {
    @Getter
    final Queue<T> queue;
    @Getter
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
                long start = System.currentTimeMillis();
                consumer.consume(elm);
//                log.info("consume elapsed: {}ms", System.currentTimeMillis() - start);
            }
        });
    }
}
