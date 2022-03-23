package com.cowell.service.biz;

import com.cowell.core.Consumer;
import com.cowell.core.ConsumerStatus;
import com.cowell.core.Tag;
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
