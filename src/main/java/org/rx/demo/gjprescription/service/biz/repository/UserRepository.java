package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;

import javax.validation.constraints.NotNull;

public interface UserRepository {
    Doctor queryDoctor(@NotNull String doctorId);

    Patient queryPatient(@NotNull String patientId);

    Pharmacist queryPharmacist(@NotNull String pharmacistId);
}
