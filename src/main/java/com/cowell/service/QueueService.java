//package com.cowell.service;
//
//import com.cowell.config.AppConfig;
//import com.cowell.core.*;
//import lombok.RequiredArgsConstructor;
//import org.rx.core.Arrays;
//import org.rx.exception.InvalidException;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//import static org.rx.core.Extends.ifNull;
//
////@SuppressWarnings(Constants.NON_RAW_TYPES)
//@Service
//@RequiredArgsConstructor
//public class QueueService {
//    final AppConfig config;
//    final List<Processor<?>> hold = new CopyOnWriteArrayList<>();
//
//    @PostConstruct
//    public void init() {
//        AppConfig.Processor[] processors = config.getProcessor();
//        if (Arrays.isEmpty(processors)) {
//            return;
//        }
//        for (AppConfig.Processor p : processors) {
//            AppConfig.Queue queue = p.getQueue();
//            if (queue == null) {
//                throw new InvalidException("app.processor.queue not specified");
//            }
//            QueueKind kind = queue.getKind();
//            if (kind == null) {
//                throw new InvalidException("app.processor.queue.kind not specified");
//            }
//            AppConfig.Dispatcher dispatcher = p.getDispatcher();
//            if (dispatcher == null) {
//                throw new InvalidException("app.processor.dispatcher not specified");
//            }
//            QueueKind store = dispatcher.getStore();
//            if (store == null) {
//                throw new InvalidException("app.processor.dispatcher.store not specified");
//            }
//            Queue<QueueElement> q = kind.newQueue(queue.getCapacity());
//            q.setName(queue.getName());
//            hold.add(new Processor<>(q, new DefaultDispatcher<>(store.newStore(), ifNull(dispatcher.getSelector(), SelectStrategy.DEFAULT).getSelector())));
//        }
//    }
//}
