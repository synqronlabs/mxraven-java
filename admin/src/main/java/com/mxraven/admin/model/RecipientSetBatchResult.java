package com.mxraven.admin.model;

/**
 * Outcome counts for a recipient-set member batch operation.
 *
 * @param requested  members requested by the operation
 * @param normalized members that normalized to a valid address
 * @param added      members added
 * @param existing   members that already existed
 * @param deleted    members deleted
 * @param missing    members that were not found
 */
public record RecipientSetBatchResult(
        int requested,
        int normalized,
        int added,
        int existing,
        int deleted,
        int missing) {
}
