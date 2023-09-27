package org.rx.demo.gjprescription.service.biz.action.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.action.DoctorAction;
import org.rx.demo.gjprescription.service.biz.model.*;
import org.rx.demo.gjprescription.service.biz.repository.PrescriptionRepository;
import org.rx.demo.gjprescription.service.biz.repository.UserRepository;
import org.rx.demo.gjprescription.service.biz.repository.InquiryRepository;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;
import org.rx.exception.InvalidException;
import org.rx.util.Snowflake;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class HyDoctorAction implements DoctorAction {
    final UserRepository userRepository;
    final InquiryRepository inquiryRepository;
    final PrescriptionRepository prescriptionRepository;

    @Override
    public StartInquiryResult startInquiry(StartInquiryParam p) {
        Doctor doctor = userRepository.queryDoctor(p.getDoctorId());
        if (doctor == null) {
            throw new InvalidException("医生{}不存在", p.getDoctorId());
        }
        Patient patient = userRepository.queryPatient(p.getPatientId());
        if (patient == null) {
            throw new InvalidException("患者{}不存在", p.getPatientId());
        }

        log.info("医生{}开始问诊患者{}", doctor.getName(), patient.getName());
        StartInquiryResult r = new StartInquiryResult();
        r.setDoctor(doctor);
        r.setPatient(patient);
        r.setInquiryId(String.valueOf(Snowflake.DEFAULT.nextId()));
        inquiryRepository.saveInquiry(r);
        return r;
    }

    @Override
    public MakePrescriptionResult makePrescription(MakePrescriptionParam p) {
        StartInquiryResult inquiryResult = inquiryRepository.queryInquiry(p.getInquiryId());
        if (inquiryResult == null) {
            throw new InvalidException("问诊{}不存在", p.getInquiryId());
        }

        Doctor doctor = inquiryResult.getDoctor();
        Patient patient = inquiryResult.getPatient();
        log.info("医生{}给患者{}开具处方{}", doctor.getName(), patient.getName(), p.getPrescription());
        prescriptionRepository.savePrescription(p.getPrescription());
        MakePrescriptionResult r = new MakePrescriptionResult();
        r.setInquiryId(inquiryResult.getInquiryId());
        r.setDoctor(inquiryResult.getDoctor());
        r.setPatient(inquiryResult.getPatient());
        r.setPrescriptions(prescriptionRepository.queryPrescriptions(inquiryResult.getInquiryId()));
        return r;
    }

    @Override
    public EndInquiryResult endInquiry(EndInquiryParam p) {
        StartInquiryResult inquiryResult = inquiryRepository.queryInquiry(p.getInquiryId());
        if (inquiryResult == null) {
            throw new InvalidException("问诊{}不存在", p.getInquiryId());
        }

        Doctor doctor = inquiryResult.getDoctor();
        Patient patient = inquiryResult.getPatient();
        log.info("医生{}结束问诊患者{}", doctor.getName(), patient.getName());
        EndInquiryResult r = new EndInquiryResult();
        r.setInquiryId(inquiryResult.getInquiryId());
        r.setDoctor(inquiryResult.getDoctor());
        r.setPatient(inquiryResult.getPatient());
        r.setPrescriptions(prescriptionRepository.queryPrescriptions(inquiryResult.getInquiryId()));
        return r;
    }
}
