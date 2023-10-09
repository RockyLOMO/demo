package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

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
    /**
     * 事件定义
     */
    List<ActionEvent> events;
}
