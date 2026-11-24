package com.mxraven.admin;

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
                .displayName("a".repeat(256)).build());
    }

    @Test
    void acceptsValidBodies() {
        assertDoesNotThrow(() -> UpdateListenerRequest.builder().displayName("Primary").build());
    }
}
