package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;
import com.mxraven.admin.model.TerminalActionType;
import com.mxraven.admin.model.UpdateListenerDefaultTerminalActionRequest;
import com.mxraven.admin.model.UpdateListenerRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateListenerRequestTest {
    @Test
    void requiresADisplayName() {
        assertThrows(IllegalArgumentException.class, () -> UpdateListenerRequest.builder().build());
        assertThrows(IllegalArgumentException.class, () -> new UpdateListenerRequest(null));
        assertThrows(IllegalArgumentException.class, () -> UpdateListenerRequest.builder()
                .displayName(Java8.repeat("a", 256)).build());
    }

    @Test
    void requiresAnActionType() {
        assertThrows(IllegalArgumentException.class,
                () -> UpdateListenerDefaultTerminalActionRequest.builder().build());
        assertThrows(IllegalArgumentException.class,
                () -> new UpdateListenerDefaultTerminalActionRequest(null, null));
    }

    @Test
    void acceptsValidBodies() {
        assertDoesNotThrow(() -> UpdateListenerRequest.builder().displayName("Primary").build());
        assertDoesNotThrow(() -> UpdateListenerDefaultTerminalActionRequest.builder()
                .action(TerminalActionType.DROP).build());
    }
}
