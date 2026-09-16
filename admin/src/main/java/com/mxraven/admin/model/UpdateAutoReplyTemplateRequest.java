package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Request body for
 * <code>PUT /v2/tenants/{slug}/auto-reply-templates/{template_id}</code>.
 *
 * <p>Replaces all writable fields; {@code textBody} and {@code htmlBody} may be
 * null.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.ALWAYS)
public final class UpdateAutoReplyTemplateRequest {
    private final String displayName;
    private final String fromAddress;
    private final String subject;
    private final String textBody;
    private final String htmlBody;
    private final List<AutoReplyTemplateHeader> headers;

    /** human-readable template name */
    public String displayName() {
        return displayName;
    }

    /** From address used for replies */
    public String fromAddress() {
        return fromAddress;
    }

    /** template subject */
    public String subject() {
        return subject;
    }

    /** plain-text body; may be null */
    public String textBody() {
        return textBody;
    }

    /** HTML body; may be null */
    public String htmlBody() {
        return htmlBody;
    }

    /** additional template headers */
    public List<AutoReplyTemplateHeader> headers() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateAutoReplyTemplateRequest that = (UpdateAutoReplyTemplateRequest) o;
        return Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.fromAddress, that.fromAddress)
                && Objects.equals(this.subject, that.subject)
                && Objects.equals(this.textBody, that.textBody)
                && Objects.equals(this.htmlBody, that.htmlBody)
                && Objects.equals(this.headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.displayName, this.fromAddress, this.subject, this.textBody, this.htmlBody, this.headers);
    }

    @Override
    public String toString() {
        return "UpdateAutoReplyTemplateRequest[" + "displayName=" + this.displayName + ", " + "fromAddress=" + this.fromAddress + ", " + "subject=" + this.subject + ", " + "textBody=" + this.textBody + ", " + "htmlBody=" + this.htmlBody + ", " + "headers=" + this.headers + "]";
    }

    /**
     * Creates a new UpdateAutoReplyTemplateRequest.
     *
     * @param displayName human-readable template name
     * @param fromAddress From address used for replies
     * @param subject template subject
     * @param textBody plain-text body; may be null
     * @param htmlBody HTML body; may be null
     * @param headers additional template headers
     */
    @JsonCreator
    public UpdateAutoReplyTemplateRequest(String displayName, String fromAddress, String subject, String textBody, String htmlBody, List<AutoReplyTemplateHeader> headers) {
        this.displayName = displayName;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.textBody = textBody;
        this.htmlBody = htmlBody;
        this.headers = headers;
    }

    /**
     * Creates a new request builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builds {@link UpdateAutoReplyTemplateRequest} instances. */
    public static final class Builder {
        private String displayName;
        private String fromAddress;
        private String subject;
        private String textBody;
        private String htmlBody;
        private List<AutoReplyTemplateHeader> headers;

        /**
         * Sets the human-readable template name.
         *
         * @param displayName human-readable template name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the From address used for replies.
         *
         * @param fromAddress From address used for replies
         * @return this builder
         */
        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        /**
         * Sets the template subject.
         *
         * @param subject template subject
         * @return this builder
         */
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the plain-text body.
         *
         * @param textBody plain-text body; may be null
         * @return this builder
         */
        public Builder textBody(String textBody) {
            this.textBody = textBody;
            return this;
        }

        /**
         * Sets the HTML body.
         *
         * @param htmlBody HTML body; may be null
         * @return this builder
         */
        public Builder htmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
            return this;
        }

        /**
         * Sets the additional template headers.
         *
         * @param headers additional template headers
         * @return this builder
         */
        public Builder headers(List<AutoReplyTemplateHeader> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the update request
         * @throws IllegalArgumentException if a field fails validation
         */
        public UpdateAutoReplyTemplateRequest build() {
            if (displayName == null || Java8.isBlank(displayName)) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (fromAddress == null || Java8.isBlank(fromAddress)) {
                throw new IllegalArgumentException("from_address is required");
            }
            if (fromAddress.codePointCount(0, fromAddress.length()) > 320) {
                throw new IllegalArgumentException("from_address must be 320 characters or fewer");
            }
            if (fromAddress.indexOf('@') < 0 || fromAddress.indexOf('\r') >= 0 || fromAddress.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("from_address must be a valid mailbox");
            }
            if (subject == null || Java8.isBlank(subject)) {
                throw new IllegalArgumentException("subject is required");
            }
            if (subject.codePointCount(0, subject.length()) > 1_048_576) {
                throw new IllegalArgumentException("subject must be 1048576 characters or fewer");
            }
            if (subject.indexOf('\r') >= 0 || subject.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("subject must not contain line breaks");
            }
            if (textBody != null && textBody.codePointCount(0, textBody.length()) > 1_048_576) {
                throw new IllegalArgumentException("text_body must be 1048576 characters or fewer");
            }
            if (htmlBody != null && htmlBody.codePointCount(0, htmlBody.length()) > 1_048_576) {
                throw new IllegalArgumentException("html_body must be 1048576 characters or fewer");
            }
            if ((textBody == null || Java8.isBlank(textBody)) && (htmlBody == null || Java8.isBlank(htmlBody))) {
                throw new IllegalArgumentException("text_body or html_body is required");
            }
            AutoReplyTemplateHeader.validateList(headers, true);
            return new UpdateAutoReplyTemplateRequest(displayName, fromAddress, subject, textBody, htmlBody, headers);
        }
    }
}
