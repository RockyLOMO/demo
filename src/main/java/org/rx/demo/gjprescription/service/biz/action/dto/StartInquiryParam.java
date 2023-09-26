package org.rx.demo.gjprescription.service.biz.action.dto;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class StartInquiryParam {
    @NotNull
    String doctorId;
    @NotNull
    String patientId;
}
