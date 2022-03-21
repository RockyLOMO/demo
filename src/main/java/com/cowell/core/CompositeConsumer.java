//package com.cowell.core;
//
//import lombok.RequiredArgsConstructor;
//import org.rx.core.Tasks;
//
//@RequiredArgsConstructor
//public class CompositeConsumer<T extends QueueElement> extends AbstractConsumer<T> {
//    final ConsumerStore<T> consumers;
//
//    @Override
//    public boolean isValid() {
//        return consumers.size() > 0;
//    }
//
////    @Override
////    public int matchTags(List<Tag> tags) {
////        return NQuery.of(consumers, true).max(p -> p.matchTags(tags));
////    }
//
//    @Override
//    public boolean accept(T element) {
//        return consumers.select(element) != null;
//    }
//
//    @Override
//    public void consume(T element) {
//        Tasks.run(() -> {
//            Consumer<T> consumer = consumers.select(element);
//            if (consumer == null || !consumer.isValid()) {
//                element.onDiscard(DiscardReason.CONSUMER_INVALID);
//                return;
//            }
//            consumer.consume(element);
//            element.onConsume(consumer);
//        });
//    }
//}
