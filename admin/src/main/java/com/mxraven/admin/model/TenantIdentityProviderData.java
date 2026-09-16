package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
import java.util.Map;

/**
 * Wire representation backing the {@link TenantIdentityProvider} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantIdentityProviderData {
    private final String id;
    private final String tenantId;
    private final String idpRef;
    private final String displayName;
    private final IdentityProviderType providerType;
    private final String zitadelProviderId;
    private final String issuer;
    private final String clientId;
    private final List<String> scopes;
    private final Map<String, Object> providerConfig;
    private final boolean autoGrantRoles;
    private final boolean isActive;
    private final IdentityProviderLifecycleStatus lifecycleStatus;

    /** unique provider identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** stable reference for the provider */
    public String idpRef() {
        return idpRef;
    }

    /** human-readable provider name */
    public String displayName() {
        return displayName;
    }

    /** identity provider type */
    public IdentityProviderType providerType() {
        return providerType;
    }

    /** provider identifier in Zitadel */
    public String zitadelProviderId() {
        return zitadelProviderId;
    }

    /** token issuer */
    public String issuer() {
        return issuer;
    }

    /** OAuth or OIDC client identifier */
    public String clientId() {
        return clientId;
    }

    /** requested scopes */
    public List<String> scopes() {
        return scopes;
    }

    /** provider-specific configuration */
    public Map<String, Object> providerConfig() {
        return providerConfig;
    }

    /** whether roles are granted automatically */
    public boolean autoGrantRoles() {
        return autoGrantRoles;
    }

    /** whether the provider is active */
    public boolean isActive() {
        return isActive;
    }

    /** provider lifecycle status */
    public IdentityProviderLifecycleStatus lifecycleStatus() {
        return lifecycleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantIdentityProviderData that = (TenantIdentityProviderData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.idpRef, that.idpRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.providerType, that.providerType)
                && Objects.equals(this.zitadelProviderId, that.zitadelProviderId)
                && Objects.equals(this.issuer, that.issuer)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.scopes, that.scopes)
                && Objects.equals(this.providerConfig, that.providerConfig)
                && this.autoGrantRoles == that.autoGrantRoles
                && this.isActive == that.isActive
                && Objects.equals(this.lifecycleStatus, that.lifecycleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.idpRef, this.displayName, this.providerType, this.zitadelProviderId, this.issuer, this.clientId, this.scopes, this.providerConfig, this.autoGrantRoles, this.isActive, this.lifecycleStatus);
    }

    @Override
    public String toString() {
        return "TenantIdentityProviderData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "idpRef=" + this.idpRef + ", " + "displayName=" + this.displayName + ", " + "providerType=" + this.providerType + ", " + "zitadelProviderId=" + this.zitadelProviderId + ", " + "issuer=" + this.issuer + ", " + "clientId=" + this.clientId + ", " + "scopes=" + this.scopes + ", " + "providerConfig=" + this.providerConfig + ", " + "autoGrantRoles=" + this.autoGrantRoles + ", " + "isActive=" + this.isActive + ", " + "lifecycleStatus=" + this.lifecycleStatus + "]";
    }

    /**
     * Creates a new TenantIdentityProviderData.
     *
     * @param id unique provider identifier
     * @param tenantId identifier of the owning tenant
     * @param idpRef stable reference for the provider
     * @param displayName human-readable provider name
     * @param providerType identity provider type
     * @param zitadelProviderId provider identifier in Zitadel
     * @param issuer token issuer
     * @param clientId OAuth or OIDC client identifier
     * @param scopes requested scopes
     * @param providerConfig provider-specific configuration
     * @param autoGrantRoles whether roles are granted automatically
     * @param isActive whether the provider is active
     * @param lifecycleStatus provider lifecycle status
     */
    @JsonCreator
    public TenantIdentityProviderData(String id, String tenantId, String idpRef, String displayName, IdentityProviderType providerType, String zitadelProviderId, String issuer, String clientId, List<String> scopes, Map<String, Object> providerConfig, boolean autoGrantRoles, boolean isActive, IdentityProviderLifecycleStatus lifecycleStatus) {
        this.id = id;
        this.tenantId = tenantId;
        this.idpRef = idpRef;
        this.displayName = displayName;
        this.providerType = providerType;
        this.zitadelProviderId = zitadelProviderId;
        this.issuer = issuer;
        this.clientId = clientId;
        this.scopes = scopes;
        this.providerConfig = providerConfig;
        this.autoGrantRoles = autoGrantRoles;
        this.isActive = isActive;
        this.lifecycleStatus = lifecycleStatus;
    }
}
