package com.mxraven.admin;

import com.mxraven.admin.model.CreateListenerRequest;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.StreamType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateListenerRequestTest {
    @Test
    void acceptsAValidSubmissionListener() {
        assertDoesNotThrow(() -> CreateListenerRequest.builder()
                .displayName("Outbound")
                .listenerType(ListenerType.SUBMISSION)
                .streamType(StreamType.TRANSACTIONAL)
                .build());
    }

    @Test
    void requiresEveryOpenApiRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.MARKETING)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .streamType(StreamType.MARKETING)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.MTA)
                .build());
    }

    @Test
    void rejectsTheSystemStream() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.SYSTEM)
                .build());
    }

    @Test
    void rejectsRspamdOnSubmissionListeners() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.SUBMISSION)
                .streamType(StreamType.TRANSACTIONAL)
                .rspamdScanningEnabled(true)
                .build());
    }
}
