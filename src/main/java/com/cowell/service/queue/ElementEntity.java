package com.cowell.service.queue;

import com.cowell.core.KAEntity;
import com.cowell.core.QueueElement;
import com.cowell.core.QueueElementStatus;
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
