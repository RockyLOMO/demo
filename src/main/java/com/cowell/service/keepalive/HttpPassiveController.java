package com.cowell.service.keepalive;

import com.cowell.config.Consts;
import com.cowell.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HttpPassiveController {
    final KeepaliveManager manager;

    @RequestMapping("/_ack")
    public void ack(Long id, Long maxMissDuration) {
        if (id == null) {
            return;
        }
        if (maxMissDuration == null) {
            maxMissDuration = Consts.DEFAULT_MISS_DURATION;
        }
        manager.receiveAck(id, maxMissDuration);
    }
}
