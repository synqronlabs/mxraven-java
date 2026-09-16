package com.mxraven.mail.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MailboxAddressTest {

    @Test
    void splitsOnTheLastAtSign() {
        MailboxAddress address = MailboxAddress.of("alice@example.com");
        assertEquals("alice", address.localPart());
        assertEquals("example.com", address.domain());
        assertEquals("alice@example.com", address.toString());

        MailboxAddress nested = MailboxAddress.of("a@b@example.com");
        assertEquals("a@b", nested.localPart());
        assertEquals("example.com", nested.domain());
    }

    @Test
    void acceptsBlankInputButRejectsAnAddressWithoutADomain() {
        MailboxAddress blank = MailboxAddress.of("  ");
        assertEquals("", blank.localPart());
        assertEquals("", blank.domain());
        assertEquals("", blank.toString());

        MailboxAddress missing = MailboxAddress.of((String) null);
        assertEquals("", missing.toString());

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> MailboxAddress.of("postmaster"));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> MailboxAddress.of("not an address"));
    }

    @Test
    void rejectsHeaderInjection() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> MailboxAddress.of("a@b.com\r\nBcc: evil@example.com"));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new MailboxAddress("a", "b.com", "Name\r\nBcc: evil@example.com"));
    }

    @Test
    void exposesTheExplicitConstructor() {
        MailboxAddress address = MailboxAddress.of("bob", "example.org");
        assertEquals("bob@example.org", address.toString());
        assertNull(address.displayName());
    }

    @Test
    void parsesDisplayNameAngleAddressForm() {
        MailboxAddress address = MailboxAddress.of("Sender Name <sender@example.com>");
        assertEquals("sender", address.localPart());
        assertEquals("example.com", address.domain());
        assertEquals("Sender Name", address.displayName());
        assertEquals("sender@example.com", address.toString());

        MailboxAddress quoted = MailboxAddress.of("\"Doe, Jane\" <jane@example.com>");
        assertEquals("Doe, Jane", quoted.displayName());
        assertEquals("jane@example.com", quoted.toString());
    }
}
