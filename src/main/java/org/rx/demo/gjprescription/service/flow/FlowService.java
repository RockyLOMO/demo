package org.rx.demo.gjprescription.service.flow;

import com.alibaba.fastjson2.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rx.annotation.EnableTrace;
import org.rx.core.*;
import org.rx.demo.gjprescription.service.flow.conf.Flow;
import org.rx.demo.gjprescription.service.flow.conf.FlowRule;
import org.rx.demo.gjprescription.service.flow.conf.RuleAction;
import org.rx.exception.InvalidException;
import org.rx.spring.SpringContext;
import org.rx.util.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.rx.core.Extends.ifNull;
import static org.rx.core.Extends.quietly;

@EnableTrace(doValidate = true)
@Slf4j
@Service
public class FlowService {
    @RequiredArgsConstructor
    public static class FlowInfo {
        final Flow flow;
        final Map<Type, Method> methods = new ConcurrentHashMap<>();
    }

    volatile Map<Integer, FlowInfo> _flows;

    public <TI, TO> TO proceed(int flowId, @NotNull TI in) {
        return proceed(flowId, in, in.getClass());
    }

    @SneakyThrows
    public <TI, TO> TO proceed(int flowId, @NotNull TI in, @NotNull Type inType) {
        FlowInfo f = _flows.get(flowId);
        if (f == null) {
            throw new InvalidException("流程{}不存在", flowId);
        }
        Method m = f.methods.get(inType);
        if (m == null) {
            throw new InvalidException("未找到与{}匹配的规则动作", inType);
        }
        TO r = (TO) m.invoke(ifNull(SpringContext.getBean(m.getDeclaringClass()), () -> IOC.get(m.getDeclaringClass())), in);
        quietly(() -> EventBus.DEFAULT.publish(r));
        //todo 根据路由规则字段（如问诊id）记录执行节点和后续校验
        return r;
    }

    @PostConstruct
    public synchronized void reload() {
        ConcurrentHashMap<Integer, FlowInfo> t = new ConcurrentHashMap<>(Linq.from(readConfig()).toMap(Flow::getId, FlowInfo::new));
        for (FlowInfo f : t.values()) {
            fillMethods(f.flow, f.methods);
        }
        _flows = t;
    }

    void fillMethods(Flow flow, Map<Type, Method> methods) {
        String s = ".";
        String p = ifNull(flow.getBasePackage(), "");
        if (!p.isEmpty()) {
            p += s;
        }
        for (FlowRule rule : flow.getRules()) {
            for (RuleAction action : rule.getActions()) {
                String cm = action.getMethod();
                int i = cm.lastIndexOf(s);
                if (i == -1) {
                    throw new InvalidException("规则动作{}配置错误", cm);
                }
                String clz = p + cm.substring(0, i);
                String m = cm.substring(i + 1);
                try {
                    Linq<Method> rms = Reflects.getMethodMap(Reflects.getClass(clz)).get(m).where(x -> !x.getDeclaringClass().isInterface());
                    if (!rms.any()) {
                        throw new InvalidException("规则动作{}方法不存在", cm);
                    }
                    if (rms.count() > 1) {
                        throw new InvalidException("规则动作{}不能有重载方法", cm);
                    }
                    Method rm = rms.first();
                    if (rm.getParameterCount() > 1) {
                        throw new InvalidException("规则动作{}只能有1个入参", cm);
                    }
                    Type paramType = rm.getGenericParameterTypes()[0];
                    methods.put(paramType, rm);
                } catch (ClassNotFoundException e) {
                    throw new InvalidException("规则动作{}类不存在", cm);
                }
            }
        }
    }

    public List<Flow> readConfig() {
        List<Flow> flows = YamlConfiguration.RX_CONF.readAs("app.flow", new TypeReference<List<Flow>>() {
        }.getType());
        if (CollectionUtils.isEmpty(flows)) {
            throw new InvalidException("未配置流程");
        }
        Set<Integer> ids = new HashSet<>(), rIds = new HashSet<>();
        for (Flow flow : flows) {
            if (!ids.add(flow.getId())) {
                throw new InvalidException("流程Id{}重复", flow.getId());
            }
            Validator.validateBean(flow);

            rIds.clear();
            for (FlowRule rule : flow.getRules()) {
                if (!rIds.add(rule.getId())) {
                    throw new InvalidException("流程Id{}规则Id{}重复", flow.getId(), rule.getId());
                }
                rule.setActions(Linq.from(rule.getActions()).orderBy(RuleAction::getOrdinal).toList());
            }
        }
        log.info("load conf {}", flows);
        return flows;
    }
}
