package com.mxraven.mail.webhook;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * The SMTP envelope of a message.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Envelope {
    private final String mailFrom;
    private final List<String> rcptTo;

    /** the envelope sender; empty for a null reverse-path */
    public String mailFrom() {
        return mailFrom;
    }

    /** the envelope recipients */
    public List<String> rcptTo() {
        return rcptTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Envelope that = (Envelope) o;
        return Objects.equals(this.mailFrom, that.mailFrom)
                && Objects.equals(this.rcptTo, that.rcptTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mailFrom, this.rcptTo);
    }

    @Override
    public String toString() {
        return "Envelope[" + "mailFrom=" + this.mailFrom + ", " + "rcptTo=" + this.rcptTo + "]";
    }

    /**
     * Creates an envelope, copying the recipient list.
     */
    @JsonCreator
    public Envelope(String mailFrom, List<String> rcptTo) {

        rcptTo = rcptTo == null ? Java8.list() : Java8.copyList(rcptTo);
    
        this.mailFrom = mailFrom;
        this.rcptTo = rcptTo;
    }
}
