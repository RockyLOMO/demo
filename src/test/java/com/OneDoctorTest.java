package com;

import com.cowell.core.DefaultDispatcher;
import com.cowell.core.SelectStrategy;
import com.cowell.service.biz.DefaultHandler;
import com.cowell.service.biz.Doctor;
import com.cowell.service.biz.Patient;
import com.cowell.service.consumer.ConsumerEntity;
import com.cowell.service.keepalive.LocalKeepaliveManager;
import com.cowell.service.lock.LocalLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.rx.core.Arrays;
import org.rx.core.NQuery;

@Slf4j
public class OneDoctorTest {
    //业务 1患者 对 1医生
    @SneakyThrows
    @Test
    public synchronized void start() {
        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(
                new LocalKeepaliveManager(),
                Util.queue, Util.group, SelectStrategy.DEFAULT.getSelector(),
                new DefaultHandler<>(new LocalLock()));
        dispatcher.onDiscard.combine((s, e) -> {
            log.info("Discard {} {}", e.getReason(), e.getElement());
        });
//        dispatcher.setMaxAcceptMillis(1000);
//        dispatcher.setSwitchAsyncThreshold(2000);
//        dispatcher.setMaxDispatchMillis(5000);

        Doctor doctor = Util.doctors.get(0);
        dispatcher.getGroup().add(doctor);
        dispatcher.getKeepaliveManager().receiveAck(ConsumerEntity.class, Util.queue.computeId(doctor.getId()), Integer.MAX_VALUE);
        Util.offerPatients(dispatcher.getQueue());

        assert NQuery.of(Util.doctors.get(0).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 0;
        assert NQuery.of(Util.doctors.get(3).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 1;

        dispatcher.startAsync();
        wait();
    }
}
