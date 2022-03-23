package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStatus;
import com.cowell.core.KAEntity;
import com.cowell.core.QueueElement;
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
