package org.rx.demo.gjprescription.service.biz.action.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.action.PharmacistAction;
import org.rx.demo.gjprescription.service.biz.action.dto.AuditPrescriptionParam;
import org.rx.demo.gjprescription.service.biz.action.dto.AuditPrescriptionResult;
import org.rx.demo.gjprescription.service.biz.repository.UserRepository;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;
import org.rx.exception.InvalidException;
import org.springframework.stereotype.Component;

@EnableTrace(doValidate = true)
@Slf4j
@Component
@RequiredArgsConstructor
public class HyPharmacistAction implements PharmacistAction {
    final UserRepository userRepository;

    @Override
    public AuditPrescriptionResult auditPrescription(AuditPrescriptionParam p) {
        Pharmacist pharmacist = userRepository.queryPharmacist(p.getPharmacistId());
        if (pharmacist == null) {
            throw new InvalidException("互医药师{}不存在", p.getPharmacistId());
        }
        log.info("互医药师{}审核处方{}", pharmacist.getName(), p.getPrescriptionNo());
        AuditPrescriptionResult r = new AuditPrescriptionResult();
        r.setPharmacist(pharmacist);
        r.setPrescriptionNo(p.getPrescriptionNo());
        r.setSignMsg("已签名");
        return r;
    }
}
