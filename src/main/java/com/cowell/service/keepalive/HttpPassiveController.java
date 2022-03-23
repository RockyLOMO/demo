package com.cowell.service.keepalive;

import com.cowell.config.Consts;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HttpPassiveController {
    final KeepaliveManager manager;

    @SneakyThrows
    @RequestMapping("/_ack")
    public void ack(String entityType, Long id, Long maxMissDuration) {
        if (entityType == null || id == null) {
            return;
        }
        if (maxMissDuration == null) {
            maxMissDuration = Consts.DEFAULT_MISS_DURATION;
        }
        manager.receiveAck((Class) Class.forName(entityType), id, maxMissDuration);
    }
}
