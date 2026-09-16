package com.mxraven.mail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * An SMTP server reply consisting of a status code and message text.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SmtpResponse {
    private final int code;
    private final String message;

    /** the three-digit SMTP reply code */
    public int code() {
        return code;
    }

    /** the reply text, stripped of the code prefix and line breaks */
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
        SmtpResponse that = (SmtpResponse) o;
        return this.code == that.code
                && Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.message);
    }

    @Override
    public String toString() {
        return "SmtpResponse[" + "code=" + this.code + ", " + "message=" + this.message + "]";
    }

    /**
     * Creates a new SmtpResponse.
     *
     * @param code the three-digit SMTP reply code
     * @param message the reply text, stripped of the code prefix and line breaks
     */
    @JsonCreator
    public SmtpResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Reports whether the reply code is in the success range.
     *
     * @return {@code true} when the code is at least 200 and below 400
     */
    public boolean isSuccess() {
        return code >= 200 && code < 400;
    }

    /**
     * Reports whether the reply code represents an error.
     *
     * @return {@code true} when the code is at least 400
     */
    public boolean isError() {
        return code >= 400;
    }
}
