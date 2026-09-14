package com.mxraven.mail;

import java.util.List;

public record SendResult(boolean success, List<RecipientResult> recipients, String message) {
    public SendResult {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
    }

    /**
     * Extracts the server-assigned message reference from the DATA response, if
     * present (mxRaven responses carry it as {@code message_ref=<uuid>}).
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
