package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/identity-providers/{idp_id}/auto-grant</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateIdentityProviderAutoGrantRequest {
    private final boolean enabled;

    /** whether automatic role grants are enabled */
    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateIdentityProviderAutoGrantRequest that = (UpdateIdentityProviderAutoGrantRequest) o;
        return this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enabled);
    }

    @Override
    public String toString() {
        return "UpdateIdentityProviderAutoGrantRequest[" + "enabled=" + this.enabled + "]";
    }

    /**
     * Creates a new UpdateIdentityProviderAutoGrantRequest.
     *
     * @param enabled whether automatic role grants are enabled
     */
    @JsonCreator
    public UpdateIdentityProviderAutoGrantRequest(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateIdentityProviderAutoGrantRequest} instances. */
    public static final class Builder {
        private boolean enabled;

        /**
         * Sets whether automatic role grants are enabled.
         *
         * @param enabled whether automatic role grants are enabled
         * @return this builder
         */
        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         */
        public UpdateIdentityProviderAutoGrantRequest build() {
            return new UpdateIdentityProviderAutoGrantRequest(enabled);
        }
    }
}
