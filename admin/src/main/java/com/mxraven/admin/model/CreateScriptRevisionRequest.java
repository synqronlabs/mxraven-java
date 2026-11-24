package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for {@code POST /v2/tenants/{slug}/listeners/{id}/script/revisions}.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateScriptRevisionRequest {
    private final String source;

    public String source() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateScriptRevisionRequest that = (CreateScriptRevisionRequest) o;
        return Objects.equals(this.source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.source);
    }

    @Override
    public String toString() {
        return "CreateScriptRevisionRequest[" + "source=" + this.source + "]";
    }

    /**
     * Creates a new CreateScriptRevisionRequest.
     *
     * @param source source
     */
    @JsonCreator
    public CreateScriptRevisionRequest(String source) {
        this.source = source;
    }

    /**
         * Creates a draft request for the given source.
         *
         * @param source Lua source
         * @return the request
         */
        public static CreateScriptRevisionRequest of(String source) {
            return new CreateScriptRevisionRequest(source);
        }
}
