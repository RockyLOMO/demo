//package com;
//
//import com.cowell.service.biz.Doctor;
//import com.cowell.service.biz.Patient;
//import com.cowell.core.DefaultDispatcher;
//import com.cowell.core.SelectStrategy;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.rx.core.Constants;
//
//public class MultiConsumerTest {
//    //业务场景 1患者 对 多个医生叫号, Tag匹配优先
//    @SneakyThrows
//    @Test
//    public synchronized void start() {
//        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(Util.queue, Util.queue.getKind().newStore(), SelectStrategy.DEFAULT.getSelector());
//        dispatcher.setMaxAcceptMillis(Constants.TIMEOUT_INFINITE);
//
//        for (Doctor doctor : Util.doctors) {
//            dispatcher.getGroup().add(doctor);
//        }
//        Util.offerPatients(dispatcher.getQueue());
//
//        dispatcher.startAsync();
//        wait();
//    }
//}
