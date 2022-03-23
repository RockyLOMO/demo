package com.cowell.service.queue;

import com.cowell.core.QueueElement;
import lombok.Data;
import org.rx.annotation.DbColumn;

import java.io.Serializable;
import java.util.Date;

@Data
public class ElementEntity<T extends QueueElement> implements Serializable {
    @DbColumn(primaryKey = true)
    long id;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    String queueId;
    T content;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    Date createTime;
}
