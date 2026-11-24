package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for
 * {@code PUT /v2/tenants/{slug}/listeners/{id}/script/body-access}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UpdateListenerScriptBodyAccessRequest {
    private final boolean enabled;

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
        UpdateListenerScriptBodyAccessRequest that = (UpdateListenerScriptBodyAccessRequest) o;
        return this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enabled);
    }

    @Override
    public String toString() {
        return "UpdateListenerScriptBodyAccessRequest[" + "enabled=" + this.enabled + "]";
    }

    /**
     * Creates a new UpdateListenerScriptBodyAccessRequest.
     *
     * @param enabled enabled
     */
    @JsonCreator
    public UpdateListenerScriptBodyAccessRequest(boolean enabled) {
        this.enabled = enabled;
    }
}
