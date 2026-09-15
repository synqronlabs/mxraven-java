package com.mxraven.admin.model;

import java.util.List;

/** Tenant identity-provider provisioning settings. */
public record IdentityProvisioning(
        List<String> roles) {
}
