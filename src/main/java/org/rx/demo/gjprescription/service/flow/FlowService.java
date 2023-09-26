package org.rx.demo.gjprescription.service.flow;

import lombok.extern.slf4j.Slf4j;
import org.rx.core.YamlConfiguration;
import org.rx.demo.gjprescription.service.flow.conf.Flow;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FlowService {
    public Flow read() {
        Flow flow = YamlConfiguration.RX_CONF.readAs("app.flow", Flow.class);
        log.info("load conf {}", flow);
        return flow;
    }
}
