package com.mxraven.admin;

import com.mxraven.admin.internal.Java8;

import com.mxraven.admin.model.CreateTenantIdentityProviderRequest;
import com.mxraven.admin.model.CreateTenantOAuthIdentityProviderRequest;
import com.mxraven.admin.model.IdentityProviderType;
import com.mxraven.admin.model.UpdateIdentityProvisioningRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentityProviderRequestTest {
    @Test
    void providerTypesMapToWireValues() {
        assertEquals("oidc", IdentityProviderType.OIDC.wire());
        assertEquals("azure_ad", IdentityProviderType.AZURE_AD.wire());
        assertEquals("github_enterprise_server", IdentityProviderType.GITHUB_ENTERPRISE_SERVER.wire());
        assertEquals("gitlab_self_hosted", IdentityProviderType.GITLAB_SELF_HOSTED.wire());
        assertEquals(IdentityProviderType.APPLE, IdentityProviderType.fromWire("apple"));
    }

    @Test
    void oidcCreateRequiresSchemaRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> CreateTenantIdentityProviderRequest.builder()
                .idpRef("corp")
                .displayName("Corp")
                .issuer("https://issuer.example.com")
                .clientId("client")
                .build());
        assertDoesNotThrow(() -> CreateTenantIdentityProviderRequest.builder()
                .idpRef("corp")
                .displayName("Corp")
                .issuer("https://issuer.example.com")
                .clientId("client")
                .clientSecret("secret")
                .build());
    }

    @Test
    void oauthCreateRequiresEndpoints() {
        assertThrows(IllegalArgumentException.class, () -> CreateTenantOAuthIdentityProviderRequest.builder()
                .idpRef("corp")
                .name("Corp OAuth")
                .clientId("client")
                .clientSecret("secret")
                .authorizationEndpoint("https://idp.example.com/authorize")
                .build());
        assertDoesNotThrow(() -> CreateTenantOAuthIdentityProviderRequest.builder()
                .idpRef("corp")
                .name("Corp OAuth")
                .clientId("client")
                .clientSecret("secret")
                .authorizationEndpoint("https://idp.example.com/authorize")
                .tokenEndpoint("https://idp.example.com/token")
                .userEndpoint("https://idp.example.com/userinfo")
                .build());
    }

    @Test
    void provisioningRolesAllowEmptyAndAreBounded() {
        assertDoesNotThrow(() -> UpdateIdentityProvisioningRequest.builder()
                .roles(Java8.list())
                .build());
        assertDoesNotThrow(() -> UpdateIdentityProvisioningRequest.builder().build());
        assertThrows(IllegalArgumentException.class, () -> UpdateIdentityProvisioningRequest.builder()
                .roles(IntStream.range(0, 65).mapToObj(i -> "role" + i).collect(java.util.stream.Collectors.toList()))
                .build());
        assertDoesNotThrow(() -> UpdateIdentityProvisioningRequest.builder()
                .roles(Java8.list("admin"))
                .build());
    }
}
