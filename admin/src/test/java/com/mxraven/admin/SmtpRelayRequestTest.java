package com.mxraven.admin;

import com.mxraven.admin.model.CreateSmtpRelayRequest;
import com.mxraven.admin.model.UpdateSmtpRelayRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SmtpRelayRequestTest {
    @Test
    void createRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpRelayRequest.builder()
                .displayName("Hello")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpRelayRequest.builder()
                .relayRef("_bad")
                .displayName("Hello")
                .host("smtp.example.com")
                .port(587)
                .username("user")
                .password("secret")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateSmtpRelayRequest.builder()
                .relayRef("primary")
                .displayName("Hello")
                .host("smtp.example.com")
                .port(70000)
                .username("user")
                .password("secret")
                .build());
    }

    @Test
    void createAcceptsAValidBody() {
        assertDoesNotThrow(() -> CreateSmtpRelayRequest.builder()
                .relayRef("primary")
                .displayName("Hello")
                .host("smtp.example.com")
                .port(587)
                .username("user")
                .password("secret")
                .build());
    }

    @Test
    void updateRequiresConnectionDetails() {
        assertThrows(IllegalArgumentException.class, () -> UpdateSmtpRelayRequest.builder()
                .displayName("Hello")
                .build());
        assertDoesNotThrow(() -> UpdateSmtpRelayRequest.builder()
                .displayName("Hello")
                .host("smtp.example.com")
                .port(587)
                .username("user")
                .password("secret")
                .build());
    }
}
