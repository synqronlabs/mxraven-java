package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation of a submission or MTA listener. Prefer the
 * {@link Listener} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ListenerData {
    private final String id;
    private final String tenantId;
    private final String displayName;
    private final ListenerType listenerType;
    private final StreamType streamType;
    private final TerminalActionType defaultTerminalActionType;
    private final TerminalActionPayload defaultTerminalActionPayload;
    private final boolean rspamdScanningEnabled;

    /** unique listener identifier */
    public String id() {
        return id;
    }

    /** identifier of the owning tenant */
    public String tenantId() {
        return tenantId;
    }

    /** human-readable listener name */
    public String displayName() {
        return displayName;
    }

    /** whether this is a submission or MTA listener */
    public ListenerType listenerType() {
        return listenerType;
    }

    /** message stream handled by the listener */
    public StreamType streamType() {
        return streamType;
    }

    /** terminal action applied when no rule matches */
    public TerminalActionType defaultTerminalActionType() {
        return defaultTerminalActionType;
    }

    /** action-specific payload for the default action */
    public TerminalActionPayload defaultTerminalActionPayload() {
        return defaultTerminalActionPayload;
    }

    /** whether inbound Rspamd scanning is enabled */
    public boolean rspamdScanningEnabled() {
        return rspamdScanningEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListenerData that = (ListenerData) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.listenerType, that.listenerType)
                && Objects.equals(this.streamType, that.streamType)
                && Objects.equals(this.defaultTerminalActionType, that.defaultTerminalActionType)
                && Objects.equals(this.defaultTerminalActionPayload, that.defaultTerminalActionPayload)
                && this.rspamdScanningEnabled == that.rspamdScanningEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.displayName, this.listenerType, this.streamType, this.defaultTerminalActionType, this.defaultTerminalActionPayload, this.rspamdScanningEnabled);
    }

    @Override
    public String toString() {
        return "ListenerData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "displayName=" + this.displayName + ", " + "listenerType=" + this.listenerType + ", " + "streamType=" + this.streamType + ", " + "defaultTerminalActionType=" + this.defaultTerminalActionType + ", " + "defaultTerminalActionPayload=" + this.defaultTerminalActionPayload + ", " + "rspamdScanningEnabled=" + this.rspamdScanningEnabled + "]";
    }

    /**
     * Creates a new ListenerData.
     *
     * @param id unique listener identifier
     * @param tenantId identifier of the owning tenant
     * @param displayName human-readable listener name
     * @param listenerType whether this is a submission or MTA listener
     * @param streamType message stream handled by the listener
     * @param defaultTerminalActionType terminal action applied when no rule matches
     * @param defaultTerminalActionPayload action-specific payload for the default action
     * @param rspamdScanningEnabled whether inbound Rspamd scanning is enabled
     */
    @JsonCreator
    public ListenerData(String id, String tenantId, String displayName, ListenerType listenerType, StreamType streamType, TerminalActionType defaultTerminalActionType, TerminalActionPayload defaultTerminalActionPayload, boolean rspamdScanningEnabled) {
        this.id = id;
        this.tenantId = tenantId;
        this.displayName = displayName;
        this.listenerType = listenerType;
        this.streamType = streamType;
        this.defaultTerminalActionType = defaultTerminalActionType;
        this.defaultTerminalActionPayload = defaultTerminalActionPayload;
        this.rspamdScanningEnabled = rspamdScanningEnabled;
    }
}
