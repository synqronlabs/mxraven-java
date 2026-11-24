package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A dedicated IP pool leased to a tenant.
 *
 * <p>{@code streamType} is one of {@code transactional} or {@code marketing}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TenantDedicatedIPPool {
    private final String id;
    private final String name;
    private final StreamType streamType;
    private final String ref;

    /** unique pool identifier */
    public String id() {
        return id;
    }

    /** human-readable pool name */
    public String name() {
        return name;
    }

    /** mail stream the pool serves */
    public StreamType streamType() {
        return streamType;
    }

    public String ref() {
        return ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantDedicatedIPPool that = (TenantDedicatedIPPool) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.streamType, that.streamType) && Objects.equals(this.ref, that.ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.streamType, this.ref);
    }

    @Override
    public String toString() {
        return "TenantDedicatedIPPool[" + "id=" + this.id + ", " + "name=" + this.name + ", " + "streamType=" + this.streamType + ", " + "ref=" + this.ref + "]";
    }

    /**
     * Creates a new TenantDedicatedIPPool.
     *
     * @param id unique pool identifier
     * @param name human-readable pool name
     * @param streamType mail stream the pool serves
     * @param ref ref
     */
    @JsonCreator
    public TenantDedicatedIPPool(String id, String name, StreamType streamType, String ref) {
        this.id = id;
        this.name = name;
        this.streamType = streamType;
        this.ref = ref;
    }
}
