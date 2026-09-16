package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Masked summary of an auto-reply template's content.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class AutoReplyTemplateContentSummary {
    private final MaskedTemplateField subject;
    private final MaskedTemplateField textBody;
    private final MaskedTemplateField htmlBody;
    private final int headerCount;

    /** masked subject field */
    public MaskedTemplateField subject() {
        return subject;
    }

    /** masked plain-text body field */
    public MaskedTemplateField textBody() {
        return textBody;
    }

    /** masked HTML body field */
    public MaskedTemplateField htmlBody() {
        return htmlBody;
    }

    /** number of custom headers */
    public int headerCount() {
        return headerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AutoReplyTemplateContentSummary that = (AutoReplyTemplateContentSummary) o;
        return Objects.equals(this.subject, that.subject)
                && Objects.equals(this.textBody, that.textBody)
                && Objects.equals(this.htmlBody, that.htmlBody)
                && this.headerCount == that.headerCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subject, this.textBody, this.htmlBody, this.headerCount);
    }

    @Override
    public String toString() {
        return "AutoReplyTemplateContentSummary[" + "subject=" + this.subject + ", " + "textBody=" + this.textBody + ", " + "htmlBody=" + this.htmlBody + ", " + "headerCount=" + this.headerCount + "]";
    }

    /**
     * Creates a new AutoReplyTemplateContentSummary.
     *
     * @param subject masked subject field
     * @param textBody masked plain-text body field
     * @param htmlBody masked HTML body field
     * @param headerCount number of custom headers
     */
    @JsonCreator
    public AutoReplyTemplateContentSummary(MaskedTemplateField subject, MaskedTemplateField textBody, MaskedTemplateField htmlBody, int headerCount) {
        this.subject = subject;
        this.textBody = textBody;
        this.htmlBody = htmlBody;
        this.headerCount = headerCount;
    }
}
