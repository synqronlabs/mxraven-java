package com.mxraven.mail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmtpResponseTest {

    @Test
    void classifiesSuccessResponses() {
        assertTrue(new SmtpResponse(200, "ok").isSuccess());
        assertTrue(new SmtpResponse(250, "ok").isSuccess());
        assertTrue(new SmtpResponse(354, "go ahead").isSuccess());
        assertFalse(new SmtpResponse(421, "unavailable").isSuccess());
    }

    @Test
    void classifiesErrorResponses() {
        assertFalse(new SmtpResponse(250, "ok").isError());
        assertTrue(new SmtpResponse(421, "unavailable").isError());
        assertTrue(new SmtpResponse(550, "no such user").isError());
    }
}
