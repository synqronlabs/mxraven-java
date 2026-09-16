package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Outcome of confirming SMTP forward destination ownership.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SmtpForwardDestinationConfirmation {
    private final boolean verified;

    /** whether ownership of the destination was verified */
    public boolean verified() {
        return verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmtpForwardDestinationConfirmation that = (SmtpForwardDestinationConfirmation) o;
        return this.verified == that.verified;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.verified);
    }

    @Override
    public String toString() {
        return "SmtpForwardDestinationConfirmation[" + "verified=" + this.verified + "]";
    }

    /**
     * Creates a new SmtpForwardDestinationConfirmation.
     *
     * @param verified whether ownership of the destination was verified
     */
    @JsonCreator
    public SmtpForwardDestinationConfirmation(boolean verified) {
        this.verified = verified;
    }
}
