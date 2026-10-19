package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Identity of the listener a mail-analytics overview describes.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerIdentity {
    private final String listenerId;
    private final String displayName;
    private final MailAnalyticsListenerState listenerState;

    /** unique listener identifier */
    public String listenerId() {
        return listenerId;
    }

    /** human-readable listener name */
    public String displayName() {
        return displayName;
    }

    /** whether the listener still exists */
    public MailAnalyticsListenerState listenerState() {
        return listenerState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsListenerIdentity that = (MailAnalyticsListenerIdentity) o;
        return Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.listenerState, that.listenerState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.displayName, this.listenerState);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerIdentity[" + "listenerId=" + this.listenerId + ", " + "displayName=" + this.displayName + ", " + "listenerState=" + this.listenerState + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerIdentity.
     *
     * @param listenerId unique listener identifier
     * @param displayName human-readable listener name
     * @param listenerState whether the listener still exists
     */
    @JsonCreator
    public MailAnalyticsListenerIdentity(String listenerId, String displayName, MailAnalyticsListenerState listenerState) {
        this.listenerId = listenerId;
        this.displayName = displayName;
        this.listenerState = listenerState;
    }
}
