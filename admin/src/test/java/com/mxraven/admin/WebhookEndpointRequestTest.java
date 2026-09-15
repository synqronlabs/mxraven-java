package com.mxraven.admin;

import com.mxraven.admin.model.CreateWebhookEndpointRequest;
import com.mxraven.admin.model.UpdateWebhookEndpointRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebhookEndpointRequestTest {
    @Test
    void createRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateWebhookEndpointRequest.builder()
                .displayName("Hooks")
                .targetUrl("https://example.com/hook")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateWebhookEndpointRequest.builder()
                .webhookRef("_bad")
                .displayName("Hooks")
                .targetUrl("https://example.com/hook")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateWebhookEndpointRequest.builder()
                .webhookRef("hooks")
                .displayName("Hooks")
                .build());
    }

    @Test
    void createValidatesTargetUrl() {
        assertThrows(IllegalArgumentException.class, () -> CreateWebhookEndpointRequest.builder()
                .webhookRef("hooks")
                .displayName("Hooks")
                .targetUrl("http://example.com/hook")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateWebhookEndpointRequest.builder()
                .webhookRef("hooks")
                .displayName("Hooks")
                .targetUrl("https://")
                .build());
        assertDoesNotThrow(() -> CreateWebhookEndpointRequest.builder()
                .webhookRef("hooks")
                .displayName("Hooks")
                .targetUrl("https://example.com/hook")
                .build());
    }

    @Test
    void updateRequiresDisplayNameAndTargetUrl() {
        assertThrows(IllegalArgumentException.class, () -> UpdateWebhookEndpointRequest.builder()
                .displayName("Hooks")
                .build());
        assertThrows(IllegalArgumentException.class, () -> UpdateWebhookEndpointRequest.builder()
                .targetUrl("https://example.com/hook")
                .build());
        assertDoesNotThrow(() -> UpdateWebhookEndpointRequest.builder()
                .displayName("Hooks")
                .targetUrl("https://example.com/hook")
                .build());
    }
}
