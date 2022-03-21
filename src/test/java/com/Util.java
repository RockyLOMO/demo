package com;

import com.cowell.service.biz.Doctor;
import com.cowell.service.biz.Patient;
import com.cowell.core.*;
import com.cowell.service.queue.MemoryQueue;
import org.rx.core.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Util {
    static final AbstractQueue<Patient> queue = new MemoryQueue<>(Integer.MAX_VALUE);
    static final List<Doctor> doctors = new ArrayList<>();
    static final Tag[] tags = new Tag[]{new Tag("1", "主任医生"), new Tag("2", "女医生"), new Tag("3", "10001")};

    static {
        queue.setMaxCheckValidMillis(10 * 1000);

        String[] prefix = {"赵", "钱", "孙", "李"};
        for (int i = 0; i < prefix.length; i++) {
            Doctor doctor = new Doctor();
            doctor.setName(prefix[i] + "医生");
            doctor.setValid(true);
            doctor.setAcceptable(true);
            switch (i) {
                case 3:
                    doctor.getTags().addAll(Arrays.toList(tags[0], tags[2]));
                    break;
            }
            doctors.add(doctor);
        }
    }

    static void offerPatients(Queue<Patient> queue) {
        long passTime = 10 * 500;
        for (int i = 0; i < 50; i++) {
            Patient patient = new Patient();
            patient.setId(i + 1);
            patient.setName("患者" + patient.getId());
            switch (i) {
                case 10:
                    patient.setValid(false);
//                log.info("check revalid {}", patient);
//                Tasks.setTimeout(() -> {
//                    log.info("revalid {}", patient);
//                    patient.setValid(true);
//                }, passTime + 5000);
                    break;
                case 15:
                    patient.setValid(true);
                    patient.getTags().add(tags[2]);
                    break;
                default:
                    patient.setValid(true);
                    break;
            }
            queue.offer(patient);
        }
    }
}
