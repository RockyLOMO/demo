package com.cowell.core;

import com.cowell.service.keepalive.LocalKeepaliveManager;
import io.netty.util.concurrent.FastThreadLocal;
import lombok.Getter;
import org.rx.exception.InvalidException;

@Getter
public final class DispatchContext {
    static final FastThreadLocal<DispatchContext> TL = new FastThreadLocal<>();

    public static DispatchContext current() {
        DispatchContext ctx = TL.getIfExists();
        if (ctx == null) {
            throw new InvalidException("No context");
        }
        return ctx;
    }

    public static <T extends QueueElement> DispatchContext set(Dispatcher<T> dispatcher) {
        DispatchContext ctx = TL.getIfExists();
        if (ctx == null) {
            TL.set(ctx = new DispatchContext());
        }
        ctx.dispatcher = dispatcher;
        return ctx;
    }

    Dispatcher dispatcher;

    public <T extends QueueElement> Dispatcher<T> getDispatcher() {
        return dispatcher;
    }

//    public <T> void setEntityType(Class<T> entityType) {
//        KeepaliveManager manager = dispatcher.getKeepaliveManager();
//        if (manager instanceof LocalKeepaliveManager) {
//            LocalKeepaliveManager.ENTITY_TYPE.set(entityType);
//        }
//    }
}
