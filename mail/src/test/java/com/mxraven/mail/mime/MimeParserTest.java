package com.mxraven.mail.mime;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MimeParserTest {

    private static final String ALTERNATIVE_WITH_ATTACHMENT = """
            Subject: Test message
            From: "Alice Example" <alice@example.com>
            To: bob@example.com, "Carol" <carol@example.com>
            Date: Sun, 13 Sep 2026 14:30:00 +0000
            Message-ID: <abc@example.com>
            MIME-Version: 1.0
            Content-Type: multipart/mixed; boundary="BOUND1"

            --BOUND1
            Content-Type: multipart/alternative; boundary="BOUND2"

            --BOUND2
            Content-Type: text/plain; charset=utf-8
            Content-Transfer-Encoding: quoted-printable

            Hello =E2=9C=93
            soft=
            break
            --BOUND2
            Content-Type: text/html; charset=utf-8
            Content-Transfer-Encoding: 7bit

            <html><body>Hi</body></html>
            --BOUND2--
            --BOUND1
            Content-Type: application/octet-stream; name="hello.txt"
            Content-Disposition: attachment; filename="hello.txt"
            Content-Transfer-Encoding: base64

            SGVsbG8gZmlsZSE=
            --BOUND1--
            """;

    private static ParsedEmail parse(String raw) {
        return ParsedEmail.parse(raw.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void parsesHeadersAddressesAndDate() {
        ParsedEmail email = parse(ALTERNATIVE_WITH_ATTACHMENT);

        assertEquals("Test message", email.subject());
        assertEquals("<abc@example.com>", email.messageId().orElseThrow());
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

        assertEquals("Hello \u2713\nsoftbreak", email.textBody().orElseThrow());
        assertEquals("<html><body>Hi</body></html>", email.htmlBody().orElseThrow());
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
        String raw = """
                Subject: =?UTF-8?B?SGVsbG8gV29ybGQ=?=
                From: =?UTF-8?Q?Jos=C3=A9?= <jose@example.com>
                Content-Type: text/plain; charset=utf-8

                body
                """;

        ParsedEmail email = parse(raw);
        assertEquals("Hello World", email.subject());
        assertEquals("Jos\u00e9", email.from().get(0).displayName());
    }

    @Test
    void parsesASimpleBodyAndDefaultsTextCharset() {
        String raw = "Subject: Simple\r\nContent-Type: text/plain\r\n\r\njust text";

        ParsedEmail email = parse(raw);
        assertEquals("just text", email.textBody().orElseThrow());
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
        assertTrue(email.textBody().isEmpty());
        assertTrue(email.attachments().isEmpty());
    }
}
