package org.rx.demo.gjprescription.service.flow.conf;

import lombok.Data;

import java.util.Map;

@Data
public class ActionEvent {
    public enum EventType {
        /**
         * 在动作之前触发
         */
        PRE,
        /**
         * 在动作之后触发
         */
        POST
    }

    /**
     * 排序
     */
    int ordinal;
    /**
     * 事件类型
     */
    EventType type;
    /**
     * true异步事件；false同步事件，同步事件中任一一个订阅者可中断当前执行
     */
    boolean async;
    /**
     * 事件DTO的Type，支持嵌套泛型
     */
    String beanType;
    /**
     * 组成事件DTO
     */
    Map<String, String> beanBuilder;
}
