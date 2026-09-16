package com.mxraven.mail.examples;

import com.mxraven.mail.SendOptions;
import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.SmtpPool;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sends a batch concurrently through a bounded pool of authenticated
 * connections. The pool health-checks, evicts, and retires connections, and
 * drains in-flight sends on close.
 *
 * <p>Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.HighThroughputPoolExample}
 */
public final class HighThroughputPoolExample {

    private HighThroughputPoolExample() {
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
                .subject("High-throughput send")
                .textBody("Sent through the pool.")
                .build();

        AtomicInteger accepted = new AtomicInteger();
        try (SmtpPool pool = new SmtpPool(
                config,
                8,                          // at most 8 connections
                Duration.ofMinutes(5),      // close after 5 minutes idle
                Duration.ofMinutes(30))) {  // retire after 30 minutes total
            ExecutorService workers = Executors.newFixedThreadPool(8);
            try {
                for (int i = 0; i < 100; i++) {
                    final int index = i;
                    workers.submit(() -> {
                        try {
                            SendResult result = pool.send(mail, SendOptions.builder()
                                    .timeout(Duration.ofSeconds(30))
                                    .build());
                            if (result.success()) {
                                accepted.incrementAndGet();
                            }
                            System.out.println("send " + index + " ok=" + result.success()
                                    + " ref=" + result.messageRef());
                        } catch (IOException e) {
                            System.out.println("send " + index + " failed: " + e.getMessage());
                        }
                    });
                }
            } finally {
                workers.shutdown();
                workers.awaitTermination(1, TimeUnit.MINUTES);
            }
            System.out.println("accepted=" + accepted.get() + " borrowedNow=" + pool.activeCount());
            // close() drains in-flight sends; use close(Duration) to bound the wait.
        }
    }
}
