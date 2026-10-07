package com.mxraven.admin;

import com.mxraven.admin.model.CreateDomainRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateDomainRequestTest {
    @Test
    void requiresADomainName() {
        assertThrows(IllegalArgumentException.class, () -> CreateDomainRequest.builder().build());
        assertThrows(IllegalArgumentException.class, () -> CreateDomainRequest.builder()
                .domainName("  ").build());
        assertThrows(IllegalArgumentException.class, () -> new CreateDomainRequest(null));
    }

    @Test
    void enforcesTheSchemaLengths() {
        assertThrows(IllegalArgumentException.class, () -> CreateDomainRequest.builder()
                .domainName("ab").build());
        assertThrows(IllegalArgumentException.class, () -> CreateDomainRequest.builder()
                .domainName("a".repeat(254)).build());
        assertThrows(IllegalArgumentException.class, () -> CreateDomainRequest.builder()
                .domainName("example.com")
                .dmarcReportAddress("a".repeat(321)).build());
    }

    @Test
    void acceptsAValidBody() {
        assertDoesNotThrow(() -> CreateDomainRequest.builder()
                .domainName("example.com")
                .dmarcReportAddress("dmarc@example.com")
                .build());
        assertDoesNotThrow(() -> new CreateDomainRequest("example.com"));
    }
}
