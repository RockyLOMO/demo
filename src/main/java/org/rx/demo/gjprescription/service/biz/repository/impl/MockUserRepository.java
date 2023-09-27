package org.rx.demo.gjprescription.service.biz.repository.impl;

import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.repository.UserRepository;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;
import org.rx.demo.gjprescription.service.biz.user.UserRole;
import org.rx.demo.gjprescription.service.biz.user.impl.HyDoctor;
import org.rx.demo.gjprescription.service.biz.user.impl.HyPatient;
import org.rx.demo.gjprescription.service.biz.user.impl.HyPharmacist;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableTrace(doValidate = true)
@Component
public class MockUserRepository implements UserRepository {
    final Map<String, UserRole> d = new ConcurrentHashMap<>();

    @Override
    public Doctor queryDoctor(String doctorId) {
        return (Doctor) d.get(doctorId);
    }

    @Override
    public Patient queryPatient(String patientId) {
        return (Patient) d.get(patientId);
    }

    @Override
    public Pharmacist queryPharmacist(String pharmacistId) {
        return (Pharmacist) d.get(pharmacistId);
    }

    @PostConstruct
    public void init() {
        HyDoctor u0 = new HyDoctor();
        u0.setId("0x00");
        u0.setName("互医医生");
        d.put(u0.getId(), u0);

        HyPatient u1 = new HyPatient();
        u1.setId("0x01");
        u1.setName("欧比王");
        d.put(u1.getId(), u1);

        HyPharmacist u2 = new HyPharmacist();
        u2.setId("0x02");
        u2.setName("互医药师");
        d.put(u2.getId(), u2);

        HyPharmacist u3 = new HyPharmacist();
        u3.setId("0x03");
        u3.setName("发药药师");
        d.put(u3.getId(), u3);
    }
}
