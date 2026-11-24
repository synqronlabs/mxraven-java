package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link WebhookEndpoint} entity.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class WebhookEndpointData {
    private final String id;
    private final String tenantId;
    private final String webhookRef;
    private final String displayName;
    private final String targetUrl;
    private final String signingKid;
    private final boolean hasSigningSecret;
    private final String credentialHeader;
    private final boolean isActive;
    private final String createdAt;
    private final String updatedAt;

    /** unique endpoint identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference for the endpoint */
    public String webhookRef() {
        return webhookRef;
    }

    /** human-readable endpoint name */
    public String displayName() {
        return displayName;
    }

    /** HTTPS URL that routing scripts call with mx.http */
    public String targetUrl() {
        return targetUrl;
    }

    /** key identifier used to sign endpoint calls */
    public String signingKid() {
        return signingKid;
    }

    /** whether a signing secret is stored */
    public boolean hasSigningSecret() {
        return hasSigningSecret;
    }

    /** header carrying the stored credential, when one is set */
    public String credentialHeader() {
        return credentialHeader;
    }

    /** whether the endpoint is active */
    public boolean isActive() {
        return isActive;
    }

    /** timestamp when the endpoint was created */
    public String createdAt() {
        return createdAt;
    }

    public String updatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebhookEndpointData that = (WebhookEndpointData) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.tenantId, that.tenantId) && Objects.equals(this.webhookRef, that.webhookRef) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.targetUrl, that.targetUrl) && Objects.equals(this.signingKid, that.signingKid) && this.hasSigningSecret == that.hasSigningSecret && Objects.equals(this.credentialHeader, that.credentialHeader) && this.isActive == that.isActive && Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.webhookRef, this.displayName, this.targetUrl, this.signingKid, this.hasSigningSecret, this.credentialHeader, this.isActive, this.createdAt, this.updatedAt);
    }

    @Override
    public String toString() {
        return "WebhookEndpointData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "webhookRef=" + this.webhookRef + ", " + "displayName=" + this.displayName + ", " + "targetUrl=" + this.targetUrl + ", " + "signingKid=" + this.signingKid + ", " + "hasSigningSecret=" + this.hasSigningSecret + ", " + "credentialHeader=" + this.credentialHeader + ", " + "isActive=" + this.isActive + ", " + "createdAt=" + this.createdAt + ", " + "updatedAt=" + this.updatedAt + "]";
    }

    /**
     * Creates a new WebhookEndpointData.
     *
     * @param id unique endpoint identifier
     * @param tenantId identifier of the owning tenant
     * @param webhookRef stable reference for the endpoint
     * @param displayName human-readable endpoint name
     * @param targetUrl HTTPS URL that routing scripts call with mx.http
     * @param signingKid key identifier used to sign endpoint calls
     * @param hasSigningSecret whether a signing secret is stored
     * @param credentialHeader header carrying the stored credential, when one is set
     * @param isActive whether the endpoint is active
     * @param createdAt timestamp when the endpoint was created
     * @param updatedAt updatedAt
     */
    @JsonCreator
    public WebhookEndpointData(String id, String tenantId, String webhookRef, String displayName, String targetUrl, String signingKid, boolean hasSigningSecret, String credentialHeader, boolean isActive, String createdAt, String updatedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.webhookRef = webhookRef;
        this.displayName = displayName;
        this.targetUrl = targetUrl;
        this.signingKid = signingKid;
        this.hasSigningSecret = hasSigningSecret;
        this.credentialHeader = credentialHeader;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
