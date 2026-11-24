package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners/{id}/script/rollback}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RollbackListenerScriptRequest {
    private final int revision;

    public int revision() {
        return revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RollbackListenerScriptRequest that = (RollbackListenerScriptRequest) o;
        return this.revision == that.revision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.revision);
    }

    @Override
    public String toString() {
        return "RollbackListenerScriptRequest[" + "revision=" + this.revision + "]";
    }

    /**
     * Creates a new RollbackListenerScriptRequest.
     *
     * @param revision revision
     */
    @JsonCreator
    public RollbackListenerScriptRequest(int revision) {
        this.revision = revision;
    }
}
