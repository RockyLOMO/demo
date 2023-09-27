package org.rx.demo.gjprescription.service.biz.repository.impl;

import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.model.StartInquiryResult;
import org.rx.demo.gjprescription.service.biz.repository.InquiryRepository;
import org.rx.util.Snowflake;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableTrace(doValidate = true)
@Component
public class MockInquiryRepository implements InquiryRepository {
    //模拟数据库
    final Map<String, StartInquiryResult> d = new ConcurrentHashMap<>();

    public void saveInquiry(StartInquiryResult p) {
        if (p.getInquiryId() == null) {
            p.setInquiryId(String.valueOf(Snowflake.DEFAULT.nextId()));
        }
        d.put(p.getInquiryId(), p);
    }

    public StartInquiryResult queryInquiry(String inquiryId) {
        return d.get(inquiryId);
    }
}
