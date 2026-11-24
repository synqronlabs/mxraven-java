package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The published routing script rendered for export.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ExportedListenerScript {
    private final String listenerId;
    private final int revision;
    private final String source;
    private final ScriptDependencies dependencies;

    /** owning listener identifier */
    public String listenerId() {
        return listenerId;
    }

    /** published revision number */
    public int revision() {
        return revision;
    }

    /** Lua source */
    public String source() {
        return source;
    }

    public ScriptDependencies dependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExportedListenerScript that = (ExportedListenerScript) o;
        return Objects.equals(this.listenerId, that.listenerId) && this.revision == that.revision && Objects.equals(this.source, that.source) && Objects.equals(this.dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.revision, this.source, this.dependencies);
    }

    @Override
    public String toString() {
        return "ExportedListenerScript[" + "listenerId=" + this.listenerId + ", " + "revision=" + this.revision + ", " + "source=" + this.source + ", " + "dependencies=" + this.dependencies + "]";
    }

    /**
     * Creates a new ExportedListenerScript.
     *
     * @param listenerId owning listener identifier
     * @param revision published revision number
     * @param source Lua source
     * @param dependencies dependencies
     */
    @JsonCreator
    public ExportedListenerScript(String listenerId, int revision, String source, ScriptDependencies dependencies) {
        this.listenerId = listenerId;
        this.revision = revision;
        this.source = source;
        this.dependencies = dependencies;
    }
}
