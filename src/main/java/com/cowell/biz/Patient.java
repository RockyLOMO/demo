package com.cowell.biz;

import com.cowell.core.DiscardReason;
import com.cowell.core.QueueElement;
import com.cowell.core.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class Patient implements QueueElement {
    long id;
    String name;

    boolean valid;
    final List<Tag> tags = new ArrayList<>();

    @Override
    public List<Tag> preferTags() {
        return tags;
    }

    @Override
    public void onDiscard(DiscardReason reason) {
        log.info("Discard {} -> {}", this, reason);
    }
}
