package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.impl.HyDoctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorRepository {
    public Doctor queryDoctor(String doctorId) {
        HyDoctor d = new HyDoctor();
        d.setId("0x00");
        d.setName("互医医生");
        return d;
    }
}
