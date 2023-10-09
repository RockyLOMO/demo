package org.rx.demo.gjprescription.service.flow;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rx.exception.InvalidException;

import java.lang.reflect.Method;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ActionJoinPoint {
    /**
     * 实例
     */
    final Object target;
    /**
     * 动作DTO
     */
    final Object dto;
    /**
     * 动作方法
     */
    final Method signature;

    public <T> T proceed() {
        return proceed(dto);
    }

    /**
     * 执行动作
     *
     * @param dto
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T proceed(@NonNull Object dto) {
        if (!this.dto.getClass().isInstance(dto)) {
            throw new InvalidException("入参类型{}与原类型{}不匹配", dto.getClass(), this.dto.getClass());
        }
        return (T) signature.invoke(target, new Object[]{dto});
    }
}
