package com.mxraven.mail;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An SMTP server reply consisting of a status code and message text.
 *
 * @param code the three-digit SMTP reply code
 * @param message the reply text, stripped of the code prefix and line breaks
 */
public record SmtpResponse(int code, String message) {
    private static final Pattern ENHANCED_STATUS =
            Pattern.compile("(?:^|[\\s;])([245])\\.(\\d{1,3})\\.(\\d{1,3})(?=[\\s;]|$)");

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
