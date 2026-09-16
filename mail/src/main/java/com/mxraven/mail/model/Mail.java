package com.mxraven.mail.model;

import java.time.Instant;
import java.util.List;

/**
 * A complete message: its envelope, content, trace fields, and receipt time.
 *
 * @param envelope   the SMTP envelope
 * @param content    the message content
 * @param trace      the trace fields; a {@code null} value is replaced with an
 *                   empty list
 * @param receivedAt the receipt time; a {@code null} value is replaced with the
 *                   current instant
 */
public record Mail(Envelope envelope, Content content, List<TraceField> trace, Instant receivedAt) {
    /**
     * Creates a message, copying the trace fields and defaulting the receipt time.
     */
    public Mail {
        trace = trace == null ? List.of() : List.copyOf(trace);
        receivedAt = receivedAt == null ? Instant.now() : receivedAt;
    }
}
