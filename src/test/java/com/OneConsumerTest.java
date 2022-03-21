package com;

import com.cowell.service.biz.Patient;
import com.cowell.core.Processor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.rx.core.Arrays;

public class OneConsumerTest {
    @SneakyThrows
    @Test
    public synchronized void start() {
        Processor<Patient> processor = new Processor<>(Util.queue, Util.doctors.get(0));
        Util.offerPatients(processor.getQueue());

        assert Util.doctors.get(0).matchTags(Arrays.toList(Util.tags[2])) == 0;
        assert Util.doctors.get(3).matchTags(Arrays.toList(Util.tags[2])) == 1;

        processor.startAsync();
        wait();
    }
}
