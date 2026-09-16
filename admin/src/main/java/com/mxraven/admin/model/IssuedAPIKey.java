package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * An API key together with its one-time plaintext secret.
 *
 * <p>{@code secret} is returned exactly once by the create operation and cannot
 * be retrieved again. Do not blindly retry an uncertain create response.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class IssuedAPIKey {
    private final String id;
    private final String tenantId;
    private final String listenerId;
    private final String username;
    private final String secret;

    /** unique API key identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** identifier of the listener the key authenticates to */
    public String listenerId() {
        return listenerId;
    }

    /** SMTP username associated with the key */
    public String username() {
        return username;
    }

    /** one-time plaintext secret, returned only by the create operation */
    public String secret() {
        return secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuedAPIKey that = (IssuedAPIKey) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.username, that.username)
                && Objects.equals(this.secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.listenerId, this.username, this.secret);
    }

    @Override
    public String toString() {
        return "IssuedAPIKey[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "username=" + this.username + ", " + "secret=" + this.secret + "]";
    }

    /**
     * Creates a new IssuedAPIKey.
     *
     * @param id unique API key identifier
     * @param tenantId identifier of the owning tenant
     * @param listenerId identifier of the listener the key authenticates to
     * @param username SMTP username associated with the key
     * @param secret one-time plaintext secret, returned only by the create operation
     */
    @JsonCreator
    public IssuedAPIKey(String id, String tenantId, String listenerId, String username, String secret) {
        this.id = id;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.username = username;
        this.secret = secret;
    }
}
