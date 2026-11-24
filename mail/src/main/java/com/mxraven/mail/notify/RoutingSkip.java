package com.mxraven.mail.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A non-terminal effect an execution-time check skipped.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RoutingSkip {
    private final RoutingIntent intent;
    private final String reason;

    /** the skipped effect */
    public RoutingIntent intent() {
        return intent;
    }

    public String reason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoutingSkip that = (RoutingSkip) o;
        return Objects.equals(this.intent, that.intent) && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.intent, this.reason);
    }

    @Override
    public String toString() {
        return "RoutingSkip[" + "intent=" + this.intent + ", " + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new RoutingSkip.
     *
     * @param intent the skipped effect
     * @param reason reason
     */
    @JsonCreator
    public RoutingSkip(RoutingIntent intent, String reason) {
        this.intent = intent;
        this.reason = reason;
    }
}
