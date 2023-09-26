package org.rx.demo.gjprescription;

import org.junit.jupiter.api.Test;
import org.rx.demo.gjprescription.service.flow.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FlowTest {
    @Autowired
    FlowService flowService;

    @Test
    public void testConf() {
        assert flowService.read() != null;
    }
}
