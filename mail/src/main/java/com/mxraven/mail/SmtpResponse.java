package com.mxraven.mail;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An SMTP server reply consisting of a status code and message text.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SmtpResponse {
    private static final Pattern ENHANCED_STATUS =
            Pattern.compile("(?:^|[\\s;])([245])\\.(\\d{1,3})\\.(\\d{1,3})(?=[\\s;]|$)");

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
     * Reports whether the reply code is a positive completion or intermediate
     * reply. This is a 2xx or 3xx code.
     *
     * @return {@code true} when the code is at least 200 and below 400
     */
    public boolean isPositive() {
        return code >= 200 && code < 400;
    }

    /**
     * Reports whether the reply code is a positive completion (2xx). A 3xx
     * intermediate reply, such as the {@code 354} to {@code DATA} or a {@code 334}
     * authentication challenge, is not a success.
     *
     * @return {@code true} when the code is at least 200 and below 300
     */
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    /**
     * Reports whether the reply code represents an error.
     *
     * @return {@code true} when the code is at least 400
     */
    public boolean isError() {
        return code >= 400;
    }

    /**
     * Extracts the RFC 3463 enhanced status code from the reply text, when the
     * server supplied one (for example {@code "250 2.1.5 Ok"}).
     *
     * @return the enhanced status, or empty when the reply has none
     */
    public Optional<EnhancedStatus> enhancedStatus() {
        if (message == null) {
            return Optional.empty();
        }
        Matcher matcher = ENHANCED_STATUS.matcher(message);
        if (!matcher.find()) {
            return Optional.empty();
        }
        return Optional.of(new EnhancedStatus(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3))));
    }
}
