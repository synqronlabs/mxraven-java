package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A non-terminal effect skipped by an execution-time check.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptSkippedIntent {
    private final ScriptIntent intent;
    private final String reason;

    /** the skipped effect */
    public ScriptIntent intent() {
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
        ScriptSkippedIntent that = (ScriptSkippedIntent) o;
        return Objects.equals(this.intent, that.intent) && Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.intent, this.reason);
    }

    @Override
    public String toString() {
        return "ScriptSkippedIntent[" + "intent=" + this.intent + ", " + "reason=" + this.reason + "]";
    }

    /**
     * Creates a new ScriptSkippedIntent.
     *
     * @param intent the skipped effect
     * @param reason reason
     */
    @JsonCreator
    public ScriptSkippedIntent(ScriptIntent intent, String reason) {
        this.intent = intent;
        this.reason = reason;
    }
}
