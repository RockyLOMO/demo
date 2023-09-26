package org.rx.demo.gjmq.service.keepalive;

import org.rx.demo.gjmq.config.Consts;
import org.rx.demo.gjmq.core.KeepaliveManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HttpPassiveController {
//    final KeepaliveManager manager;

    @SneakyThrows
    @RequestMapping("/_ack")
    public void ack(String region, Long id, Long maxMissDuration) {
        if (region == null || id == null) {
            return;
        }
        if (maxMissDuration == null) {
            maxMissDuration = Consts.DEFAULT_MISS_DURATION;
        }
//        manager.receiveAck(KeepaliveManager.Region.valueOf(region), id, maxMissDuration);
    }
}
