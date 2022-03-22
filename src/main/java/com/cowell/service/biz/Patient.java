package com.cowell.service.biz;

import com.cowell.core.AbstractQueueElement;
import com.cowell.core.DiscardReason;
import com.cowell.core.Tag;
import com.cowell.service.keepalive.Keepalive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends AbstractQueueElement {
    long id;
    String name;
    final Keepalive keepalive;
    final List<Tag> tags = new ArrayList<>();

    @Override
    public boolean isValid() {
        return keepalive.isValid();
    }

    @Override
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public void onDiscard(DiscardReason reason) {
        log.info("Discard {} -> {}", reason, this);
    }
}
