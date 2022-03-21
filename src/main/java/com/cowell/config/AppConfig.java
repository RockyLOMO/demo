package com.cowell.config;

import com.cowell.core.QueueKind;
import com.cowell.core.SelectStrategy;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    @Data
    public static class Processor {
        Queue queue = new Queue();
        Dispatcher dispatcher = new Dispatcher();
    }

    @Data
    public static class Queue {
        String name;
        long capacity;
        QueueKind kind;
    }

    @Data
    public static class Dispatcher {
        QueueKind store;
        SelectStrategy selector;
    }

    String[] nameserver;
    Processor[] processor;
}
