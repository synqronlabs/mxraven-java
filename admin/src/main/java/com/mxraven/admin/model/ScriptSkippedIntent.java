package com.mxraven.admin.model;

/**
 * A non-terminal effect skipped by an execution-time check.
 *
 * @param intent the skipped effect
 * @param reason why it was skipped
 */
public record ScriptSkippedIntent(ScriptIntent intent, String reason) {
}
