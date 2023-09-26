package org.rx.demo.gjprescription.service.biz.action.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rx.demo.gjprescription.service.biz.action.DoctorAction;
import org.rx.demo.gjprescription.service.biz.action.dto.*;
import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;
import org.rx.demo.gjprescription.service.biz.repository.DoctorRepository;
import org.rx.demo.gjprescription.service.biz.repository.PatientRepository;
import org.rx.demo.gjprescription.service.biz.user.Doctor;
import org.rx.demo.gjprescription.service.biz.user.Patient;
import org.rx.util.Snowflake;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class HyDoctorAction implements DoctorAction {
    final DoctorRepository doctorRepository;
    final PatientRepository patientRepository;
    //模拟数据库
    final Map<String, StartInquiryResult> di = new ConcurrentHashMap<>();
    final Map<String, List<PrescriptionEntity>> dp = new ConcurrentHashMap<>();

    @Override
    public StartInquiryResult startInquiry(StartInquiryParam p) {
        Doctor doctor = doctorRepository.queryDoctor(p.getDoctorId());
        Patient patient = patientRepository.queryPatient(p.getPatientId());
        log.info("医生{}开始问诊患者{}", doctor.getName(), patient.getName());
        StartInquiryResult r = new StartInquiryResult();
        r.setDoctor(doctor);
        r.setPatient(patient);
        r.setInquiryId(String.valueOf(Snowflake.DEFAULT.nextId()));
        di.put(r.getInquiryId(), r);
        return r;
    }

    @Override
    public MakePrescriptionResult makePrescription(MakePrescriptionParam p) {
        StartInquiryResult inquiryResult = di.get(p.getInquiryId());
        Doctor doctor = inquiryResult.getDoctor();
        Patient patient = inquiryResult.getPatient();
        log.info("医生{}给患者{}开具处方{}", doctor.getName(), patient.getName(), p.getPrescription());
        List<PrescriptionEntity> prescriptionEntities = dp.computeIfAbsent(inquiryResult.getInquiryId(), k -> new Vector<>());
        prescriptionEntities.add(p.getPrescription());
        MakePrescriptionResult r = new MakePrescriptionResult();
        r.setInquiryId(inquiryResult.getInquiryId());
        r.setDoctor(inquiryResult.getDoctor());
        r.setPatient(inquiryResult.getPatient());
        r.setPrescriptions(Collections.unmodifiableList(prescriptionEntities));
        return r;
    }

    @Override
    public EndInquiryResult endInquiry(EndInquiryParam p) {
        StartInquiryResult inquiryResult = di.get(p.getInquiryId());
        Doctor doctor = inquiryResult.getDoctor();
        Patient patient = inquiryResult.getPatient();
        log.info("医生{}结束问诊患者{}", doctor.getName(), patient.getName());
        EndInquiryResult r = new EndInquiryResult();
        r.setInquiryId(inquiryResult.getInquiryId());
        r.setDoctor(inquiryResult.getDoctor());
        r.setPatient(inquiryResult.getPatient());
        r.setPrescriptions(Collections.unmodifiableList(dp.getOrDefault(inquiryResult.getInquiryId(), Collections.emptyList())));
        return r;
    }
}
