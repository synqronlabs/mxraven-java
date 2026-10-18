package com.mxraven.mail.notify;

/** The value a notification handler returns. */
public enum HandlerResult {
    /** Acknowledges the current event and continues with the next one. */
    CONTINUE,
    /** Acknowledges the current event and stops consumption. */
    STOP
}
