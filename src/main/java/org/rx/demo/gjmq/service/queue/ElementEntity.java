package org.rx.demo.gjmq.service.queue;

import org.rx.demo.gjmq.core.KAEntity;
import org.rx.demo.gjmq.core.QueueElement;
import org.rx.demo.gjmq.core.QueueElementStatus;
import lombok.Data;
import org.rx.annotation.DbColumn;

import java.util.Date;

@Data
public class ElementEntity<T extends QueueElement> implements KAEntity {
    @DbColumn(primaryKey = true)
    long id;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    String queueId;
    QueueElementStatus status;
    T content;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    Date createTime;

    long ttl;
}
