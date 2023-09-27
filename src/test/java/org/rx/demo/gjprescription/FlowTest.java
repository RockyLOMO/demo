package org.rx.demo.gjprescription;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.demo.gjprescription.service.biz.action.impl.HyDoctorAction;
import org.rx.demo.gjprescription.service.biz.model.MakePrescriptionParam;
import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;
import org.rx.demo.gjprescription.service.biz.model.StartInquiryParam;
import org.rx.demo.gjprescription.service.biz.model.StartInquiryResult;
import org.rx.demo.gjprescription.service.flow.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;

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
        StartInquiryResult startInquiryResult = flowService.proceed(flowId, startInquiryParam);

        MakePrescriptionParam makePrescriptionParam = new MakePrescriptionParam();
        makePrescriptionParam.setInquiryId(startInquiryResult.getInquiryId());
        PrescriptionEntity prescription = new PrescriptionEntity();
        prescription.setDoctorId(startInquiryResult.getDoctor().getId());
        prescription.setPatientId(startInquiryResult.getPatient().getId());
        makePrescriptionParam.setPrescription(prescription);
        flowService.proceed(flowId, makePrescriptionParam);
//        flowService.proceed(flowId, null);
    }

    @Test
    public void testFlowWithError() {
        final int flowId = 1024;
        StartInquiryParam startInquiryParam = new StartInquiryParam();
        startInquiryParam.setDoctorId("1");
        startInquiryParam.setPatientId("0x01");
        flowService.proceed(flowId, startInquiryParam);
    }
//
//    @SneakyThrows
//    @Test
//    public void xx(){
//        AnnotationUtils.findAnnotation(HyDoctorAction.class.getMethod("startInquiry",StartInquiryParam.class));
//    }

    @Test
    public void testConf() {
        assert flowService.readConfig() != null;
    }
}
