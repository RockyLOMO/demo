package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;
import org.rx.demo.gjprescription.service.flow.ActionInterceptor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FlowStage {
    /**
     * 流程阶段id
     */
    int id;
    /**
     * 流程阶段名
     */
    String name;
    /**
     * 阶段范围
     */
    List<String> scopes;
    /**
     * 动作拦截器
     */
    List<Class<? extends ActionInterceptor>> interceptors;
    /**
     * 实现类的package
     */
    String basePackage;
    /**
     * 具体规则
     */
    @NotNull
    @Valid
    List<RuleAction> rules;
}
