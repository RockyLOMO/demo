package org.rx.demo.gjmq.core;

import java.util.List;

public interface ConsumerHandler<T extends QueueElement> {
    boolean testAccept(long consumerId, T element);

    boolean accept(long consumerId, T element);

    List<T> getAcceptedList(long consumerId);

    T beginConsume(long consumerId, Long preferElementId);

    void endConsume(long consumerId);
}
