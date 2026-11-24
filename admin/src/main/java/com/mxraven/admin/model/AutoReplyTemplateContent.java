package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * Stored auto-reply template content as the listener renders it.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AutoReplyTemplateContent {
    private final String subject;
    private final String textBody;
    private final String htmlBody;
    private final List<AutoReplyTemplateHeader> headers;

    /** message subject */
    public String subject() {
        return subject;
    }

    /** plain-text body, when set */
    public String textBody() {
        return textBody;
    }

    /** stored HTML body after the authoring-time content policy */
    public String htmlBody() {
        return htmlBody;
    }

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
        AutoReplyTemplateContent that = (AutoReplyTemplateContent) o;
        return Objects.equals(this.subject, that.subject) && Objects.equals(this.textBody, that.textBody) && Objects.equals(this.htmlBody, that.htmlBody) && Objects.equals(this.headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subject, this.textBody, this.htmlBody, this.headers);
    }

    @Override
    public String toString() {
        return "AutoReplyTemplateContent[" + "subject=" + this.subject + ", " + "textBody=" + this.textBody + ", " + "htmlBody=" + this.htmlBody + ", " + "headers=" + this.headers + "]";
    }

    /**
     * Creates a new AutoReplyTemplateContent.
     *
     * @param subject message subject
     * @param textBody plain-text body, when set
     * @param htmlBody stored HTML body after the authoring-time content policy
     * @param headers headers
     */
    @JsonCreator
    public AutoReplyTemplateContent(String subject, String textBody, String htmlBody, List<AutoReplyTemplateHeader> headers) {
        this.subject = subject;
        this.textBody = textBody;
        this.htmlBody = htmlBody;
        this.headers = headers == null ? Java8.list() : Java8.copyList(headers);
    }

    /**
         * Creates content, copying the header list.
         */
}
