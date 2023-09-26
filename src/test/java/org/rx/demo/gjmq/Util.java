package org.rx.demo.gjmq;

import org.rx.demo.gjmq.config.Consts;
import org.rx.demo.gjmq.core.*;
import org.rx.demo.gjmq.service.biz.Doctor;
import org.rx.demo.gjmq.service.consumer.LocalGroup;
import org.rx.demo.gjmq.service.biz.Patient;
import org.rx.demo.gjmq.service.keepalive.LocalKeepaliveManager;
import org.rx.demo.gjmq.service.queue.LocalQueue;
import org.rx.core.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Util {
    static final Queue<Patient> queue = new LocalQueue<>("test-001", Integer.MAX_VALUE);
    static final ConsumerGroup<Patient> group = new LocalGroup<>("test-002");
    static final KeepaliveManager kaMgr = new LocalKeepaliveManager();

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

    static <T extends QueueElement> void offerPatients(Queue<Patient> queue, Dispatcher<T> dispatcher) {
        long passTime = 10 * 2000;
        for (int i = 0; i < 30; i++) {
            Patient patient = new Patient();
            patient.setId(i + 100);
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
            if (dispatcher != null) {
                dispatcher.renewElementTtl(patient.getId());
            }
            queue.offer(patient);
        }
    }
}
