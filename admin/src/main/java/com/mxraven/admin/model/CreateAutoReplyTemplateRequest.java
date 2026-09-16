package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.regex.Pattern;

import java.util.List;

/**
 * Request body for <code>POST /v2/tenants/{slug}/auto-reply-templates</code>.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CreateAutoReplyTemplateRequest {
    private final String templateRef;
    private final String displayName;
    private final String fromAddress;
    private final String subject;
    private final String textBody;
    private final String htmlBody;
    private final List<AutoReplyTemplateHeader> headers;

    /** immutable template reference */
    public String templateRef() {
        return templateRef;
    }

    /** human-readable template name */
    public String displayName() {
        return displayName;
    }

    /** sender address used for replies */
    public String fromAddress() {
        return fromAddress;
    }

    /** reply subject line */
    public String subject() {
        return subject;
    }

    /** optional plain-text body */
    public String textBody() {
        return textBody;
    }

    /** optional HTML body */
    public String htmlBody() {
        return htmlBody;
    }

    /** optional custom headers */
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
        CreateAutoReplyTemplateRequest that = (CreateAutoReplyTemplateRequest) o;
        return Objects.equals(this.templateRef, that.templateRef)
                && Objects.equals(this.displayName, that.displayName)
                && Objects.equals(this.fromAddress, that.fromAddress)
                && Objects.equals(this.subject, that.subject)
                && Objects.equals(this.textBody, that.textBody)
                && Objects.equals(this.htmlBody, that.htmlBody)
                && Objects.equals(this.headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.templateRef, this.displayName, this.fromAddress, this.subject, this.textBody, this.htmlBody, this.headers);
    }

    @Override
    public String toString() {
        return "CreateAutoReplyTemplateRequest[" + "templateRef=" + this.templateRef + ", " + "displayName=" + this.displayName + ", " + "fromAddress=" + this.fromAddress + ", " + "subject=" + this.subject + ", " + "textBody=" + this.textBody + ", " + "htmlBody=" + this.htmlBody + ", " + "headers=" + this.headers + "]";
    }

    /**
     * Creates a new CreateAutoReplyTemplateRequest.
     *
     * @param templateRef immutable template reference
     * @param displayName human-readable template name
     * @param fromAddress sender address used for replies
     * @param subject reply subject line
     * @param textBody optional plain-text body
     * @param htmlBody optional HTML body
     * @param headers optional custom headers
     */
    @JsonCreator
    public CreateAutoReplyTemplateRequest(String templateRef, String displayName, String fromAddress, String subject, String textBody, String htmlBody, List<AutoReplyTemplateHeader> headers) {
        this.templateRef = templateRef;
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

    /** Builds a {@link CreateAutoReplyTemplateRequest}. */
    public static final class Builder {
        private String templateRef;
        private String displayName;
        private String fromAddress;
        private String subject;
        private String textBody;
        private String htmlBody;
        private List<AutoReplyTemplateHeader> headers;

        /**
         * Sets the immutable template reference.
         *
         * @param templateRef template reference
         * @return this builder
         */
        public Builder templateRef(String templateRef) {
            this.templateRef = templateRef;
            return this;
        }

        /**
         * Sets the human-readable template name.
         *
         * @param displayName template name
         * @return this builder
         */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /**
         * Sets the sender address used for replies.
         *
         * @param fromAddress sender address
         * @return this builder
         */
        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        /**
         * Sets the reply subject line.
         *
         * @param subject subject line
         * @return this builder
         */
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the plain-text body.
         *
         * @param textBody plain-text body
         * @return this builder
         */
        public Builder textBody(String textBody) {
            this.textBody = textBody;
            return this;
        }

        /**
         * Sets the HTML body.
         *
         * @param htmlBody HTML body
         * @return this builder
         */
        public Builder htmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
            return this;
        }

        /**
         * Sets the custom headers.
         *
         * @param headers custom headers
         * @return this builder
         */
        public Builder headers(List<AutoReplyTemplateHeader> headers) {
            this.headers = headers;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        /**
         * Builds the request.
         *
         * @return new request
         * @throws IllegalArgumentException when a field is missing or invalid
         */
        public CreateAutoReplyTemplateRequest build() {
            if (templateRef == null || Java8.isBlank(templateRef)) {
                throw new IllegalArgumentException("template_ref is required");
            }
            if (templateRef.codePointCount(0, templateRef.length()) > 100) {
                throw new IllegalArgumentException("template_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(templateRef).matches()) {
                throw new IllegalArgumentException("template_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
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
            AutoReplyTemplateHeader.validateList(headers, false);
            return new CreateAutoReplyTemplateRequest(templateRef, displayName, fromAddress, subject, textBody, htmlBody, headers);
        }
    }
}
