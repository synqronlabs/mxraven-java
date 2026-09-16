package com.mxraven.mail;

/**
 * An SMTP server reply consisting of a status code and message text.
 *
 * @param code the three-digit SMTP reply code
 * @param message the reply text, stripped of the code prefix and line breaks
 */
public record SmtpResponse(int code, String message) {
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
