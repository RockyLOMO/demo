package org.rx.demo.gjprescription.service.biz.action;

import org.rx.demo.gjprescription.service.biz.model.AuditPrescriptionParam;
import org.rx.demo.gjprescription.service.biz.model.AuditPrescriptionResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PharmacistAction extends Stage {
    @Override
    default StageType getType() {
        return StageType.DISPENSING;
    }

    @NotNull
    AuditPrescriptionResult auditPrescription(@NotNull @Valid AuditPrescriptionParam p);
}
