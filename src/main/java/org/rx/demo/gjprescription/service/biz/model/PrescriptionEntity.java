package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class PrescriptionEntity {
    @NotNull
    String doctorId;
    @NotNull
    String patientId;
    @NotNull
    String inquiryId;
    @NotNull
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
