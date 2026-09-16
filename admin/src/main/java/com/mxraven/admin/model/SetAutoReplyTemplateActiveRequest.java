package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Request body for <code>PUT .../auto-reply-templates/{template_id}/active</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SetAutoReplyTemplateActiveRequest {
    private final boolean isActive;

    /** whether the template is active */
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetAutoReplyTemplateActiveRequest that = (SetAutoReplyTemplateActiveRequest) o;
        return this.isActive == that.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isActive);
    }

    @Override
    public String toString() {
        return "SetAutoReplyTemplateActiveRequest[" + "isActive=" + this.isActive + "]";
    }

    /**
     * Creates a new SetAutoReplyTemplateActiveRequest.
     *
     * @param isActive whether the template is active
     */
    @JsonCreator
    public SetAutoReplyTemplateActiveRequest(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link SetAutoReplyTemplateActiveRequest} instances. */
    public static final class Builder {
        private boolean isActive;

        /**
         * Sets whether the template is active.
         *
         * @param isActive whether the template is active
         * @return this builder
         */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return a new request
         */
        public SetAutoReplyTemplateActiveRequest build() {
            return new SetAutoReplyTemplateActiveRequest(isActive);
        }
    }
}
