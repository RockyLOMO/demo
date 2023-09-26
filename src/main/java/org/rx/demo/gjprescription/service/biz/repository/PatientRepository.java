package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;
import org.rx.demo.gjprescription.service.biz.user.impl.HyDoctor;
import org.rx.demo.gjprescription.service.biz.user.impl.HyPatient;
import org.springframework.stereotype.Component;

@Component
public class PatientRepository {
    public Patient queryPatient(String patientId) {
        HyPatient d = new HyPatient();
        d.setId("0x01");
        d.setName("欧比王");
        return d;
    }
}
