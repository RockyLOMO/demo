package org.rx.demo.gjprescription.service.biz.user.impl;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Patient;

@Data
public class HyPatient implements Patient {
    String id;
    String name;
}
