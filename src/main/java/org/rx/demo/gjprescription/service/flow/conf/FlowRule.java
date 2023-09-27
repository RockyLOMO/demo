package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FlowRule {
    int id;
    @NotNull
    @Valid
    List<RuleAction> actions;
}
