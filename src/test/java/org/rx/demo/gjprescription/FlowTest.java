package org.rx.demo.gjprescription;

import org.junit.jupiter.api.Test;
import org.rx.demo.gjprescription.service.biz.action.dto.StartInquiryParam;
import org.rx.demo.gjprescription.service.flow.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FlowTest {
    @Autowired
    FlowService flowService;

    @Test
    public void testFlow() {
        final int flowId = 1024;
        StartInquiryParam startInquiryParam = new StartInquiryParam();
        startInquiryParam.setDoctorId("0x00");
        startInquiryParam.setPatientId("0x01");
        flowService.proceed(flowId, startInquiryParam);
    }

    @Test
    public void testFlowWithError() {
        final int flowId = 1024;
        StartInquiryParam startInquiryParam = new StartInquiryParam();
        startInquiryParam.setDoctorId("1");
        startInquiryParam.setPatientId("0x01");
        flowService.proceed(flowId, startInquiryParam);
    }

    @Test
    public void testConf() {
        assert flowService.readConfig() != null;
    }
}
