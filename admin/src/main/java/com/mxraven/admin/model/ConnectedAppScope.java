package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * A grantable connected-application permission.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConnectedAppScope {
    private final String scope;
    private final String description;

    /** the scope identifier */
    public String scope() {
        return scope;
    }

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
        ConnectedAppScope that = (ConnectedAppScope) o;
        return Objects.equals(this.scope, that.scope) && Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.scope, this.description);
    }

    @Override
    public String toString() {
        return "ConnectedAppScope[" + "scope=" + this.scope + ", " + "description=" + this.description + "]";
    }

    /**
     * Creates a new ConnectedAppScope.
     *
     * @param scope the scope identifier
     * @param description description
     */
    @JsonCreator
    public ConnectedAppScope(String scope, String description) {
        this.scope = scope;
        this.description = description;
    }
}
