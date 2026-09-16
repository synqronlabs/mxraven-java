package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.time.Instant;
import java.util.List;

/**
 * A complete message: its envelope, content, trace fields, and receipt time.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Mail {
    private final Envelope envelope;
    private final Content content;
    private final List<TraceField> trace;
    private final Instant receivedAt;

    /** the SMTP envelope */
    public Envelope envelope() {
        return envelope;
    }

    /** the message content */
    public Content content() {
        return content;
    }

    /** the trace fields; a {@code null} value is replaced with an empty list */
    public List<TraceField> trace() {
        return trace;
    }

    /** the receipt time; a {@code null} value is replaced with the current instant */
    public Instant receivedAt() {
        return receivedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mail that = (Mail) o;
        return Objects.equals(this.envelope, that.envelope)
                && Objects.equals(this.content, that.content)
                && Objects.equals(this.trace, that.trace)
                && Objects.equals(this.receivedAt, that.receivedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.envelope, this.content, this.trace, this.receivedAt);
    }

    @Override
    public String toString() {
        return "Mail[" + "envelope=" + this.envelope + ", " + "content=" + this.content + ", " + "trace=" + this.trace + ", " + "receivedAt=" + this.receivedAt + "]";
    }

    /**
     * Creates a message, copying the trace fields and defaulting the receipt time.
     */
    @JsonCreator
    public Mail(Envelope envelope, Content content, List<TraceField> trace, Instant receivedAt) {

        trace = trace == null ? Java8.list() : Java8.copyList(trace);
        receivedAt = receivedAt == null ? Instant.now() : receivedAt;
    
        this.envelope = envelope;
        this.content = content;
        this.trace = trace;
        this.receivedAt = receivedAt;
    }
}
