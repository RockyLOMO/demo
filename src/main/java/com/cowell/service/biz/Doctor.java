package com.cowell.service.biz;

import com.cowell.core.AbstractConsumer;
import com.cowell.core.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.NQuery;
import org.rx.exception.InvalidException;

import java.util.ArrayList;
import java.util.List;

import static org.rx.core.Extends.sleep;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends AbstractConsumer<Patient> {
    long id;
    String name;
    final Keepalive keepalive;
    final List<Tag> tags = new ArrayList<>();

    @Override
    public int matchTags(List<Tag> tags) {
        return NQuery.of(this.tags).intersection(tags).count();
    }

    @Override
    public boolean isValid() {
        return keepalive.isValid();
    }

    @Override
    public boolean accept(Patient element) {
        return syncInvoke(() -> keepalive.isValid() && acceptable);
    }

    @Override
    public void consume(Patient element) {
        syncInvoke(() -> {
            if (!acceptable) {
                throw new InvalidException("Not acceptable");
            }

            acceptable = false;
            log.info("consume {} -> {}", this, element);
            sleep(2000);
            acceptable = true;
        });
    }
}
