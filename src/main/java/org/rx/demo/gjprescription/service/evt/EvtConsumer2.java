package org.rx.demo.gjprescription.service.evt;

import lombok.extern.slf4j.Slf4j;
import org.rx.annotation.Subscribe;
import org.rx.core.EventBus;
import org.rx.demo.gjprescription.service.biz.model.StartInquiryResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class EvtConsumer2 {
    @Subscribe
    public void onEvt(StartInquiryResult p) {
        //dto 属性可以透明懒加载
        log.info("开始问诊事件触发{}------22", p);

    }

    @PostConstruct
    public void init() {
        EventBus.DEFAULT.register(this);
    }
}
