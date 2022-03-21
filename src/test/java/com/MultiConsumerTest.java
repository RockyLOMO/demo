package com;

import com.cowell.service.biz.Doctor;
import com.cowell.service.biz.Patient;
import com.cowell.core.DefaultDispatcher;
import com.cowell.core.Processor;
import com.cowell.core.SelectStrategy;
import com.cowell.service.consumer.MemoryStore;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.core.Constants;

public class MultiConsumerTest {
    //业务场景 1患者 对 多个医生叫号, Tag匹配优先
    @SneakyThrows
    @Test
    public synchronized void start() {
        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(new MemoryStore<>(), SelectStrategy.DEFAULT.getSelector());
        dispatcher.setMaxWaitInvalidMillis(Constants.TIMEOUT_INFINITE);
        Processor<Patient> processor = new Processor<>(Util.queue, dispatcher);

        Util.offerPatients(processor.getQueue());
        for (Doctor doctor : Util.doctors) {
            dispatcher.getStore().add(doctor);
        }

        processor.startAsync();
        wait();
    }
}
