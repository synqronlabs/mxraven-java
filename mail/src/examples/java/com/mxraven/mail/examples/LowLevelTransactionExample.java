package com.mxraven.mail.examples;

import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.SmtpResponse;

import java.nio.charset.StandardCharsets;

/**
 * Drives an SMTP transaction by hand: {@code MAIL FROM}, {@code RCPT TO}, then
 * {@code DATA} or a single {@code BDAT … LAST} when the server supports
 * {@code CHUNKING}. Each call returns the raw reply.
 *
 * <p>Prefer {@link com.mxraven.mail.SmtpClient#send} unless you need this level of
 * control; it handles envelope parameters, recipient gating, and {@code RSET}.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.LowLevelTransactionExample}
 */
public final class LowLevelTransactionExample {

    private LowLevelTransactionExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        byte[] message = ("From: sender@example.com\r\n"
                + "To: recipient@example.com\r\n"
                + "Subject: Low-level transaction\r\n"
                + "Date: Thu, 01 Jan 2026 00:00:00 +0000\r\n"
                + "\r\n"
                + "Body built by hand.\r\n").getBytes(StandardCharsets.UTF_8);

        try (SmtpClient client = SmtpClient.connect(config)) {
            SmtpResponse from = client.mail("sender@example.com");
            System.out.println("MAIL FROM -> " + from.code() + " " + from.message());
            if (!from.isSuccess()) {
                client.reset();   // abort, since the transaction never opened
                return;
            }

            SmtpResponse rcpt = client.rcpt("recipient@example.com");
            System.out.println("RCPT TO   -> " + rcpt.code() + " " + rcpt.message());

            SmtpResponse done = client.hasExtension("CHUNKING")
                    ? client.bdat(message, true)   // one BDAT ... LAST, no dot-stuffing
                    : client.data(message);        // DATA + dot-stuffing + "."
            System.out.println("body      -> " + done.code() + " " + done.message());
            done.enhancedStatus().ifPresent(status ->
                    System.out.println("enhanced  -> " + status.code()));

            if (!done.isSuccess()) {
                client.reset();
            }
            client.noop();
            System.out.println("last reply -> " + client.lastResponse().code());
            client.quit();
        }
    }
}
