package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One non-secret tenant resource match from resource search.
 *
 * <p>{@code resourceType} is a {@code TenantSearchResourceType} value. Results
 * are ordered by exact, then prefix, then substring relevance.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantResourceSearchResult {
    private final TenantSearchResourceType resourceType;
    private final String resourceId;
    private final String parentResourceId;
    private final String label;
    private final String reference;
    private final String description;

    /** type of the matched resource */
    public TenantSearchResourceType resourceType() {
        return resourceType;
    }

    /** identifier of the matched resource */
    public String resourceId() {
        return resourceId;
    }

    /** identifier of the parent resource, if any */
    public String parentResourceId() {
        return parentResourceId;
    }

    /** human-readable resource label */
    public String label() {
        return label;
    }

    /** stable resource reference */
    public String reference() {
        return reference;
    }

    /** resource description */
    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantResourceSearchResult that = (TenantResourceSearchResult) o;
        return Objects.equals(this.resourceType, that.resourceType)
                && Objects.equals(this.resourceId, that.resourceId)
                && Objects.equals(this.parentResourceId, that.parentResourceId)
                && Objects.equals(this.label, that.label)
                && Objects.equals(this.reference, that.reference)
                && Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resourceType, this.resourceId, this.parentResourceId, this.label, this.reference, this.description);
    }

    @Override
    public String toString() {
        return "TenantResourceSearchResult[" + "resourceType=" + this.resourceType + ", " + "resourceId=" + this.resourceId + ", " + "parentResourceId=" + this.parentResourceId + ", " + "label=" + this.label + ", " + "reference=" + this.reference + ", " + "description=" + this.description + "]";
    }

    /**
     * Creates a new TenantResourceSearchResult.
     *
     * @param resourceType type of the matched resource
     * @param resourceId identifier of the matched resource
     * @param parentResourceId identifier of the parent resource, if any
     * @param label human-readable resource label
     * @param reference stable resource reference
     * @param description resource description
     */
    @JsonCreator
    public TenantResourceSearchResult(TenantSearchResourceType resourceType, String resourceId, String parentResourceId, String label, String reference, String description) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.parentResourceId = parentResourceId;
        this.label = label;
        this.reference = reference;
        this.description = description;
    }
}
