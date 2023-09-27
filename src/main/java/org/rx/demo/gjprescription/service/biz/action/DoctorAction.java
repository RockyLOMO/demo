package org.rx.demo.gjprescription.service.biz.action;

import org.rx.annotation.EnableTrace;
import org.rx.demo.gjprescription.service.biz.model.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EnableTrace(doValidate = true)
public interface DoctorAction extends Stage {
    @Override
    default StageType getType() {
        return StageType.INQUIRY;
    }

    @NotNull
    StartInquiryResult startInquiry(@NotNull @Valid StartInquiryParam p);

    @NotNull
    MakePrescriptionResult makePrescription(@NotNull @Valid MakePrescriptionParam p);

    @NotNull
    EndInquiryResult endInquiry(@NotNull @Valid EndInquiryParam p);
}
