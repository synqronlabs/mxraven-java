package com.mxraven.mail.examples;

import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

import java.io.File;

/**
 * Sends a message with a file attachment and an inline image referenced as
 * {@code cid:logo}.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendWithAttachmentsExample}
 */
public final class SendWithAttachmentsExample {

    private SendWithAttachmentsExample() {
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
                .subject("Monthly report")
                .textBody("See the attachment.")
                .htmlBody("<p>See the attachment and <img src=\"cid:logo\"></p>")
                .attachFile(new File("report.pdf"))
                .attachInline(new File("logo.png"), "logo")
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.send(mail);
            System.out.println("success=" + result.success());
        }
    }
}
