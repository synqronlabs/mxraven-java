package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Revision metadata without the source. Prefer {@link ScriptRevision} when the
 * source or diagnostics are needed.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptRevisionSummary {
    private final String id;
    private final String listenerId;
    private final int revision;
    private final ScriptRevisionStatus status;
    private final boolean publishable;
    private final String sourceSha256;
    private final String schemaVersion;
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

    /** lowercase hex SHA-256 of the source */
    public String sourceSha256() {
        return sourceSha256;
    }

    /** fact schema the revision is pinned to, such as {@code mxraven/mail/v1} */
    public String schemaVersion() {
        return schemaVersion;
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
        ScriptRevisionSummary that = (ScriptRevisionSummary) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.listenerId, that.listenerId) && this.revision == that.revision && Objects.equals(this.status, that.status) && this.publishable == that.publishable && Objects.equals(this.sourceSha256, that.sourceSha256) && Objects.equals(this.schemaVersion, that.schemaVersion) && Objects.equals(this.createdBy, that.createdBy) && Objects.equals(this.createdAt, that.createdAt) && Objects.equals(this.publishedBy, that.publishedBy) && Objects.equals(this.publishedAt, that.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.listenerId, this.revision, this.status, this.publishable, this.sourceSha256, this.schemaVersion, this.createdBy, this.createdAt, this.publishedBy, this.publishedAt);
    }

    @Override
    public String toString() {
        return "ScriptRevisionSummary[" + "id=" + this.id + ", " + "listenerId=" + this.listenerId + ", " + "revision=" + this.revision + ", " + "status=" + this.status + ", " + "publishable=" + this.publishable + ", " + "sourceSha256=" + this.sourceSha256 + ", " + "schemaVersion=" + this.schemaVersion + ", " + "createdBy=" + this.createdBy + ", " + "createdAt=" + this.createdAt + ", " + "publishedBy=" + this.publishedBy + ", " + "publishedAt=" + this.publishedAt + "]";
    }

    /**
     * Creates a new ScriptRevisionSummary.
     *
     * @param id revision identifier
     * @param listenerId owning listener identifier
     * @param revision per-listener revision number, starting at 1
     * @param status lifecycle state
     * @param publishable whether the revision compiled and validated without errors
     * @param sourceSha256 lowercase hex SHA-256 of the source
     * @param schemaVersion fact schema the revision is pinned to, such as {@code mxraven/mail/v1}
     * @param createdBy principal that saved the revision
     * @param createdAt save timestamp
     * @param publishedBy principal that published the revision, when it was published
     * @param publishedAt publishedAt
     */
    @JsonCreator
    public ScriptRevisionSummary(String id, String listenerId, int revision, ScriptRevisionStatus status, boolean publishable, String sourceSha256, String schemaVersion, String createdBy, String createdAt, String publishedBy, String publishedAt) {
        this.id = id;
        this.listenerId = listenerId;
        this.revision = revision;
        this.status = status;
        this.publishable = publishable;
        this.sourceSha256 = sourceSha256;
        this.schemaVersion = schemaVersion;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.publishedBy = publishedBy;
        this.publishedAt = publishedAt;
    }
}
