package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Result of claiming workspace access for an IdP-provisioned user.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class IdentityAccessClaim {
    private final boolean granted;

    /** whether workspace access was granted */
    public boolean granted() {
        return granted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityAccessClaim that = (IdentityAccessClaim) o;
        return this.granted == that.granted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.granted);
    }

    @Override
    public String toString() {
        return "IdentityAccessClaim[" + "granted=" + this.granted + "]";
    }

    /**
     * Creates a new IdentityAccessClaim.
     *
     * @param granted whether workspace access was granted
     */
    @JsonCreator
    public IdentityAccessClaim(boolean granted) {
        this.granted = granted;
    }
}
