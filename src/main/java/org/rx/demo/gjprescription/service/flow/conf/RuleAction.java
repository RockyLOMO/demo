package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RuleAction {
    /**
     * 排序
     */
    int ordinal;
    /**
     * 动作实现方法全路径名
     */
    @NotNull
    String method;
}
