package org.rx.demo.gjprescription.service.biz.action.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditPrescriptionParam {
    @NotNull
    String pharmacistId;
    @NotNull
    String prescriptionNo;
}
