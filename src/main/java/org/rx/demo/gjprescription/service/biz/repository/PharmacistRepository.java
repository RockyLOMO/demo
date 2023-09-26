package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.user.Pharmacist;
import org.rx.demo.gjprescription.service.biz.user.impl.HyPharmacist;
import org.springframework.stereotype.Component;

import static org.rx.core.Extends.eq;

@Component
public class PharmacistRepository {
    public Pharmacist queryPharmacist(String pharmacistId) {
        HyPharmacist pharmacist = new HyPharmacist();
        if (eq(pharmacistId, "0x03")) {
            pharmacist.setId("0x03");
            pharmacist.setName("发药药师");
        } else {
            pharmacist.setId("0x02");
            pharmacist.setName("互医药师");
        }
        return pharmacist;
    }
}
