package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RuleAction {
    int ordinal;
    @NotNull
    String method;
}
