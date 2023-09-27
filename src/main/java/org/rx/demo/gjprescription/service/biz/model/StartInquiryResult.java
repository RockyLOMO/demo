package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class StartInquiryResult {
    /**
     * 医生
     */
    @NotNull
    @Valid
    Doctor doctor;
    /**
     * 患者
     */
    @NotNull
    @Valid
    Patient patient;
    @NotNull
    String inquiryId;
}
