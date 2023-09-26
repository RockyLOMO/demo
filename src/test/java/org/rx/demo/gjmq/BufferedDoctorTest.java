//package com;
//
//import com.cowell.service.biz.BufferedDoctor;
//import com.cowell.service.biz.Patient;
//import com.cowell.core.*;
//import com.cowell.service.keepalive.TcpKeepalive;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//
//@Slf4j
//public class BufferedDoctorTest {
//    @SneakyThrows
//    @Test
//    public synchronized void start() {
//        BufferedDoctor doctor = new BufferedDoctor(1, new TcpKeepalive(), Util.queue.getKind().newStore());
//        doctor.getKeepalive().sendAck();
//
//        Dispatcher<Patient> dispatcher = new DefaultDispatcher<>(Util.queue, Util.queue.getKind().newStore(), SelectStrategy.DEFAULT.getSelector());
//        dispatcher.getGroup().add(doctor);
//        Util.offerPatients(dispatcher.getQueue());
//
//        dispatcher.startAsync();
//        wait();
//    }
//}
