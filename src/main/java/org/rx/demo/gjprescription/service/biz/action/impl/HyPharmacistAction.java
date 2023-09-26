package org.rx.demo.gjprescription.service.biz.action.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.demo.gjprescription.service.biz.action.PharmacistAction;
import org.rx.demo.gjprescription.service.biz.action.dto.AuditPrescriptionParam;
import org.rx.demo.gjprescription.service.biz.action.dto.AuditPrescriptionResult;
import org.rx.demo.gjprescription.service.biz.repository.PharmacistRepository;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HyPharmacistAction implements PharmacistAction {
    final PharmacistRepository pharmacistRepository;

    @Override
    public AuditPrescriptionResult auditPrescription(AuditPrescriptionParam p) {
        Pharmacist pharmacist = pharmacistRepository.queryPharmacist(p.getPharmacistId());
        log.info("药师{}审核处方{}", pharmacist.getName(), p.getPrescriptionNo());
        AuditPrescriptionResult r = new AuditPrescriptionResult();
        r.setPharmacist(pharmacist);
        r.setPrescriptionNo(p.getPrescriptionNo());
        r.setSignMsg("已签名");
        return r;
    }
}
