package com.mxraven.admin.model;

/**
 * Revision metadata without the source. Prefer {@link ScriptRevision} when the
 * source or diagnostics are needed.
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
 * @param publishedAt publication timestamp, when it was published
 */
public record ScriptRevisionSummary(
        String id,
        String listenerId,
        int revision,
        ScriptRevisionStatus status,
        boolean publishable,
        String sourceSha256,
        String schemaVersion,
        String createdBy,
        String createdAt,
        String publishedBy,
        String publishedAt) {
}
