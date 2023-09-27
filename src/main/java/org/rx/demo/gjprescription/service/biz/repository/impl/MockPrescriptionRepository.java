package org.rx.demo.gjprescription.service.biz.repository.impl;

import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;
import org.rx.demo.gjprescription.service.biz.repository.PrescriptionRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@EnableTrace(doValidate = true)
@Component
public class MockPrescriptionRepository implements PrescriptionRepository {
    final Map<String, List<PrescriptionEntity>> d = new ConcurrentHashMap<>();

    @Override
    public void savePrescription(PrescriptionEntity p) {
        d.computeIfAbsent(p.getInquiryId(), k -> new Vector<>()).add(p);
    }

    @Override
    public List<PrescriptionEntity> queryPrescriptions(String inquiryId) {
        return Collections.unmodifiableList(d.getOrDefault(inquiryId, Collections.emptyList()));
    }
}
