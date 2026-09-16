package com.mxraven.mail.model;

import com.mxraven.mail.mime.ContentTransferEncoding;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailBuilderTest {

    @Test
    void buildsHeadersAndEnvelopeFromRecipientLists() {
        Mail mail = MailBuilder.create()
                .from("sender@example.com")
                .sender("bounce@example.com")
                .to("to@example.com")
                .cc("cc@example.com")
                .bcc("bcc@example.com")
                .subject("Subject")
                .build();

        Headers headers = mail.content().headers();
        assertEquals("sender@example.com", headers.first("From").orElseThrow());
        assertEquals("bounce@example.com", headers.first("Sender").orElseThrow());
        assertEquals("to@example.com", headers.first("To").orElseThrow());
        assertEquals("cc@example.com", headers.first("Cc").orElseThrow());
        assertEquals("Subject", headers.first("Subject").orElseThrow());
        assertTrue(headers.first("Bcc").isEmpty());

        Envelope envelope = mail.envelope();
        assertEquals("sender@example.com", envelope.from().mailbox().toString());
        assertEquals(3, envelope.to().size());
        assertTrue(envelope.size() > 0, "size is derived from the serialized message");
        assertTrue(mail.trace().isEmpty());
        assertNotNull(mail.receivedAt());
    }

    @Test
    void textBodySetsContentTypeEncodingAndBytes() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .textBody("hello")
                .build();

        Content content = mail.content();
        assertEquals("text/plain; charset=utf-8", content.headers().first("Content-Type").orElseThrow());
        assertEquals(ContentTransferEncoding.SEVEN_BIT, content.encoding());
        assertEquals("hello", new String(content.body(), StandardCharsets.UTF_8));
    }

    @Test
    void htmlBodySetsHtmlContentType() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .htmlBody("<p>hi</p>")
                .build();

        assertEquals("text/html; charset=utf-8",
                mail.content().headers().first("Content-Type").orElseThrow());
    }

    @Test
    void nullSenderOmitsTheFromHeader() {
        Mail mail = MailBuilder.create()
                .nullSender()
                .to("c@d.com")
                .build();

        assertTrue(mail.envelope().from().isNull());
        assertTrue(mail.content().headers().first("From").isEmpty());
    }

    @Test
    void customHeadersMessageIdAndDateArePreserved() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .header("X-Custom", "value")
                .messageId("id@example.com")
                .date(Instant.parse("2024-01-02T03:04:05Z"))
                .build();

        Headers headers = mail.content().headers();
        assertEquals("value", headers.first("X-Custom").orElseThrow());
        assertEquals("<id@example.com>", headers.first("Message-ID").orElseThrow());
        assertTrue(headers.first("Date").isPresent());
    }

    @Test
    void explicitBodyKeepsTheSuppliedEncoding() {
        byte[] data = Base64.getEncoder().encode("hello".getBytes(StandardCharsets.UTF_8));
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .body(data, "text/plain", ContentTransferEncoding.BASE64)
                .build();

        assertEquals(ContentTransferEncoding.BASE64, mail.content().encoding());
    }

    @Test
    void nonAsciiEnvelopeAddressRequestsSmtpUtf8() {
        Mail mail = MailBuilder.create()
                .from("s\u00e9nder@example.com")
                .to("c@d.com")
                .textBody("x")
                .build();

        assertTrue(mail.envelope().smtpUtf8());
        assertFalse(MailBuilder.create().from("a@b.com").to("c@d.com").textBody("x").build()
                .envelope().smtpUtf8());
    }

    @Test
    void eightBitBodyRequestsTheMatchingBodyType() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .body("caf\u00e9".getBytes(StandardCharsets.UTF_8), "text/plain",
                        ContentTransferEncoding.EIGHT_BIT)
                .build();

        assertEquals(BodyType.EIGHT_BIT_MIME, mail.envelope().bodyType());
    }

    @Test
    void quotesDisplayNamesContainingSpecials() {
        Mail mail = MailBuilder.create()
                .from(new MailboxAddress("a", "b.com", "Doe, Jane"))
                .to("c@d.com")
                .build();

        assertEquals("\"Doe, Jane\" <a@b.com>",
                mail.content().headers().first("From").orElseThrow());
    }

    @Test
    void rejectsHeaderValuesThatCouldInjectFields() {
        assertThrows(IllegalArgumentException.class, () -> MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .header("X-Test", "value\r\nBcc: evil@example.com"));
    }

    @Test
    void multipleRecipientsAreJoinedWithCommas() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("x@example.com", "y@example.com")
                .build();

        assertEquals("x@example.com, y@example.com",
                mail.content().headers().first("To").orElseThrow());
        assertEquals(2, mail.envelope().to().size());
    }

    @Test
    void messageIdentifiersAreAngleBracketed() {
        Mail mail = MailBuilder.create()
                .from("a@b.com")
                .to("c@d.com")
                .messageId("id@example.com")
                .inReplyTo("parent@example.com")
                .references("one@example.com", "<two@example.com>")
                .build();

        Headers headers = mail.content().headers();
        assertEquals("<id@example.com>", headers.first("Message-ID").orElseThrow());
        assertEquals("<parent@example.com>", headers.first("In-Reply-To").orElseThrow());
        assertEquals("<one@example.com> <two@example.com>", headers.first("References").orElseThrow());
    }
}
