package org.rx.demo.gjprescription.service.biz.user.impl;

import lombok.Data;
import org.rx.demo.gjprescription.service.biz.user.Doctor;

@Data
public class HyDoctor implements Doctor {
    String id;
    String name;
}
