package com;

import com.cowell.core.DefaultDispatcher;
import com.cowell.core.Dispatcher;
import com.cowell.core.SelectStrategy;
import com.cowell.service.biz.Patient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.core.Arrays;
import org.rx.core.NQuery;

public class OneDoctorTest {
    //业务 1患者 对 1医生
    @SneakyThrows
    @Test
    public synchronized void start() {
        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(Util.queue, Util.queue.getKind().newStore(), SelectStrategy.DEFAULT.getSelector());
        dispatcher.setMaxWaitAcceptMillis(1000);
        dispatcher.setSwitchAsyncThreshold(2000);
        dispatcher.setMaxSelectMillis(5000);
        dispatcher.getStore().add(Util.doctors.get(0));
        Util.offerPatients(dispatcher.getQueue());

        assert NQuery.of(Util.doctors.get(0).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 0;
        assert NQuery.of(Util.doctors.get(3).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 1;

        dispatcher.startAsync();
        wait();
    }
}
