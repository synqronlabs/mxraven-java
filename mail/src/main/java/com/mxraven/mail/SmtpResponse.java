package com.mxraven.mail;

public record SmtpResponse(int code, String message) {
    public boolean isSuccess() {
        return code >= 200 && code < 400;
    }

    public boolean isError() {
        return code >= 400;
    }
}
