package com.mxraven.admin;

import com.mxraven.admin.model.CreateTenantSuppressionRequest;
import com.mxraven.admin.model.SuppressionReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateTenantSuppressionRequestTest {
    @Test
    void requiresAValidMailbox() {
        assertThrows(IllegalArgumentException.class, () -> CreateTenantSuppressionRequest.builder().build());
        assertThrows(IllegalArgumentException.class, () -> CreateTenantSuppressionRequest.builder()
                .emailAddress("not-a-mailbox").build());
        assertThrows(IllegalArgumentException.class, () -> CreateTenantSuppressionRequest.builder()
                .emailAddress("a".repeat(256) + "@example.com").build());
    }

    @Test
    void acceptsAValidBody() {
        assertDoesNotThrow(() -> CreateTenantSuppressionRequest.builder()
                .emailAddress("user@example.com")
                .reason(SuppressionReason.UNSUBSCRIBE)
                .build());
    }
}
