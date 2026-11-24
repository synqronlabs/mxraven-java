package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * A script revision's dependency manifest: the only resource references its
 * effects may use. References are resolved and ownership-checked when the
 * revision is saved.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ScriptDependencies {
    private final List<String> forward;
    private final List<String> relay;
    private final List<String> storage;
    private final List<String> endpoint;
    private final List<String> template;
    private final List<String> pool;
    private final List<String> sets;

    /** SMTP forward-destination references */
    public List<String> forward() {
        return forward;
    }

    /** SMTP relay references */
    public List<String> relay() {
        return relay;
    }

    /** storage-integration references */
    public List<String> storage() {
        return storage;
    }

    /** webhook-endpoint references used by {@code mx.http} */
    public List<String> endpoint() {
        return endpoint;
    }

    /** auto-reply-template references */
    public List<String> template() {
        return template;
    }

    /** dedicated IP pool references */
    public List<String> pool() {
        return pool;
    }

    public List<String> sets() {
        return sets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptDependencies that = (ScriptDependencies) o;
        return Objects.equals(this.forward, that.forward) && Objects.equals(this.relay, that.relay) && Objects.equals(this.storage, that.storage) && Objects.equals(this.endpoint, that.endpoint) && Objects.equals(this.template, that.template) && Objects.equals(this.pool, that.pool) && Objects.equals(this.sets, that.sets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.forward, this.relay, this.storage, this.endpoint, this.template, this.pool, this.sets);
    }

    @Override
    public String toString() {
        return "ScriptDependencies[" + "forward=" + this.forward + ", " + "relay=" + this.relay + ", " + "storage=" + this.storage + ", " + "endpoint=" + this.endpoint + ", " + "template=" + this.template + ", " + "pool=" + this.pool + ", " + "sets=" + this.sets + "]";
    }

    /**
     * Creates a new ScriptDependencies.
     *
     * @param forward SMTP forward-destination references
     * @param relay SMTP relay references
     * @param storage storage-integration references
     * @param endpoint webhook-endpoint references used by {@code mx.http}
     * @param template auto-reply-template references
     * @param pool dedicated IP pool references
     * @param sets sets
     */
    @JsonCreator
    public ScriptDependencies(List<String> forward, List<String> relay, List<String> storage, List<String> endpoint, List<String> template, List<String> pool, List<String> sets) {
        this.forward = forward == null ? Java8.list() : Java8.copyList(forward);
        this.relay = relay == null ? Java8.list() : Java8.copyList(relay);
        this.storage = storage == null ? Java8.list() : Java8.copyList(storage);
        this.endpoint = endpoint == null ? Java8.list() : Java8.copyList(endpoint);
        this.template = template == null ? Java8.list() : Java8.copyList(template);
        this.pool = pool == null ? Java8.list() : Java8.copyList(pool);
        this.sets = sets == null ? Java8.list() : Java8.copyList(sets);
    }

    /**
         * Creates a dependency manifest, copying every list.
         */
}
