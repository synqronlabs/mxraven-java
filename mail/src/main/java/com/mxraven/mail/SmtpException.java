package com.mxraven.mail;

import com.mxraven.mail.exception.MxRavenException;

public class SmtpException extends MxRavenException {
    public SmtpException(String message) {
        super(message);
    }

    public SmtpException(String message, Throwable cause) {
        super(message, cause);
    }
}
