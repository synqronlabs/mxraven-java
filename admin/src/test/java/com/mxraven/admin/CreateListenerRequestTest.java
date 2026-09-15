package com.mxraven.admin;

import com.mxraven.admin.model.CreateListenerRequest;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.StreamType;
import com.mxraven.admin.model.TerminalActionPayload;
import com.mxraven.admin.model.TerminalActionType;
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
                .defaultTerminalAction(TerminalActionType.DELIVER)
                .build());
    }

    @Test
    void requiresEveryOpenApiRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.MARKETING)
                .defaultTerminalAction(TerminalActionType.DROP)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .streamType(StreamType.MARKETING)
                .defaultTerminalAction(TerminalActionType.DROP)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.MTA)
                .defaultTerminalAction(TerminalActionType.DROP)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.MARKETING)
                .build());
    }

    @Test
    void rejectsSystemStreamAndMisplacedActions() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.MTA)
                .streamType(StreamType.SYSTEM)
                .defaultTerminalAction(TerminalActionType.DROP)
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.SUBMISSION)
                .streamType(StreamType.TRANSACTIONAL)
                .defaultTerminalAction(TerminalActionType.RELAY, TerminalActionPayload.relay("primary"))
                .build());
    }

    @Test
    void rejectsRspamdOnSubmissionListeners() {
        assertThrows(IllegalArgumentException.class, () -> CreateListenerRequest.builder()
                .displayName("Listener")
                .listenerType(ListenerType.SUBMISSION)
                .streamType(StreamType.TRANSACTIONAL)
                .defaultTerminalAction(TerminalActionType.DROP)
                .rspamdScanningEnabled(true)
                .build());
    }
}
