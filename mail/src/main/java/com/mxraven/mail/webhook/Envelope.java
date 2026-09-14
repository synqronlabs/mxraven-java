package com.mxraven.mail.webhook;

import java.util.List;

/**
 * The SMTP envelope of a message.
 *
 * @param mailFrom the envelope sender; empty for a null reverse-path
 * @param rcptTo   the envelope recipients
 */
public record Envelope(String mailFrom, List<String> rcptTo) {
    public Envelope {
        rcptTo = rcptTo == null ? List.of() : List.copyOf(rcptTo);
    }
}
