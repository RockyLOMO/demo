package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Flow {
    /**
     * 流程id
     */
    int id;
    /**
     * 流程名
     */
    String name;
    /**
     * 路由规则
     */
    String routeExpr;
    /**
     * 实现类的package
     */
    String basePackage;
    /**
     * 具体阶段
     */
    @NotNull
    @Valid
    List<FlowStage> stages;
}
