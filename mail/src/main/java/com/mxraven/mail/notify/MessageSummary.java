package com.mxraven.mail.notify;

import java.util.List;

/**
 * A summary of the parsed RFC 5322 headers of an accepted message.
 *
 * @param subject the decoded Subject header
 * @param from the decoded From mailboxes
 * @param to the decoded To mailboxes
 * @param cc the decoded Cc mailboxes
 * @param messageId the Message-ID header
 * @param date the raw Date header
 */
public record MessageSummary(
        String subject,
        List<String> from,
        List<String> to,
        List<String> cc,
        String messageId,
        String date) {

    /**
     * Creates a summary, copying every address list.
     */
    public MessageSummary {
        from = from == null ? List.of() : List.copyOf(from);
        to = to == null ? List.of() : List.copyOf(to);
        cc = cc == null ? List.of() : List.copyOf(cc);
    }
}
