package com.mxraven.mail.examples;

import com.mxraven.mail.mime.Attachment;
import com.mxraven.mail.mime.ParsedEmail;

import java.nio.charset.StandardCharsets;

/**
 * Parses a raw RFC 822 message into its headers, bodies, and attachments.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.ParseMimeExample}
 */
public final class ParseMimeExample {

    private ParseMimeExample() {
    }

    public static void main(String[] args) {
        byte[] raw = ("From: Sender <sender@example.com>\r\n"
                + "To: recipient@example.com\r\n"
                + "Subject: Parsed by hand\r\n"
                + "MIME-Version: 1.0\r\n"
                + "Content-Type: text/plain; charset=UTF-8\r\n"
                + "\r\n"
                + "Hello from a raw message.\r\n").getBytes(StandardCharsets.UTF_8);

        ParsedEmail parsed = ParsedEmail.parse(raw);
        System.out.println("subject=" + parsed.subject());
        parsed.textBody().ifPresent(body -> System.out.println("body=" + body));
        for (Attachment attachment : parsed.attachments()) {
            System.out.println("attachment=" + attachment.filename());
        }
    }
}
