package com.mxraven.mail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmtpResponseTest {

    @Test
    void classifiesSuccessResponses() {
        assertTrue(new SmtpResponse(200, "ok").isSuccess());
        assertTrue(new SmtpResponse(250, "ok").isSuccess());
        assertFalse(new SmtpResponse(421, "unavailable").isSuccess());
        assertFalse(new SmtpResponse(550, "no such user").isSuccess());
    }

    @Test
    void intermediateRepliesArePositiveButNotSuccess() {
        SmtpResponse data = new SmtpResponse(354, "go ahead");
        assertTrue(data.isPositive());
        assertFalse(data.isSuccess());
        assertTrue(new SmtpResponse(334, "challenge").isPositive());
        assertFalse(new SmtpResponse(334, "challenge").isSuccess());
    }

    @Test
    void classifiesErrorResponses() {
        assertFalse(new SmtpResponse(250, "ok").isError());
        assertTrue(new SmtpResponse(421, "unavailable").isError());
        assertTrue(new SmtpResponse(550, "no such user").isError());
    }

    @Test
    void extractsEnhancedStatusCodes() {
        SmtpResponse response = new SmtpResponse(550, "5.1.1 no such user");
        assertTrue(response.enhancedStatus().isPresent());
        assertEquals("5.1.1", response.enhancedStatus().orElseThrow().code());
        assertEquals(5, response.enhancedStatus().orElseThrow().statusClass());
        assertTrue(response.enhancedStatus().orElseThrow().isError());
        assertTrue(new SmtpResponse(250, "2.0.0 ok").enhancedStatus().orElseThrow().isSuccess());
        assertTrue(new SmtpResponse(250, "ok").enhancedStatus().isEmpty());
    }
}
