package com.mxraven.admin;

import java.io.IOException;

/**
 * Base for hydrated resource entities. An entity wraps the wire record
 * {@code D} with the client and path needed to act on the resource, plus local
 * delete state.
 *
 * <p>Reads return a snapshot; call {@code reload()} (where supported) for fresh
 * state. {@link #isDeleted()} is local bookkeeping for deletes performed through
 * this instance — it cannot observe deletes made elsewhere.
 *
 * @param <D> the wire record backing this entity
 */
public abstract class Entity<D> {
    /** The client used to issue requests for this entity. */
    protected final AdminClient client;

    /** Client-relative path of this resource. */
    protected final String path;

    /** Raw wire record backing this entity. */
    protected final D data;

    private volatile boolean deleted;

    /**
     * Creates an entity bound to a client, path, and backing data.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing wire record
     */
    protected Entity(AdminClient client, String path, D data) {
        this(client, path, data, false);
    }

    /**
     * Creates an entity bound to a client, path, backing data, and delete state.
     *
     * @param client admin client used for requests
     * @param path resource path
     * @param data backing wire record
     * @param deleted whether the entity is considered deleted
     */
    protected Entity(AdminClient client, String path, D data, boolean deleted) {
        this.client = client;
        this.path = path;
        this.data = data;
        this.deleted = deleted;
    }

    /**
     * The raw wire record backing this entity.
     *
     * @return the underlying wire record
     */
    public D data() {
        return data;
    }

    /**
     * Whether this entity has been deleted through this instance. Local state
     * only; not authoritative server state.
     *
     * @return {@code true} if deleted through this instance
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Delete this resource. Idempotent: once this instance has deleted it,
     * further calls are a local no-op.
     *
     * @return {@code true} once the resource has been deleted through this
     *         instance
     * @throws IOException if the delete request fails or is interrupted
     */
    public boolean delete() throws IOException {
        if (deleted) {
            return deleted;
        }
        client.delete(path);
        deleted = true;
        return deleted;
    }

    /**
     * Returns the client-relative path of this resource.
     *
     * @return the resource path
     */
    protected String path() {
        return path;
    }
}
