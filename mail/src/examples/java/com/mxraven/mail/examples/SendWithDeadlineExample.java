package com.mxraven.mail.examples;

import com.mxraven.mail.Cancellation;
import com.mxraven.mail.SendOptions;
import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.SmtpException;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

import java.time.Duration;

/**
 * Bounds a single send with a deadline, and aborts one from another thread with
 * a {@link Cancellation}.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendWithDeadlineExample}
 */
public final class SendWithDeadlineExample {

    private SendWithDeadlineExample() {
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
                .subject("Bounded send")
                .textBody("This send has a deadline.")
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            // The deadline covers the whole transaction, including the body.
            SendResult result = client.send(mail, SendOptions.builder()
                    .timeout(Duration.ofSeconds(30))
                    .build());
            System.out.println("deadline success=" + result.success());

            // Cancellation targets this one send. If the client is blocked in a
            // read or write the connection is closed to unblock it; otherwise it
            // stays usable.
            Cancellation cancellation = Cancellation.create();
            Thread canceller = new Thread(() -> {
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                cancellation.cancel();
            }, "example-canceller");
            canceller.setDaemon(true);
            canceller.start();

            try {
                client.send(mail, SendOptions.builder()
                        .timeout(Duration.ofMinutes(2))
                        .cancellation(cancellation)
                        .build());
                System.out.println("completed before the cancellation fired");
            } catch (SmtpException e) {
                System.out.println("cancelled: " + e.getMessage());
            }
        }
    }
}
