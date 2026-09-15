package com.mxraven.admin.model;

/**
 * Outcome counts for a recipient-set member batch operation.
 */
public record RecipientSetBatchResult(
        int requested,
        int normalized,
        int added,
        int existing,
        int deleted,
        int missing) {
}
