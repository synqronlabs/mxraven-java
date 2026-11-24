package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation of a submission or MTA listener. Prefer the
 * {@link Listener} entity.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ListenerData {
    private final String id;
    private final String tenantId;
    private final String displayName;
    private final ListenerType listenerType;
    private final StreamType streamType;
    private final boolean rspamdScanningEnabled;
    private final String activeScriptRevisionId;
    private final boolean scriptBodyAccess;

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

    /** whether inbound Rspamd scanning is enabled */
    public boolean rspamdScanningEnabled() {
        return rspamdScanningEnabled;
    }

    /** the published routing-script revision, when one exists */
    public String activeScriptRevisionId() {
        return activeScriptRevisionId;
    }

    public boolean scriptBodyAccess() {
        return scriptBodyAccess;
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
        return Objects.equals(this.id, that.id) && Objects.equals(this.tenantId, that.tenantId) && Objects.equals(this.displayName, that.displayName) && Objects.equals(this.listenerType, that.listenerType) && Objects.equals(this.streamType, that.streamType) && this.rspamdScanningEnabled == that.rspamdScanningEnabled && Objects.equals(this.activeScriptRevisionId, that.activeScriptRevisionId) && this.scriptBodyAccess == that.scriptBodyAccess;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.tenantId, this.displayName, this.listenerType, this.streamType, this.rspamdScanningEnabled, this.activeScriptRevisionId, this.scriptBodyAccess);
    }

    @Override
    public String toString() {
        return "ListenerData[" + "id=" + this.id + ", " + "tenantId=" + this.tenantId + ", " + "displayName=" + this.displayName + ", " + "listenerType=" + this.listenerType + ", " + "streamType=" + this.streamType + ", " + "rspamdScanningEnabled=" + this.rspamdScanningEnabled + ", " + "activeScriptRevisionId=" + this.activeScriptRevisionId + ", " + "scriptBodyAccess=" + this.scriptBodyAccess + "]";
    }

    /**
     * Creates a new ListenerData.
     *
     * @param id unique listener identifier
     * @param tenantId identifier of the owning tenant
     * @param displayName human-readable listener name
     * @param listenerType whether this is a submission or MTA listener
     * @param streamType message stream handled by the listener
     * @param rspamdScanningEnabled whether inbound Rspamd scanning is enabled
     * @param activeScriptRevisionId the published routing-script revision, when one exists
     * @param scriptBodyAccess scriptBodyAccess
     */
    @JsonCreator
    public ListenerData(String id, String tenantId, String displayName, ListenerType listenerType, StreamType streamType, boolean rspamdScanningEnabled, String activeScriptRevisionId, boolean scriptBodyAccess) {
        this.id = id;
        this.tenantId = tenantId;
        this.displayName = displayName;
        this.listenerType = listenerType;
        this.streamType = streamType;
        this.rspamdScanningEnabled = rspamdScanningEnabled;
        this.activeScriptRevisionId = activeScriptRevisionId;
        this.scriptBodyAccess = scriptBodyAccess;
    }
}
