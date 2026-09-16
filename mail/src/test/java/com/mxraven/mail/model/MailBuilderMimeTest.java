package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;
import com.mxraven.mail.mime.Attachment;
import com.mxraven.mail.mime.ContentTransferEncoding;
import com.mxraven.mail.mime.MimeType;
import com.mxraven.mail.mime.ParsedEmail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MailBuilderMimeTest {

    @Test
    void buildsAlternativeAndMixedAndRoundTrips() {
        Mail mail = MailBuilder.create()
                .from(new MailboxAddress("alice", "example.com", "Alice Example"))
                .to("bob@example.com")
                .subject("H\u00e9llo")
                .textBody("Hello \u2713")
                .htmlBody("<b>Hello</b>")
                .attachFile("notes.txt", "some notes".getBytes(StandardCharsets.UTF_8), MimeType.TEXT_PLAIN)
                .build();

        String topType = mail.content().headers().first("Content-Type").get();
        assertTrue(topType.startsWith("multipart/mixed; boundary="), topType);

        ParsedEmail parsed = ParsedEmail.parse(mail.content().toRaw());
        assertEquals("H\u00e9llo", parsed.subject());
        assertEquals("Hello \u2713", parsed.textBody().get());
        assertEquals("<b>Hello</b>", parsed.htmlBody().get());
        assertEquals("Alice Example", parsed.from().get(0).displayName());

        assertEquals(1, parsed.attachments().size());
        Attachment attachment = parsed.attachments().get(0);
        assertEquals("notes.txt", attachment.filename());
        assertEquals("some notes", new String(attachment.data(), StandardCharsets.UTF_8));
    }

    @Test
    void buildsAsciiTextAsSevenBit() {
        Mail mail = MailBuilder.create()
                .from("a@example.com")
                .to("b@example.com")
                .textBody("plain")
                .build();

        assertEquals("text/plain; charset=utf-8", mail.content().headers().first("Content-Type").get());
        assertEquals("7bit", mail.content().headers().first("Content-Transfer-Encoding").get());
        assertEquals(ContentTransferEncoding.SEVEN_BIT, mail.content().encoding());
    }

    @Test
    void encodesNonAsciiTextAndSubject() {
        Mail mail = MailBuilder.create()
                .from("a@example.com")
                .to("b@example.com")
                .subject("caf\u00e9")
                .textBody("caf\u00e9")
                .build();

        assertEquals("quoted-printable",
                mail.content().headers().first("Content-Transfer-Encoding").get());
        assertEquals(ContentTransferEncoding.QUOTED_PRINTABLE, mail.content().encoding());
        assertTrue(mail.content().headers().first("Subject").get().startsWith("=?UTF-8?B?"));
        assertEquals("caf\u00e9", ParsedEmail.parse(mail.content().toRaw()).subject());
    }

    @Test
    void inlineAttachmentCarriesContentId() {
        Mail mail = MailBuilder.create()
                .from("a@example.com")
                .to("b@example.com")
                .htmlBody("<img src=\"cid:logo\">")
                .attachInline("logo.png", "logo", new byte[]{1, 2, 3}, MimeType.IMAGE_PNG)
                .build();

        ParsedEmail parsed = ParsedEmail.parse(mail.content().toRaw());
        Attachment attachment = parsed.attachments().get(0);
        assertEquals("logo.png", attachment.filename());
        assertTrue(attachment.inline());
        assertEquals("logo", attachment.contentId());
        assertEquals("image/png", attachment.contentType());
    }

    @Test
    void attachesAFileFromDisk(@TempDir java.nio.file.Path directory) throws Exception {
        java.nio.file.Path file = directory.resolve("notes.txt");
        Files.write(file, "from disk".getBytes(StandardCharsets.UTF_8));

        Mail mail = MailBuilder.create()
                .from("a@example.com")
                .to("b@example.com")
                .textBody("see attached")
                .attachFile(file.toFile())
                .build();

        ParsedEmail parsed = ParsedEmail.parse(mail.content().toRaw());
        Attachment attachment = parsed.attachments().get(0);
        assertEquals("notes.txt", attachment.filename());
        assertEquals("text/plain", attachment.contentType());
        assertEquals("from disk", new String(attachment.data(), StandardCharsets.UTF_8));
    }

    @Test
    void foldsLongHeadersWithinTheLineLimit() {
        String longValue = Java8.repeat("word ", 60).trim();
        Mail mail = MailBuilder.create()
                .from("a@example.com")
                .to("b@example.com")
                .header("X-Long", longValue)
                .build();

        String raw = new String(mail.content().toRaw(), StandardCharsets.UTF_8);
        assertTrue(raw.contains("X-Long: "));
        assertTrue(raw.contains("\r\n "), "the long header should be folded");
        for (String line : raw.split("\r\n")) {
            assertTrue(line.length() <= 998, "header lines must not exceed 998 octets");
        }
    }

    @Test
    void requiresSenderAndRecipient() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> MailBuilder.create().to("b@example.com").build());
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> MailBuilder.create().from("a@example.com").build());
    }
}
