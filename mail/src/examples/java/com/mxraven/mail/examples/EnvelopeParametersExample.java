package com.mxraven.mail.examples;

import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.BodyType;
import com.mxraven.mail.model.DSNEnvelopeParams;
import com.mxraven.mail.model.DSNRecipientParams;
import com.mxraven.mail.model.DeliveryBy;
import com.mxraven.mail.model.DeliveryByMode;
import com.mxraven.mail.model.Envelope;
import com.mxraven.mail.model.Path;
import com.mxraven.mail.model.Recipient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Sends with envelope parameters: DSN ({@code RET}/{@code ENVID}, per-recipient
 * {@code NOTIFY}/{@code ORCPT}), {@code BODY}, and DELIVERBY. Each parameter is
 * sent only when the server advertises the matching extension.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.EnvelopeParametersExample}
 */
public final class EnvelopeParametersExample {

    private EnvelopeParametersExample() {
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
                .to(List.of(new Recipient(Path.of("recipient@example.com"),
                        new DSNRecipientParams(
                                List.of("SUCCESS", "FAILURE"),  // NEVER must be used alone
                                "orig@example.com"))))          // sent as rfc822;orig@example.com
                .bodyType(BodyType.EIGHT_BIT_MIME)              // BODY=8BITMIME
                .deliveryBy(new DeliveryBy(3600, DeliveryByMode.NOTIFY, false))   // BY=3600;N
                .dsnParams(new DSNEnvelopeParams("FULL"))       // RET=FULL
                .envId("order-12345")                           // ENVID=order-12345
                .build();

        byte[] raw = ("From: sender@example.com\r\n"
                + "To: recipient@example.com\r\n"
                + "Subject: Envelope parameters\r\n"
                + "Date: Thu, 01 Jan 2026 00:00:00 +0000\r\n"
                + "\r\n"
                + "Body.\r\n").getBytes(StandardCharsets.UTF_8);

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.sendRaw(envelope, raw);
            System.out.println("success=" + result.success() + " " + result.message());
        }
    }
}
