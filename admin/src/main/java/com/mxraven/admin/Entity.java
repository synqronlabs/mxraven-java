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
    protected final AdminClient client;
    protected final String path;
    protected final D data;
    private volatile boolean deleted;

    protected Entity(AdminClient client, String path, D data) {
        this(client, path, data, false);
    }

    protected Entity(AdminClient client, String path, D data, boolean deleted) {
        this.client = client;
        this.path = path;
        this.data = data;
        this.deleted = deleted;
    }

    /** The raw wire record backing this entity. */
    public D data() {
        return data;
    }

    /**
     * Whether this entity has been deleted through this instance. Local state
     * only; not authoritative server state.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Delete this resource. Idempotent: once this instance has deleted it,
     * further calls are a local no-op.
     */
    public boolean delete() throws IOException {
        if (deleted) {
            return deleted;
        }
        client.delete(path);
        deleted = true;
        return deleted;
    }

    protected String path() {
        return path;
    }
}
