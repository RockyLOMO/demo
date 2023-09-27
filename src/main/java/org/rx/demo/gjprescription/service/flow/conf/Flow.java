package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Flow {
    int id;
    String name;
    String basePackage;
    @NotNull
    @Valid
    List<FlowRule> rules;
}
