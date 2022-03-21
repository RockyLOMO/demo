//package com;
//
//import com.cowell.service.biz.Doctor;
//import com.cowell.service.biz.Patient;
//import com.cowell.core.*;
//import com.cowell.service.consumer.MemoryStore;
//import com.cowell.service.queue.MemoryQueue;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.rx.core.Arrays;
//import org.rx.core.Constants;
//
//@Slf4j
//public class BizTest {
//    AbstractQueue<Patient> queue = new MemoryQueue<>(Integer.MAX_VALUE);
//    Doctor doctor = new Doctor();
//    Doctor doctor2 = new Doctor();
//    Tag[] tags = new Tag[]{new Tag("1", "主任医生"), new Tag("2", "女医生"), new Tag("3", "10001")};
//    DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(new MemoryStore<>(), SelectStrategy.DEFAULT.getSelector());
//
//    public BizTest() {
//        queue.setMaxCheckValidMillis(10 * 1000);
//
//        doctor.setName("王大夫");
//        doctor.setValid(true);
//        doctor.setAcceptable(true);
//        doctor.getTags().addAll(Arrays.toList(tags[0], tags[2]));
//
//        doctor2.setName("李大夫");
//        doctor2.setValid(true);
//        doctor2.setAcceptable(true);
//        doctor2.getTags().add(tags[1]);
//
//        dispatcher.setMaxWaitInvalidMillis(Constants.TIMEOUT_INFINITE);
//        dispatcher.getStore().add(doctor);
//        dispatcher.getStore().add(doctor2);
//
//        long passTime = 10 * 500;
//        for (int i = 0; i < 50; i++) {
//            Patient patient = new Patient();
//            patient.setId(i + 1);
//            patient.setName("患者" + patient.getId());
//            switch (i) {
//                case 10:
//                    patient.setValid(false);
////                log.info("check revalid {}", patient);
////                Tasks.setTimeout(() -> {
////                    log.info("revalid {}", patient);
////                    patient.setValid(true);
////                }, passTime + 5000);
//                    break;
////                case 15:
////
////                    break;
//                default:
//                    patient.setValid(true);
//                    break;
//            }
//            queue.offer(patient);
//        }
//    }
//
//    @Test
//    public void group() {
//        while (queue.size() > 0) {
//            Patient patient = queue.take();
//            dispatcher.consume(patient);
//        }
//    }
//
//    @Test
//    public void single() {
//        while (queue.size() > 0) {
//            Patient patient = queue.take();
//            doctor.consume(patient);
//        }
//    }
//}
