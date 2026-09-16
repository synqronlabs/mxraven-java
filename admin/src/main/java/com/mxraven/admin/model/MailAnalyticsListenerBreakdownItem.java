package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One ranked listener in an overview listener breakdown.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsListenerBreakdownItem {
    private final String listenerId;
    private final String displayName;
    private final MailAnalyticsListenerState listenerState;
    private final long egressTasks;

    /** listener identifier */
    public String listenerId() {
        return listenerId;
    }

    /** human-readable listener name */
    public String displayName() {
        return displayName;
    }

    /** current or deleted/unknown listener state */
    public MailAnalyticsListenerState listenerState() {
        return listenerState;
    }

    /** egress tasks attributed to the listener */
    public long egressTasks() {
        return egressTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsListenerBreakdownItem that = (MailAnalyticsListenerBreakdownItem) o;
        return Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.listenerState, that.listenerState)
                && this.egressTasks == that.egressTasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.displayName, this.listenerState, this.egressTasks);
    }

    @Override
    public String toString() {
        return "MailAnalyticsListenerBreakdownItem[" + "listenerId=" + this.listenerId + ", " + "displayName=" + this.displayName + ", " + "listenerState=" + this.listenerState + ", " + "egressTasks=" + this.egressTasks + "]";
    }

    /**
     * Creates a new MailAnalyticsListenerBreakdownItem.
     *
     * @param listenerId listener identifier
     * @param displayName human-readable listener name
     * @param listenerState current or deleted/unknown listener state
     * @param egressTasks egress tasks attributed to the listener
     */
    @JsonCreator
    public MailAnalyticsListenerBreakdownItem(String listenerId, String displayName, MailAnalyticsListenerState listenerState, long egressTasks) {
        this.listenerId = listenerId;
        this.displayName = displayName;
        this.listenerState = listenerState;
        this.egressTasks = egressTasks;
    }
}
