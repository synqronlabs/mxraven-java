package com.mxraven.admin.model;

/** Result of claiming workspace access for an IdP-provisioned user. */
public record IdentityAccessClaim(
        boolean granted) {
}
