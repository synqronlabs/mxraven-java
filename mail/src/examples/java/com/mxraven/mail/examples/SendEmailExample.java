package com.mxraven.mail.examples;

import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

/**
 * Sends a plain-text and HTML email over SMTP.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendEmailExample}
 */
public final class SendEmailExample {

    private SendEmailExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        Mail mail = MailBuilder.create()
                .from("Sender <sender@example.com>")
                .to("recipient@example.com")
                .subject("Hello from mxRaven")
                .textBody("Plain text body")
                .htmlBody("<p>HTML body</p>")
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.send(mail);
            System.out.println("success=" + result.success() + " " + result.message());
        }
    }
}
