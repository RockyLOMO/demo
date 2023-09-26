package org.rx.demo.gjmq.service.biz;

import org.rx.demo.gjmq.core.Consumer;
import org.rx.demo.gjmq.core.ConsumerStatus;
import org.rx.demo.gjmq.core.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class Doctor implements Consumer<Patient> {
    long id;
    boolean suspend;
    ConsumerStatus status = ConsumerStatus.IDLE;
    List<Tag> tags;

    String name;
}
