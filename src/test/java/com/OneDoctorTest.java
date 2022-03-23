package com;

import com.cowell.core.DefaultDispatcher;
import com.cowell.core.SelectStrategy;
import com.cowell.service.biz.DefaultHandler;
import com.cowell.service.biz.Patient;
import com.cowell.service.keepalive.NonKeepaliveManager;
import com.cowell.service.lock.LocalLock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.core.Arrays;
import org.rx.core.NQuery;

public class OneDoctorTest {
    //业务 1患者 对 1医生
    @SneakyThrows
    @Test
    public synchronized void start() {
        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(
                new NonKeepaliveManager(),
                Util.queue, Util.group, SelectStrategy.DEFAULT.getSelector(),
                new DefaultHandler<>(new LocalLock()));
//        dispatcher.setMaxAcceptMillis(1000);
//        dispatcher.setSwitchAsyncThreshold(2000);
//        dispatcher.setMaxDispatchMillis(5000);

        dispatcher.getGroup().add(Util.doctors.get(0));
        Util.offerPatients(dispatcher.getQueue());

        assert NQuery.of(Util.doctors.get(0).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 0;
        assert NQuery.of(Util.doctors.get(3).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 1;

        dispatcher.startAsync();
        wait();
    }
}
