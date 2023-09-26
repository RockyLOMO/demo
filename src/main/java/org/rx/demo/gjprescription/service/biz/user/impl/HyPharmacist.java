package org.rx.demo.gjprescription.service.biz.user.impl;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Pharmacist;

@Data
public class HyPharmacist implements Pharmacist {
    String id;
    String name;
}
