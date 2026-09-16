package com.mxraven.mail.examples;

import com.mxraven.mail.mime.Attachment;
import com.mxraven.mail.mime.ParsedEmail;
import com.mxraven.mail.webhook.InboundEmail;
import com.mxraven.mail.webhook.WebhookCallbacks;
import com.mxraven.mail.webhook.WebhookHandler;
import com.mxraven.mail.webhook.WebhookListener;
import com.mxraven.mail.webhook.WebhookServer;
import com.mxraven.mail.webhook.WebhookVerifier;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Receives signed inbound-mail webhooks, verifies the signature, and parses the
 * raw MIME of each message.
 *
 * <p>Set the signing secret in {@code MXRAVEN_WEBHOOK_SECRET}. Run with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.ReceiveWebhookExample}
 */
public final class ReceiveWebhookExample {

    private ReceiveWebhookExample() {
    }

    public static void main(String[] args) throws Exception {
        String signingSecret = System.getenv("MXRAVEN_WEBHOOK_SECRET");

        WebhookVerifier verifier = WebhookVerifier.builder()
                .secret(signingSecret)
                .build();

        WebhookListener listener = new WebhookCallbacks() {
            @Override
            public void onInboundEmail(InboundEmail email) {
                try {
                    ParsedEmail parsed = email.parse();
                    System.out.println("subject=" + parsed.subject());
                    parsed.textBody().ifPresent(System.out::println);
                    for (Attachment file : parsed.attachments()) {
                        System.out.println(file.filename() + " (" + file.size() + " bytes)");
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        };

        WebhookHandler handler = WebhookHandler.builder()
                .verifier(verifier)
                .listener(listener)
                .build();

        try (WebhookServer server = WebhookServer.builder()
                .port(8080)
                .path("/mxraven/webhook")
                .handler(handler)
                .build()) {
            server.start();
            System.out.println("Listening on port " + server.port() + server.path());
            Thread.currentThread().join();   // keep serving until interrupted
        }
    }
}
