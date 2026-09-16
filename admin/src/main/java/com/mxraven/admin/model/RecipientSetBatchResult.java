package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Outcome counts for a recipient-set member batch operation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientSetBatchResult {
    private final int requested;
    private final int normalized;
    private final int added;
    private final int existing;
    private final int deleted;
    private final int missing;

    /** members requested by the operation */
    public int requested() {
        return requested;
    }

    /** members that normalized to a valid address */
    public int normalized() {
        return normalized;
    }

    /** members added */
    public int added() {
        return added;
    }

    /** members that already existed */
    public int existing() {
        return existing;
    }

    /** members deleted */
    public int deleted() {
        return deleted;
    }

    /** members that were not found */
    public int missing() {
        return missing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientSetBatchResult that = (RecipientSetBatchResult) o;
        return this.requested == that.requested
                && this.normalized == that.normalized
                && this.added == that.added
                && this.existing == that.existing
                && this.deleted == that.deleted
                && this.missing == that.missing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.requested, this.normalized, this.added, this.existing, this.deleted, this.missing);
    }

    @Override
    public String toString() {
        return "RecipientSetBatchResult[" + "requested=" + this.requested + ", " + "normalized=" + this.normalized + ", " + "added=" + this.added + ", " + "existing=" + this.existing + ", " + "deleted=" + this.deleted + ", " + "missing=" + this.missing + "]";
    }

    /**
     * Creates a new RecipientSetBatchResult.
     *
     * @param requested members requested by the operation
     * @param normalized members that normalized to a valid address
     * @param added members added
     * @param existing members that already existed
     * @param deleted members deleted
     * @param missing members that were not found
     */
    @JsonCreator
    public RecipientSetBatchResult(int requested, int normalized, int added, int existing, int deleted, int missing) {
        this.requested = requested;
        this.normalized = normalized;
        this.added = added;
        this.existing = existing;
        this.deleted = deleted;
        this.missing = missing;
    }
}
