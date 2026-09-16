package com.mxraven.mail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import com.mxraven.mail.model.Recipient;

/**
 * The outcome of a single {@code RCPT TO} command during a send operation.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientResult {
    private final Recipient recipient;
    private final boolean accepted;
    private final String status;

    /** the recipient the server was asked to accept */
    public Recipient recipient() {
        return recipient;
    }

    /** whether the server accepted the recipient */
    public boolean accepted() {
        return accepted;
    }

    /** the server reply text for the recipient */
    public String status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientResult that = (RecipientResult) o;
        return Objects.equals(this.recipient, that.recipient)
                && this.accepted == that.accepted
                && Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.recipient, this.accepted, this.status);
    }

    @Override
    public String toString() {
        return "RecipientResult[" + "recipient=" + this.recipient + ", " + "accepted=" + this.accepted + ", " + "status=" + this.status + "]";
    }

    /**
     * Creates a new RecipientResult.
     *
     * @param recipient the recipient the server was asked to accept
     * @param accepted whether the server accepted the recipient
     * @param status the server reply text for the recipient
     */
    @JsonCreator
    public RecipientResult(Recipient recipient, boolean accepted, String status) {
        this.recipient = recipient;
        this.accepted = accepted;
        this.status = status;
    }
}
