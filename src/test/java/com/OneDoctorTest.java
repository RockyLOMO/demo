package com;

import com.cowell.core.DefaultDispatcher;
import com.cowell.core.SelectStrategy;
import com.cowell.service.biz.DefaultHandler;
import com.cowell.service.biz.Doctor;
import com.cowell.service.biz.Patient;
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
        DefaultHandler<Patient> handler = new DefaultHandler<>(Util.group, new LocalLock());
        DefaultDispatcher<Patient> dispatcher = new DefaultDispatcher<>(
                Util.kaMgr, Util.queue, Util.group, SelectStrategy.DEFAULT.getSelector(),
                handler);
        dispatcher.onDiscard.combine((s, e) -> {
            log.info("Discard {} {}", e.getReason(), e.getElement());
        });
        //当队列里的元素taken时，如果元素不可用 同步等待恢复可用最大时间
        dispatcher.setMaxWaitInvalidMillis(1000);
        //当元素恢复可用时 是否放置于队列第一位
        dispatcher.setPutFirstOnReValid(true);
        //异步检测元素最大时间，超过后元素会被丢弃 触发onDiscard事件
        dispatcher.setMaxCheckValidMillis(10 * 1000);
        //续命TTL
        dispatcher.setRenewTtl(Integer.MAX_VALUE);
        //最大同步select时间，超过后 变为异步select
        dispatcher.setSwitchAsyncThreshold(1000);
        //如果医生繁忙 最大等待时间
        dispatcher.setMaxAcceptMillis(1000);
        //超时时间，超过后元素被丢弃
        dispatcher.setMaxDispatchMillis(5000);

        Doctor doctor = Util.doctors.get(0);
        dispatcher.getGroup().add(doctor);

//        Util.offerPatients(dispatcher.getQueue(), null); //测试患者不可用
        Util.offerPatients(dispatcher.getQueue(), dispatcher); //测试医生不可用
//        handler.suspend(doctor.getId());  //医生暂停接诊
        dispatcher.renewConsumerTtl(doctor.getId());

        assert NQuery.of(Util.doctors.get(0).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 0;
        assert NQuery.of(Util.doctors.get(3).getTags()).intersection(Arrays.toList(Util.tags[2])).count() == 1;

        dispatcher.startAsync();
        wait();
    }
}
