package com.mxraven.mail;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * The outcome of an SMTP send operation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SendResult {
    private final boolean success;
    private final List<RecipientResult> recipients;
    private final String message;

    /** whether the sender and message content were accepted */
    public boolean success() {
        return success;
    }

    /** the per-recipient results, in the order the recipients were attempted */
    public List<RecipientResult> recipients() {
        return recipients;
    }

    /** the server reply from the DATA phase */
    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendResult that = (SendResult) o;
        return this.success == that.success
                && Objects.equals(this.recipients, that.recipients)
                && Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.success, this.recipients, this.message);
    }

    @Override
    public String toString() {
        return "SendResult[" + "success=" + this.success + ", " + "recipients=" + this.recipients + ", " + "message=" + this.message + "]";
    }

    /**
     * Creates a send result, copying the recipient results.
     */
    @JsonCreator
    public SendResult(boolean success, List<RecipientResult> recipients, String message) {

        recipients = recipients == null ? Java8.list() : Java8.copyList(recipients);
    
        this.success = success;
        this.recipients = recipients;
        this.message = message;
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
        while (value.startsWith("<")) {
            value = value.substring(1);
        }
        int end = value.length();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '>' || c == ';' || c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                end = i;
                break;
            }
        }
        return value.substring(0, end);
    }
}
