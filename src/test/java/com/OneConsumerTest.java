package com;

import com.cowell.biz.Patient;
import com.cowell.core.Processor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class OneConsumerTest {
    final Processor<Patient> processor = new Processor<>(Util.queue, Util.doctors.get(0));

    OneConsumerTest() {
        Util.offerPatients(processor.getQueue());
    }

    @SneakyThrows
    @Test
    public synchronized void start() {
        processor.startAsync();
        wait();
    }
}
