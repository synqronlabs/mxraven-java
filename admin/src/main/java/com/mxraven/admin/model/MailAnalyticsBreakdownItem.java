package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One ranked item in an analytics breakdown.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailAnalyticsBreakdownItem {
    private final String key;
    private final String displayName;
    private final MailAnalyticsResourceState resourceState;
    private final long value;

    /** stable dimension key */
    public String key() {
        return key;
    }

    /** human-readable label */
    public String displayName() {
        return displayName;
    }

    /** current state of the dimension resource */
    public MailAnalyticsResourceState resourceState() {
        return resourceState;
    }

    /** aggregated metric value for the item */
    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailAnalyticsBreakdownItem that = (MailAnalyticsBreakdownItem) o;
        return Objects.equals(this.key, that.key)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.resourceState, that.resourceState)
                && this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.displayName, this.resourceState, this.value);
    }

    @Override
    public String toString() {
        return "MailAnalyticsBreakdownItem[" + "key=" + this.key + ", " + "displayName=" + this.displayName + ", " + "resourceState=" + this.resourceState + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new MailAnalyticsBreakdownItem.
     *
     * @param key stable dimension key
     * @param displayName human-readable label
     * @param resourceState current state of the dimension resource
     * @param value aggregated metric value for the item
     */
    @JsonCreator
    public MailAnalyticsBreakdownItem(String key, String displayName, MailAnalyticsResourceState resourceState, long value) {
        this.key = key;
        this.displayName = displayName;
        this.resourceState = resourceState;
        this.value = value;
    }
}
