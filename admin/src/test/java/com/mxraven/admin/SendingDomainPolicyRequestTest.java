package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;
import com.mxraven.admin.model.ReplaceSendingDomainPolicyRequest;
import com.mxraven.admin.model.SendingDomainPolicyGrantRequest;
import com.mxraven.admin.model.SendingDomainSubdomainScope;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SendingDomainPolicyRequestTest {
    private static final String DOMAIN_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    void requiresAGrantList() {
        assertThrows(IllegalArgumentException.class, () -> ReplaceSendingDomainPolicyRequest.builder().build());
    }

    @Test
    void rejectsNullGrantEntries() {
        List<SendingDomainPolicyGrantRequest> grants = new ArrayList<SendingDomainPolicyGrantRequest>();
        grants.add(null);
        assertThrows(IllegalArgumentException.class, () -> ReplaceSendingDomainPolicyRequest.builder()
                .grants(grants).build());
    }

    @Test
    void grantRequiresDomainAndScope() {
        assertThrows(IllegalArgumentException.class, () -> SendingDomainPolicyGrantRequest.builder()
                .subdomainScope(SendingDomainSubdomainScope.EXACT).build());
        assertThrows(IllegalArgumentException.class, () -> SendingDomainPolicyGrantRequest.builder()
                .domainId(DOMAIN_ID).build());
        assertThrows(IllegalArgumentException.class, () -> SendingDomainPolicyGrantRequest.builder()
                .domainId("domain-1").subdomainScope(SendingDomainSubdomainScope.EXACT).build());
    }

    @Test
    void acceptsAnEmptyGrantListToClearThePolicy() {
        assertDoesNotThrow(() -> ReplaceSendingDomainPolicyRequest.builder().grants(Java8.<SendingDomainPolicyGrantRequest>list()).build());
    }

    @Test
    void acceptsAValidGrant() {
        assertDoesNotThrow(() -> ReplaceSendingDomainPolicyRequest.builder()
                .grants(Java8.list(SendingDomainPolicyGrantRequest.builder()
                        .domainId(DOMAIN_ID)
                        .subdomainScope(SendingDomainSubdomainScope.INCLUDE_SUBDOMAINS)
                        .build()))
                .build());
    }
}
