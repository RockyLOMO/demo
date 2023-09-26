package org.rx.demo.gjmq.service.consumer;

import lombok.Data;
import org.rx.annotation.DbColumn;

import java.io.Serializable;

@Data
public class TagEntity implements Serializable {
    @DbColumn(primaryKey = true)
    long id;
    String groupId;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    long consumerId;
    @DbColumn(index = DbColumn.IndexKind.INDEX_ASC)
    String name;
    String value;
}
