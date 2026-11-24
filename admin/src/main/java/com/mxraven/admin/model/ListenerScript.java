package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A listener's routing script.
 *
 * <p>A listener without a published revision uses the listener-type default:
 * submission listeners deliver through the shared pool, and MTA listeners drop
 * after acceptance.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ListenerScript {
    private final String listenerId;
    private final ListenerType listenerType;
    private final boolean bodyAccess;
    private final ScriptRevision published;

    /** owning listener identifier */
    public String listenerId() {
        return listenerId;
    }

    /** submission or MTA */
    public ListenerType listenerType() {
        return listenerType;
    }

    /** whether {@code mail.body:text()} may read the spooled body (MTA only) */
    public boolean bodyAccess() {
        return bodyAccess;
    }

    public ScriptRevision published() {
        return published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListenerScript that = (ListenerScript) o;
        return Objects.equals(this.listenerId, that.listenerId) && Objects.equals(this.listenerType, that.listenerType) && this.bodyAccess == that.bodyAccess && Objects.equals(this.published, that.published);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.listenerType, this.bodyAccess, this.published);
    }

    @Override
    public String toString() {
        return "ListenerScript[" + "listenerId=" + this.listenerId + ", " + "listenerType=" + this.listenerType + ", " + "bodyAccess=" + this.bodyAccess + ", " + "published=" + this.published + "]";
    }

    /**
     * Creates a new ListenerScript.
     *
     * @param listenerId owning listener identifier
     * @param listenerType submission or MTA
     * @param bodyAccess whether {@code mail.body:text()} may read the spooled body (MTA only)
     * @param published published
     */
    @JsonCreator
    public ListenerScript(String listenerId, ListenerType listenerType, boolean bodyAccess, ScriptRevision published) {
        this.listenerId = listenerId;
        this.listenerType = listenerType;
        this.bodyAccess = bodyAccess;
        this.published = published;
    }
}
