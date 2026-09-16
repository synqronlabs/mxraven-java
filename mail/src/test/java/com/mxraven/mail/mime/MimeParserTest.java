package com.mxraven.mail.mime;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MimeParserTest {

    private static final String ALTERNATIVE_WITH_ATTACHMENT = "Subject: Test message\nFrom: \"Alice Example\" <alice@example.com>\nTo: bob@example.com, \"Carol\" <carol@example.com>\nDate: Sun, 13 Sep 2026 14:30:00 +0000\nMessage-ID: <abc@example.com>\nMIME-Version: 1.0\nContent-Type: multipart/mixed; boundary=\"BOUND1\"\n\n--BOUND1\nContent-Type: multipart/alternative; boundary=\"BOUND2\"\n\n--BOUND2\nContent-Type: text/plain; charset=utf-8\nContent-Transfer-Encoding: quoted-printable\n\nHello =E2=9C=93\nsoft=\nbreak\n--BOUND2\nContent-Type: text/html; charset=utf-8\nContent-Transfer-Encoding: 7bit\n\n<html><body>Hi</body></html>\n--BOUND2--\n--BOUND1\nContent-Type: application/octet-stream; name=\"hello.txt\"\nContent-Disposition: attachment; filename=\"hello.txt\"\nContent-Transfer-Encoding: base64\n\nSGVsbG8gZmlsZSE=\n--BOUND1--\n";

    private static ParsedEmail parse(String raw) {
        return ParsedEmail.parse(raw.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void parsesHeadersAddressesAndDate() {
        ParsedEmail email = parse(ALTERNATIVE_WITH_ATTACHMENT);

        assertEquals("Test message", email.subject());
        assertEquals("<abc@example.com>", email.messageId().get());
        assertTrue(email.date().isPresent());
        assertEquals(1, email.from().size());
        assertEquals("Alice Example", email.from().get(0).displayName());
        assertEquals("alice@example.com", email.from().get(0).toString());
        assertEquals(2, email.to().size());
        assertEquals("carol@example.com", email.to().get(1).toString());
    }

    @Test
    void extractsTextAndHtmlBodies() {
        ParsedEmail email = parse(ALTERNATIVE_WITH_ATTACHMENT);

        assertEquals("Hello \u2713\nsoftbreak", email.textBody().get());
        assertEquals("<html><body>Hi</body></html>", email.htmlBody().get());
    }

    @Test
    void extractsDecodedAttachments() {
        ParsedEmail email = parse(ALTERNATIVE_WITH_ATTACHMENT);

        List<Attachment> attachments = email.attachments();
        assertEquals(1, attachments.size());
        Attachment attachment = attachments.get(0);
        assertEquals("hello.txt", attachment.filename());
        assertEquals("application/octet-stream", attachment.contentType());
        assertFalse(attachment.inline());
        assertEquals("Hello file!", new String(attachment.data(), StandardCharsets.UTF_8));
    }

    @Test
    void decodesRfc2047SubjectAndDisplayName() {
        String raw = "Subject: =?UTF-8?B?SGVsbG8gV29ybGQ=?=\nFrom: =?UTF-8?Q?Jos=C3=A9?= <jose@example.com>\nContent-Type: text/plain; charset=utf-8\n\nbody\n";

        ParsedEmail email = parse(raw);
        assertEquals("Hello World", email.subject());
        assertEquals("Jos\u00e9", email.from().get(0).displayName());
    }

    @Test
    void parsesASimpleBodyAndDefaultsTextCharset() {
        String raw = "Subject: Simple\r\nContent-Type: text/plain\r\n\r\njust text";

        ParsedEmail email = parse(raw);
        assertEquals("just text", email.textBody().get());
        assertTrue(email.attachments().isEmpty());
        assertEquals("us-ascii", email.mime().charset());
    }

    @Test
    void decodesRfc2231AttachmentFilename() {
        MediaType type = MediaType.parse("attachment; filename*=UTF-8''%C3%A9.txt");
        assertEquals("attachment", type.type());
        assertEquals("\u00e9.txt", type.parameter("filename"));
    }

    @Test
    void handlesAnEmptyMessage() {
        ParsedEmail email = ParsedEmail.parse(new byte[0]);
        assertEquals("", email.subject());
        assertTrue(!email.textBody().isPresent());
        assertTrue(email.attachments().isEmpty());
    }
}
