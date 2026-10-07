package com.mxraven.admin;

import com.mxraven.admin.model.CreateInboundRouteRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateInboundRouteRequestTest {
    private static final String LISTENER_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    void requiresListenerAndDomain() {
        assertThrows(IllegalArgumentException.class, () -> CreateInboundRouteRequest.builder()
                .domainName("example.com").build());
        assertThrows(IllegalArgumentException.class, () -> CreateInboundRouteRequest.builder()
                .mtaListenerId(LISTENER_ID).build());
    }

    @Test
    void requiresAValidListenerUuid() {
        assertThrows(IllegalArgumentException.class, () -> CreateInboundRouteRequest.builder()
                .mtaListenerId("listener-1").domainName("example.com").build());
    }

    @Test
    void enforcesTheSchemaDomainNameLength() {
        assertThrows(IllegalArgumentException.class, () -> CreateInboundRouteRequest.builder()
                .mtaListenerId(LISTENER_ID).domainName("a".repeat(256)).build());
    }

    @Test
    void acceptsAValidBody() {
        assertDoesNotThrow(() -> CreateInboundRouteRequest.builder()
                .mtaListenerId(LISTENER_ID)
                .domainName("example.com")
                .onboardDomainIfMissing(true)
                .build());
    }
}
