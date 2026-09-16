package com.mxraven.mail.examples;

import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Path;
import com.mxraven.mail.model.Recipient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Sends a prebuilt RFC 5322 message with an explicit SMTP envelope.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendRawMessageExample}
 */
public final class SendRawMessageExample {

    private SendRawMessageExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        Envelope envelope = Envelope.builder()
                .from(Path.of("sender@example.com"))
                .to(List.of(Recipient.of("recipient@example.com")))
                .build();

        byte[] raw = ("From: sender@example.com\r\n"
                + "To: recipient@example.com\r\n"
                + "Subject: Raw message\r\n"
                + "Date: Thu, 01 Jan 2026 00:00:00 +0000\r\n"
                + "\r\n"
                + "Body built by hand.\r\n").getBytes(StandardCharsets.UTF_8);

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.sendRaw(envelope, raw);
            System.out.println("success=" + result.success());
        }
    }
}
