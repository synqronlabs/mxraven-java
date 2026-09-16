package com.mxraven.admin.model;

import java.util.List;

/**
 * Tenant identity-provider provisioning settings.
 *
 * @param roles roles granted to provisioned identities
 */
public record IdentityProvisioning(
        List<String> roles) {
}
