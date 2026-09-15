package com.mxraven.admin.model;

import java.util.regex.Pattern;

import java.util.List;

/**
 * Request body for {@code POST /v2/tenants/{slug}/auto-reply-templates}.
 */
public record CreateAutoReplyTemplateRequest(
        String templateRef,
        String displayName,
        String fromAddress,
        String subject,
        String textBody,
        String htmlBody,
        List<AutoReplyTemplateHeader> headers) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String templateRef;
        private String displayName;
        private String fromAddress;
        private String subject;
        private String textBody;
        private String htmlBody;
        private List<AutoReplyTemplateHeader> headers;

        public Builder templateRef(String templateRef) {
            this.templateRef = templateRef;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder textBody(String textBody) {
            this.textBody = textBody;
            return this;
        }

        public Builder htmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
            return this;
        }

        public Builder headers(List<AutoReplyTemplateHeader> headers) {
            this.headers = headers;
            return this;
        }

        private static final Pattern REFERENCE = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9._-]*$");

        public CreateAutoReplyTemplateRequest build() {
            if (templateRef == null || templateRef.isBlank()) {
                throw new IllegalArgumentException("template_ref is required");
            }
            if (templateRef.codePointCount(0, templateRef.length()) > 100) {
                throw new IllegalArgumentException("template_ref must be 100 characters or fewer");
            }
            if (!REFERENCE.matcher(templateRef).matches()) {
                throw new IllegalArgumentException("template_ref must start with a letter or digit and may contain "
                        + "letters, digits, dots, underscores, or hyphens");
            }
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException("display_name is required");
            }
            if (displayName.codePointCount(0, displayName.length()) > 255) {
                throw new IllegalArgumentException("display_name must be 255 characters or fewer");
            }
            if (fromAddress == null || fromAddress.isBlank()) {
                throw new IllegalArgumentException("from_address is required");
            }
            if (fromAddress.codePointCount(0, fromAddress.length()) > 320) {
                throw new IllegalArgumentException("from_address must be 320 characters or fewer");
            }
            if (fromAddress.indexOf('@') < 0 || fromAddress.indexOf('\r') >= 0 || fromAddress.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("from_address must be a valid mailbox");
            }
            if (subject == null || subject.isBlank()) {
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
            if ((textBody == null || textBody.isBlank()) && (htmlBody == null || htmlBody.isBlank())) {
                throw new IllegalArgumentException("text_body or html_body is required");
            }
            AutoReplyTemplateHeader.validateList(headers, false);
            return new CreateAutoReplyTemplateRequest(templateRef, displayName, fromAddress, subject, textBody, htmlBody, headers);
        }
    }
}
