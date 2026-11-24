package com.mxraven.admin.model;

import java.util.List;

/**
 * One saved routing-script revision, including its source and save-time
 * diagnostics.
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
 * @param publishedAt publication timestamp, when it was published
 */
public record ScriptRevision(
        String id,
        String listenerId,
        int revision,
        ScriptRevisionStatus status,
        boolean publishable,
        String source,
        String sourceSha256,
        String schemaVersion,
        ScriptDependencies dependencies,
        List<ScriptDiagnostic> diagnostics,
        String createdBy,
        String createdAt,
        String publishedBy,
        String publishedAt) {

    /**
     * Creates a revision, copying the diagnostic list.
     */
    public ScriptRevision {
        diagnostics = diagnostics == null ? List.of() : List.copyOf(diagnostics);
    }
}
