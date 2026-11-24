package com.mxraven.mail.notify;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationCallbacksTest {
    @Test
    void dispatchesFaultEvents() throws Exception {
        FaultEvent event = new FaultEvent("t1", "l1", "m1", 1, "rev-1", 2,
                "sender@example.com", "rcpt@example.com", "451 defer",
                new RoutingFault("runtime_error", "boom", 3), null, null, 4);
        AtomicReference<FaultEvent> seen = new AtomicReference<>();

        NotificationCallbacks callbacks = new NotificationCallbacks() {
            @Override
            public HandlerResult onFault(FaultEvent fault) {
                seen.set(fault);
                return HandlerResult.CONTINUE;
            }
        };

        assertEquals(HandlerResult.CONTINUE, callbacks.handle(new FaultNotification(event)));
        assertEquals(event, seen.get());
        assertEquals(0, seen.get().httpCalls().size());
    }
}
