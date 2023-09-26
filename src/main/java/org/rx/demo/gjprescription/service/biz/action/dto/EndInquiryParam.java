package org.rx.demo.gjprescription.service.biz.action.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EndInquiryParam {
    @NotNull
    String inquiryId;
}
