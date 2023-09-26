package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;

@Data
public class DrugEntity {
    String prescriptionNo;
    String drugId;
    String drugName;
    String drugMsg;
    String drugUsageDosageMsg;
    String drugType;
}
