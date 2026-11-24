package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * One saved routing-script revision, including its source and save-time
 * diagnostics.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptRevision {
    private final String id;
    private final String listenerId;
    private final int revision;
    private final ScriptRevisionStatus status;
    private final boolean publishable;
    private final String source;
    private final String sourceSha256;
    private final String schemaVersion;
    private final ScriptDependencies dependencies;
    private final List<ScriptDiagnostic> diagnostics;
    private final String createdBy;
    private final String createdAt;
    private final String publishedBy;
    private final String publishedAt;

    /** revision identifier */
    public String id() {
        return id;
    }

    /** owning listener identifier */
    public String listenerId() {
        return listenerId;
    }

    /** per-listener revision number, starting at 1 */
    public int revision() {
        return revision;
    }

    /** lifecycle state */
    public ScriptRevisionStatus status() {
        return status;
    }

    /** whether the revision compiled and validated without errors */
    public boolean publishable() {
        return publishable;
    }

    /** Lua 5.4 source defining {@code function handle(mail, mx)} */
    public String source() {
        return source;
    }

    /** lowercase hex SHA-256 of the source */
    public String sourceSha256() {
        return sourceSha256;
    }

    /** fact schema the revision is pinned to */
    public String schemaVersion() {
        return schemaVersion;
    }

    /** resolved dependency manifest */
    public ScriptDependencies dependencies() {
        return dependencies;
    }

    /** save-time findings */
    public List<ScriptDiagnostic> diagnostics() {
        return diagnostics;
    }

    /** principal that saved the revision */
    public String createdBy() {
        return createdBy;
    }

    /** save timestamp */
    public String createdAt() {
        return createdAt;
    }

    /** principal that published the revision, when it was published */
    public String publishedBy() {
        return publishedBy;
    }

    public String publishedAt() {
        return publishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptRevision that = (ScriptRevision) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.listenerId, that.listenerId) && this.revision == that.revision && Objects.equals(this.status, that.status) && this.publishable == that.publishable && Objects.equals(this.source, that.source) && Objects.equals(this.sourceSha256, that.sourceSha256) && Objects.equals(this.schemaVersion, that.schemaVersion) && Objects.equals(this.dependencies, that.dependencies) && Objects.equals(this.diagnostics, that.diagnostics) && Objects.equals(this.createdBy, that.createdBy) && Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.publishedBy, that.publishedBy) && Objects.equals(this.publishedAt, that.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.listenerId, this.revision, this.status, this.publishable, this.source, this.sourceSha256, this.schemaVersion, this.dependencies, this.diagnostics, this.createdBy, this.createdAt, this.publishedBy, this.publishedAt);
    }

    @Override
    public String toString() {
        return "ScriptRevision[" + "id=" + this.id + ", " + "listenerId=" + this.listenerId + ", " + "revision=" + this.revision + ", " + "status=" + this.status + ", " + "publishable=" + this.publishable + ", " + "source=" + this.source + ", " + "sourceSha256=" + this.sourceSha256 + ", " + "schemaVersion=" + this.schemaVersion + ", " + "dependencies=" + this.dependencies + ", " + "diagnostics=" + this.diagnostics + ", " + "createdBy=" + this.createdBy + ", " + "createdAt=" + this.createdAt + ", " + "publishedBy=" + this.publishedBy + ", " + "publishedAt=" + this.publishedAt + "]";
    }

    /**
     * Creates a new ScriptRevision.
     *
     * @param id revision identifier
     * @param listenerId owning listener identifier
     * @param revision per-listener revision number, starting at 1
     * @param status lifecycle state
     * @param publishable whether the revision compiled and validated without errors
     * @param source Lua 5.4 source defining {@code function handle(mail, mx)}
     * @param sourceSha256 lowercase hex SHA-256 of the source
     * @param schemaVersion fact schema the revision is pinned to
     * @param dependencies resolved dependency manifest
     * @param diagnostics save-time findings
     * @param createdBy principal that saved the revision
     * @param createdAt save timestamp
     * @param publishedBy principal that published the revision, when it was published
     * @param publishedAt publishedAt
     */
    @JsonCreator
    public ScriptRevision(String id, String listenerId, int revision, ScriptRevisionStatus status, boolean publishable, String source, String sourceSha256, String schemaVersion, ScriptDependencies dependencies, List<ScriptDiagnostic> diagnostics, String createdBy, String createdAt, String publishedBy, String publishedAt) {
        this.id = id;
        this.listenerId = listenerId;
        this.revision = revision;
        this.status = status;
        this.publishable = publishable;
        this.source = source;
        this.sourceSha256 = sourceSha256;
        this.schemaVersion = schemaVersion;
        this.dependencies = dependencies;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.publishedBy = publishedBy;
        this.publishedAt = publishedAt;
        this.diagnostics = diagnostics == null ? Java8.list() : Java8.copyList(diagnostics);
    }

    /**
         * Creates a revision, copying the diagnostic list.
         */
}
