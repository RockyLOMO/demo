package com;

import com.cowell.config.Consts;
import com.cowell.service.biz.Doctor;
import com.cowell.service.consumer.LocalGroup;
import com.cowell.service.keepalive.TcpKeepalive;
import com.cowell.service.biz.Patient;
import com.cowell.core.*;
import com.cowell.service.queue.LocalQueue;
import org.rx.core.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Util {
    static final LocalQueue<Patient> queue = new LocalQueue<>("test-001", Integer.MAX_VALUE);
    static final ConsumerGroup<Patient> group = new LocalGroup<>("test-002");

    static final List<Doctor> doctors = new ArrayList<>();
    static final Tag[] tags = new Tag[]{new Tag("1", "主任医生"), new Tag("2", "女医生"),
            new Tag(Consts.ID_TAG_NAME, "10001")};

    static {
        queue.setName("慢病队列");

        String[] prefix = {"赵", "钱", "孙", "李"};
        for (int i = 0; i < prefix.length; i++) {
            Doctor doctor = new Doctor();
            doctor.setId(i + 1);
            doctor.setSuspend(false);
            doctor.setName(prefix[i] + "医生");
            switch (i) {
                case 3:
                    doctor.setTags(Arrays.toList(tags[0], tags[2]));
                    break;
            }
            doctors.add(doctor);
        }
    }

    static void offerPatients(Queue<Patient> queue) {
        long passTime = 10 * 2000;
        for (int i = 0; i < 40; i++) {
            Patient patient = new Patient();
            patient.setId(i + 10);
            patient.setName("患者" + patient.getId());
            switch (i) {
                case 10:
//                    patient.getKeepalive().setValid(false);
//                log.info("check revalid {}", patient);
//                Tasks.setTimeout(() -> {
//                    log.info("revalid {}", patient);
//                    patient.setValid(true);
//                }, passTime + 5000);
                    break;
                case 15:
                    patient.setPreferTags(Arrays.toList(tags[2]));
                    break;
            }
            queue.offer(patient);
        }
    }
}
