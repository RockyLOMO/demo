package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class MakePrescriptionParam {
    @NotNull
    String inquiryId;
    @NotNull
    @Valid
    PrescriptionEntity prescription;
}
