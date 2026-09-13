package com.mxraven.mail.model;

import java.time.Instant;
import java.util.List;

public record Mail(Envelope envelope, Content content, List<TraceField> trace, Instant receivedAt) {
    public Mail {
        trace = trace == null ? List.of() : List.copyOf(trace);
        receivedAt = receivedAt == null ? Instant.now() : receivedAt;
    }
}
