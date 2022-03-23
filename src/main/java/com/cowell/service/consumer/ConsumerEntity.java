package com.cowell.service.consumer;

import com.cowell.core.Consumer;
import com.cowell.core.QueueElement;
import lombok.Data;
import org.rx.annotation.DbColumn;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConsumerEntity<T extends QueueElement> implements Serializable {
    @DbColumn(primaryKey = true)
    long id;
    String groupId;
    boolean valid;
    long queueSize;
    Consumer<T> content;
    Date createTime;
}
