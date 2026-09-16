package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Whether an auto-reply template's sender can currently deliver.
 *
 * <p>{@code status} is one of {@code ready}, {@code invalid_from_address},
 * {@code domain_not_found}, {@code sending_not_enabled},
 * {@code domain_not_verified}, or {@code dkim_not_verified}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AutoReplySenderReadiness {
    private final boolean ready;
    private final AutoReplySenderReadinessStatus status;

    /** whether the sender can currently deliver */
    public boolean ready() {
        return ready;
    }

    /** detailed sender-readiness status */
    public AutoReplySenderReadinessStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AutoReplySenderReadiness that = (AutoReplySenderReadiness) o;
        return this.ready == that.ready
                && Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ready, this.status);
    }

    @Override
    public String toString() {
        return "AutoReplySenderReadiness[" + "ready=" + this.ready + ", " + "status=" + this.status + "]";
    }

    /**
     * Creates a new AutoReplySenderReadiness.
     *
     * @param ready whether the sender can currently deliver
     * @param status detailed sender-readiness status
     */
    @JsonCreator
    public AutoReplySenderReadiness(boolean ready, AutoReplySenderReadinessStatus status) {
        this.ready = ready;
        this.status = status;
    }
}
