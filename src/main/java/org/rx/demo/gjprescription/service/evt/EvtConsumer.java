package org.rx.demo.gjprescription.service.evt;

import lombok.extern.slf4j.Slf4j;
import org.rx.annotation.Subscribe;
import org.rx.core.EventBus;
import org.rx.demo.gjprescription.service.biz.action.dto.StartInquiryResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class EvtConsumer {
    @Subscribe
    public void onEvt(StartInquiryResult p) {
        log.info("开始问诊事件触发{}", p);
    }

    @PostConstruct
    public void init() {
        EventBus.DEFAULT.register(this);
    }
}
