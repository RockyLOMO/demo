package org.rx.demo.gjmq.service.biz;

import org.rx.demo.gjmq.core.AbstractQueueElement;
import org.rx.demo.gjmq.core.QueueElementStatus;
import org.rx.demo.gjmq.core.Tag;
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
