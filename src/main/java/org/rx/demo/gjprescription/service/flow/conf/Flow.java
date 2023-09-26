package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

@Data
public class Flow {
    int id;
    String name;
    String basePackage;
    FlowRule rule;
}
