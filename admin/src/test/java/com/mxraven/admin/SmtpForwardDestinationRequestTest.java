package com.mxraven.admin;

import com.mxraven.admin.model.ConfirmSmtpForwardDestinationRequest;
import com.mxraven.admin.model.CreateSmtpForwardDestinationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmtpForwardDestinationRequestTest {
    @Test
    void createRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpForwardDestinationRequest.builder()
                .displayName("smtp")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpForwardDestinationRequest.builder()
                .destinationRef("_bad")
                .displayName("smtp")
                .emailAddress("forward@example.com")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpForwardDestinationRequest.builder()
                .destinationRef("forwarding")
                .displayName("smtp")
                .emailAddress("not-an-address")
                .build());
    }

    @Test
    void createAcceptsAValidBody() {
        assertDoesNotThrow(() -> CreateSmtpForwardDestinationRequest.builder()
                .destinationRef("forwarding")
                .displayName("smtp")
                .emailAddress("forward@example.com")
                .build());
    }

    @Test
    void confirmEnforcesTokenLength() {
        assertThrows(IllegalArgumentException.class, () -> ConfirmSmtpForwardDestinationRequest.builder()
                .token("too-short")
                .build());
        assertThrows(IllegalArgumentException.class, () -> ConfirmSmtpForwardDestinationRequest.builder()
                .token("x".repeat(129))
                .build());
        assertDoesNotThrow(() -> ConfirmSmtpForwardDestinationRequest.builder()
                .token("x".repeat(40))
                .build());
    }
}
