package com.cowell.service.biz;

import com.cowell.core.AbstractConsumer;
import com.cowell.core.Tag;
import com.cowell.service.keepalive.Keepalive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.rx.exception.InvalidException;

import java.util.ArrayList;
import java.util.List;

import static org.rx.core.Extends.sleep;
import static org.rx.core.Extends.tryClose;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends AbstractConsumer<Patient> {
    long id;
    String name;
    final Keepalive keepalive;
    final List<Tag> tags = new ArrayList<>();

    @Override
    public boolean isValid() {
        return keepalive.isValid();
    }

    @Override
    protected void freeObjects() {
        tryClose(keepalive);
    }

    //不加锁
    @Override
    public boolean accept(Patient element) {
        return keepalive.isValid() && !isBusy();
    }

    @Override
    public void consume(Patient element) {
        System.out.println(isBusy());
        syncInvoke(() -> {
            if (isBusy()) {
                throw new InvalidException("Not acceptable");
            }

            setBusy(true);
            log.info("consume {} -> {}", this, element);
            sleep(2000);
            setBusy(false);
        });
    }
}
