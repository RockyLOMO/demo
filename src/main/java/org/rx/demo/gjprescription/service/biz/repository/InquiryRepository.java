package org.rx.demo.gjprescription.service.biz.repository;

import org.rx.demo.gjprescription.service.biz.action.dto.StartInquiryResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface InquiryRepository {
    void saveInquiry(@NotNull @Valid StartInquiryResult p);

    StartInquiryResult queryInquiry(@NotNull String inquiryId);
}
