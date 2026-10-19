package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Client notification setting of a listener.
 *
 * <p>When enabled, the listener publishes client notification events on
 * {@code subject}; clients bind the durable pull {@code consumer} to read them.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ListenerNotificationSetting {
    private final boolean enabled;
    private final String subject;
    private final String consumer;

    /** whether the listener publishes client notification events */
    public boolean enabled() {
        return enabled;
    }

    /** subject prefix client notifications are published on for this listener */
    public String subject() {
        return subject;
    }

    /** durable pull consumer a client binds to in order to read those events */
    public String consumer() {
        return consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListenerNotificationSetting that = (ListenerNotificationSetting) o;
        return this.enabled == that.enabled
                && Objects.equals(this.subject, that.subject)
                && Objects.equals(this.consumer, that.consumer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.enabled, this.subject, this.consumer);
    }

    @Override
    public String toString() {
        return "ListenerNotificationSetting[" + "enabled=" + this.enabled + ", " + "subject=" + this.subject + ", " + "consumer=" + this.consumer + "]";
    }

    /**
     * Creates a new ListenerNotificationSetting.
     *
     * @param enabled whether the listener publishes client notification events
     * @param subject subject prefix client notifications are published on for this listener
     * @param consumer durable pull consumer a client binds to in order to read those events
     */
    @JsonCreator
    public ListenerNotificationSetting(boolean enabled, String subject, String consumer) {
        this.enabled = enabled;
        this.subject = subject;
        this.consumer = consumer;
    }
}
