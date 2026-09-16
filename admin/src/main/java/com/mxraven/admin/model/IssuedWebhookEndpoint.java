package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A webhook endpoint returned by creation or secret rotation, including the
 * one-time {@code signingSecret}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class IssuedWebhookEndpoint {
    private final String id;
    private final String tenantId;
    private final String webhookRef;
    private final String displayName;
    private final String targetUrl;
    private final String signingKid;
    private final boolean hasSigningSecret;
    private final boolean isActive;
    private final String createdAt;
    private final String updatedAt;
    private final String signingSecret;

    /** unique webhook endpoint identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference used when delivering events */
    public String webhookRef() {
        return webhookRef;
    }

    /** human-readable endpoint name */
    public String displayName() {
        return displayName;
    }

    /** URL webhooks are delivered to */
    public String targetUrl() {
        return targetUrl;
    }

    /** identifier of the active signing key */
    public String signingKid() {
        return signingKid;
    }

    /** whether a signing secret is configured */
    public boolean hasSigningSecret() {
        return hasSigningSecret;
    }

    /** whether the endpoint is active */
    public boolean isActive() {
        return isActive;
    }

    /** creation timestamp */
    public String createdAt() {
        return createdAt;
    }

    /** timestamp of the last update */
    public String updatedAt() {
        return updatedAt;
    }

    /** one-time plaintext signing secret, returned only on creation or rotation */
    public String signingSecret() {
        return signingSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuedWebhookEndpoint that = (IssuedWebhookEndpoint) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.webhookRef, that.webhookRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.targetUrl, that.targetUrl)
                && Objects.equals(this.signingKid, that.signingKid)
                && this.hasSigningSecret == that.hasSigningSecret
                && this.isActive == that.isActive
                && Objects.equals(this.createdAt, that.createdAt)
                && Objects.equals(this.updatedAt, that.updatedAt)
                && Objects.equals(this.signingSecret, that.signingSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.webhookRef, this.displayName, this.targetUrl, this.signingKid, this.hasSigningSecret, this.isActive, this.createdAt, this.updatedAt, this.signingSecret);
    }

    @Override
    public String toString() {
        return "IssuedWebhookEndpoint[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "webhookRef=" + this.webhookRef + ", " + "displayName=" + this.displayName + ", " + "targetUrl=" + this.targetUrl + ", " + "signingKid=" + this.signingKid + ", " + "hasSigningSecret=" + this.hasSigningSecret + ", " + "isActive=" + this.isActive + ", " + "createdAt=" + this.createdAt + ", " + "updatedAt=" + this.updatedAt + ", " + "signingSecret=" + this.signingSecret + "]";
    }

    /**
     * Creates a new IssuedWebhookEndpoint.
     *
     * @param id unique webhook endpoint identifier
     * @param tenantId identifier of the owning tenant
     * @param webhookRef stable reference used when delivering events
     * @param displayName human-readable endpoint name
     * @param targetUrl URL webhooks are delivered to
     * @param signingKid identifier of the active signing key
     * @param hasSigningSecret whether a signing secret is configured
     * @param isActive whether the endpoint is active
     * @param createdAt creation timestamp
     * @param updatedAt timestamp of the last update
     * @param signingSecret one-time plaintext signing secret, returned only on creation or rotation
     */
    @JsonCreator
    public IssuedWebhookEndpoint(String id, String tenantId, String webhookRef, String displayName, String targetUrl, String signingKid, boolean hasSigningSecret, boolean isActive, String createdAt, String updatedAt, String signingSecret) {
        this.id = id;
        this.tenantId = tenantId;
        this.webhookRef = webhookRef;
        this.displayName = displayName;
        this.targetUrl = targetUrl;
        this.signingKid = signingKid;
        this.hasSigningSecret = hasSigningSecret;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.signingSecret = signingSecret;
    }
}
