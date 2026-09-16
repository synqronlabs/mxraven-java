package com.mxraven.mail;

import java.util.List;

/**
 * The outcome of an SMTP send operation.
 *
 * @param success whether the sender and message content were accepted
 * @param recipients the per-recipient results, in the order the recipients were attempted
 * @param message the server reply from the DATA phase
 */
public record SendResult(boolean success, List<RecipientResult> recipients, String message) {
    /**
     * Creates a send result, copying the recipient results.
     */
    public SendResult {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
    }

    /**
     * Extracts the server-assigned message reference from the DATA response, if
     * present (mxRaven responses carry it as {@code message_ref=<uuid>}).
     *
     * @return the reference value, or {@code null} when absent
     */
    public String messageRef() {
        if (message == null) {
            return null;
        }
        int index = message.indexOf("message_ref=");
        if (index < 0) {
            return null;
        }
        String value = message.substring(index + "message_ref=".length()).trim();
        int end = value.indexOf(' ');
        return end < 0 ? value : value.substring(0, end);
    }
}
