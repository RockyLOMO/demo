package org.rx.demo.gjprescription.service.biz.action.dto;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class AuditPrescriptionResult {
    @NotNull
    @Valid
    Pharmacist pharmacist;
    @NotNull
    String prescriptionNo;
    @NotNull
    String signMsg;
}
