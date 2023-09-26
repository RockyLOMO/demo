package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.user.Pharmacist;
import org.rx.demo.gjprescription.service.biz.user.impl.HyPharmacist;
import org.springframework.stereotype.Component;

@Component
public class PharmacistRepository {
    public Pharmacist queryPharmacist(String pharmacistId) {
        HyPharmacist pharmacist = new HyPharmacist();
        pharmacist.setId("0x02");
        pharmacist.setName("互医药师");
        return pharmacist;
    }
}
