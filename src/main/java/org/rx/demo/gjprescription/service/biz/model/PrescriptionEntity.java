package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PrescriptionEntity {
    String doctorId;
    String patientId;
    String inquiryId;
    String prescriptionNo;
    Date prescriptionDate;
    String prescriptionType;
    String imageUrl;
    String hospitalId;
    String hospitalName;
    String laboratoryId;
    String laboratoryName;
    String medicalMsg;
    String status;
    String channel;
    List<DiagnosisEntity> diagnoses;
    List<DrugEntity> drugs;
}
