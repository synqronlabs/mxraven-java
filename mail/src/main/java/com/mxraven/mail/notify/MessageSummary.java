package com.mxraven.mail.notify;

import com.mxraven.mail.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;

/**
 * A summary of the parsed RFC 5322 headers of an accepted message.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MessageSummary {
    private final String subject;
    private final List<String> from;
    private final List<String> to;
    private final List<String> cc;
    private final String messageId;
    private final String date;

    /** the decoded Subject header */
    public String subject() {
        return subject;
    }

    /** the decoded From mailboxes */
    public List<String> from() {
        return from;
    }

    /** the decoded To mailboxes */
    public List<String> to() {
        return to;
    }

    /** the decoded Cc mailboxes */
    public List<String> cc() {
        return cc;
    }

    /** the Message-ID header */
    public String messageId() {
        return messageId;
    }

    public String date() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MessageSummary that = (MessageSummary) o;
        return Objects.equals(this.subject, that.subject) && Objects.equals(this.from, that.from) && Objects.equals(this.to, that.to) && Objects.equals(this.cc, that.cc) && Objects.equals(this.messageId, that.messageId) && Objects.equals(this.date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subject, this.from, this.to, this.cc, this.messageId, this.date);
    }

    @Override
    public String toString() {
        return "MessageSummary[" + "subject=" + this.subject + ", " + "from=" + this.from + ", " + "to=" + this.to + ", " + "cc=" + this.cc + ", " + "messageId=" + this.messageId + ", " + "date=" + this.date + "]";
    }

    /**
     * Creates a new MessageSummary.
     *
     * @param subject the decoded Subject header
     * @param from the decoded From mailboxes
     * @param to the decoded To mailboxes
     * @param cc the decoded Cc mailboxes
     * @param messageId the Message-ID header
     * @param date date
     */
    @JsonCreator
    public MessageSummary(String subject, List<String> from, List<String> to, List<String> cc, String messageId, String date) {
        this.subject = subject;
        this.messageId = messageId;
        this.date = date;
        this.from = from == null ? Java8.list() : Java8.copyList(from);
        this.to = to == null ? Java8.list() : Java8.copyList(to);
        this.cc = cc == null ? Java8.list() : Java8.copyList(cc);
    }

    /**
         * Creates a summary, copying every address list.
         */
}
