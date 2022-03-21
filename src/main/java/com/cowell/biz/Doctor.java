package com.cowell.biz;

import com.cowell.core.Consumer;
import com.cowell.core.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.rx.core.NQuery;
import org.rx.exception.InvalidException;

import java.util.ArrayList;
import java.util.List;

import static org.rx.core.Extends.sleep;

@Slf4j
@Data
public class Doctor implements Consumer<Patient> {
    String name;

    boolean valid;
    final List<Tag> tags = new ArrayList<>();
    boolean acceptable;

    @Override
    public int matchTags(List<Tag> tags) {
        return NQuery.of(tags).intersection(tags).count();
    }

    public synchronized boolean isAcceptable() {
        return acceptable;
    }

    @Override
    public synchronized boolean accept(Patient element) {
        return valid && acceptable;
    }

    @Override
    public synchronized void consume(Patient element) {
        if (!acceptable) {
            throw new InvalidException("Not acceptable");
        }

        acceptable = false;
        log.info("consume {} -> {}", this, element);
        sleep(1000);
        acceptable = true;
    }
}
