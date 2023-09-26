package org.rx.demo.gjmq.service.consumer;

import org.rx.demo.gjmq.core.Consumer;
import org.rx.demo.gjmq.core.ConsumerStatus;
import org.rx.demo.gjmq.core.KAEntity;
import org.rx.demo.gjmq.core.QueueElement;
import lombok.Data;
import org.rx.annotation.DbColumn;

import java.util.Date;

@Data
public class ConsumerEntity<T extends QueueElement> implements KAEntity {
    @DbColumn(primaryKey = true)
    long id;
    String groupId;
    boolean suspend;
    ConsumerStatus status;
    long queueSize;
    Consumer<T> content;
    Date createTime;

    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    long ttl;
}
