package com.cowell.service.biz;

import com.cowell.core.AbstractQueueElement;
import com.cowell.core.QueueElementStatus;
import com.cowell.core.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends AbstractQueueElement {
    long id;
    QueueElementStatus status = QueueElementStatus.TRANSIENT;
    List<Tag> preferTags;

    String name;
}
