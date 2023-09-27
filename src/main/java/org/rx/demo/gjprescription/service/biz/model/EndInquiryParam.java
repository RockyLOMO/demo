package org.rx.demo.gjprescription.service.biz.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EndInquiryParam {
    @NotNull
    String inquiryId;
}
