package com.mxraven.admin;

import com.mxraven.admin.model.UpdateInboundRouteRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateInboundRouteRequestTest {
    private static final String LISTENER_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    void requiresListenerAndDomain() {
        assertThrows(IllegalArgumentException.class, () -> UpdateInboundRouteRequest.builder()
                .domainName("example.com").build());
        assertThrows(IllegalArgumentException.class, () -> UpdateInboundRouteRequest.builder()
                .mtaListenerId(LISTENER_ID).build());
        assertThrows(IllegalArgumentException.class,
                () -> new UpdateInboundRouteRequest("listener-1", "example.com"));
    }

    @Test
    void acceptsAValidBody() {
        assertDoesNotThrow(() -> UpdateInboundRouteRequest.builder()
                .mtaListenerId(LISTENER_ID)
                .domainName("example.com")
                .build());
    }
}
