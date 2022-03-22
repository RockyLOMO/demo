package com;

import com.cowell.core.DefaultDispatcher;
import com.cowell.core.Dispatcher;
import com.cowell.core.SelectStrategy;
import com.cowell.service.biz.Patient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.core.Arrays;
import org.rx.core.NQuery;

public class OneConsumerTest {
    //业务 1患者 对 1医生
    @SneakyThrows
    @Test
    public synchronized void start() {
        Dispatcher<Patient> processor = new DefaultDispatcher<>(Util.queue, Util.queue.getKind().newStore(), SelectStrategy.DEFAULT.getSelector());
        processor.getStore().add(Util.doctors.get(0));
        Util.offerPatients(processor.getQueue());

        assert NQuery.of(Util.doctors.get(0).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 0;
        assert NQuery.of(Util.doctors.get(3).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 1;

        processor.startAsync();
        wait();
    }
}
