package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import java.util.List;

@Data
public class FlowRule {
    int id;
    List<RuleAction> actions;
}
