package com.mxraven.mail.examples;

import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.SmtpPool;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

/**
 * Reuses a small pool of connections for several sends.
 *
 * <p>Use the pool instead of a bare {@link com.mxraven.mail.SmtpClient} whenever
 * more than one thread sends, or when sending many messages: connections stay
 * connected and authenticated, so each send skips the
 * connect/STARTTLS/EHLO/AUTH cost.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.PoolSendingExample}
 */
public final class PoolSendingExample {

    private PoolSendingExample() {
    }

    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        Mail mail = MailBuilder.create()
                .from("sender@example.com")
                .to("recipient@example.com")
                .subject("Hello from the pool")
                .textBody("This send reused a pooled connection.")
                .build();

        try (SmtpPool pool = new SmtpPool(config, 4)) {
            for (int i = 0; i < 5; i++) {
                SendResult result = pool.send(mail);
                System.out.println("send " + i + " success=" + result.success()
                        + " ref=" + result.messageRef());
            }
            System.out.println("borrowed now=" + pool.activeCount());
        }
    }
}
