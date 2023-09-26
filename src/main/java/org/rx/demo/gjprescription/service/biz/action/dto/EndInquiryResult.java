package org.rx.demo.gjprescription.service.biz.action.dto;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EndInquiryResult {
    @NotNull
    String inquiryId;
    @NotNull
    @Valid
    Doctor doctor;
    @NotNull
    @Valid
    Patient patient;
    @NotNull
    @Valid
    List<PrescriptionEntity> prescriptions;
}
