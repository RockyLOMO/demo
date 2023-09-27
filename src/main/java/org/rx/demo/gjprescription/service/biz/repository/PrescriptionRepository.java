package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.model.PrescriptionEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface PrescriptionRepository {
    void savePrescription(@NotNull @Valid PrescriptionEntity p);

    List<PrescriptionEntity> queryPrescriptions(@NotNull String inquiryId);
}
