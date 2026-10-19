package com.mxraven.mail.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvelopeTest {

    @Test
    void acceptsAnAddressStringForTheSender() {
        Envelope fromString = Envelope.builder()
                .from("sender@example.com")
                .build();
        Envelope fromPath = Envelope.builder()
                .from(Path.of("sender@example.com"))
                .build();

        assertEquals(fromPath.from(), fromString.from());
        assertEquals("sender", fromString.from().mailbox().localPart());
        assertEquals("example.com", fromString.from().mailbox().domain());
    }

    @Test
    void aBlankSenderAddressYieldsTheNullPath() {
        Envelope envelope = Envelope.builder()
                .from("")
                .build();

        assertTrue(envelope.from().isNull());
    }
}
