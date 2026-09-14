package com.mxraven.mail.webhook;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.mxraven.mail.webhook.WebhookTestSupport.INBOUND_BODY;
import static com.mxraven.mail.webhook.WebhookTestSupport.KID;
import static com.mxraven.mail.webhook.WebhookTestSupport.OTHER_SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.URI;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;
import static com.mxraven.mail.webhook.WebhookTestSupport.signedHeaders;

class WebhookVerifierTest {

    private static String now() {
        return String.valueOf(Instant.now().getEpochSecond());
    }

    private static WebhookVerifier verifier() {
        return WebhookVerifier.builder().secret(SECRET).build();
    }

    @Test
    void verifiesAndDecodesASignedRequest() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);

        WebhookEvent event = verifier().verifyAndDecode("POST", URI, headers, body);

        assertEquals(WebhookEventType.INBOUND_EMAIL, event.type());
        InboundEmail email = assertInstanceOf(InboundEmail.class, event);
        assertEquals("task-123", email.taskId());
        assertEquals("sender@example.net", email.envelope().mailFrom());
        assertEquals(TerminalAction.RELAY, email.routingDecision().terminalAction());
    }

    @Test
    void rejectsATamperedBody() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);
        byte[] tampered = bytes(INBOUND_BODY + " ");

        assertThrows(InvalidSignatureException.class, () -> verifier().verify("POST", URI, headers, tampered));
    }

    @Test
    void rejectsTheWrongSecret() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", OTHER_SECRET);

        assertThrows(InvalidSignatureException.class, () -> verifier().verify("POST", URI, headers, body));
    }

    @Test
    void rejectsADifferentRequestTarget() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);

        assertThrows(InvalidSignatureException.class,
                () -> verifier().verify("POST", java.net.URI.create("https://hooks.example.com/other"), headers, body));
    }

    @Test
    void rejectsMissingRequiredHeaders() {
        for (String header : new String[]{
                WebhookVerifier.HEADER_WEBHOOK_ID,
                WebhookVerifier.HEADER_TIMESTAMP,
                WebhookVerifier.HEADER_SIGNATURE}) {
            byte[] body = bytes(INBOUND_BODY);
            Map<String, String> headers = new HashMap<>(
                    signedHeaders("POST", URI, body, now(), "task-123", SECRET));
            headers.remove(header);

            assertThrows(WebhookException.class, () -> verifier().verify("POST", URI, headers, body));
        }
    }

    @Test
    void rejectsStaleTimestampsUnlessToleranceIsDisabled() {
        String stale = String.valueOf(Instant.now().minus(Duration.ofMinutes(10)).getEpochSecond());
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, stale, "task-123", SECRET);

        assertThrows(WebhookException.class, () -> verifier().verify("POST", URI, headers, body));

        WebhookVerifier relaxed = WebhookVerifier.builder()
                .secret(SECRET)
                .tolerance(Duration.ZERO)
                .build();
        relaxed.verify("POST", URI, headers, body);
    }

    @Test
    void selectsTheSecretByKeyId() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);

        WebhookVerifier verifier = WebhookVerifier.builder().key(KID, SECRET).build();
        verifier.verify("POST", URI, headers, body);

        WebhookVerifier unknown = WebhookVerifier.builder().key("whk_other", SECRET).build();
        assertThrows(WebhookException.class, () -> unknown.verify("POST", URI, headers, body));

        headers.remove(WebhookVerifier.HEADER_SIGNATURE_KID);
        assertThrows(WebhookException.class, () -> verifier.verify("POST", URI, headers, body));
    }

    @Test
    void rejectsOversizedBodies() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);
        WebhookVerifier verifier = WebhookVerifier.builder().secret(SECRET).maxBodyBytes(16).build();

        assertThrows(WebhookException.class, () -> verifier.verify("POST", URI, headers, body));
    }

    @Test
    void rejectsUnsupportedSignatureScheme() {
        byte[] body = bytes(INBOUND_BODY);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "task-123", SECRET);
        headers.put(WebhookVerifier.HEADER_SIGNATURE, "sha1=deadbeef");

        assertThrows(WebhookException.class, () -> verifier().verify("POST", URI, headers, body));
    }

    @Test
    void requiresASecret() {
        assertThrows(IllegalStateException.class, () -> WebhookVerifier.builder().build());
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().secret(""));
    }

    @Test
    void rejectsInvalidOptions() {
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().key("", SECRET));
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().key(KID, ""));
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().tolerance(Duration.ofSeconds(-1)));
        assertThrows(IllegalArgumentException.class, () -> WebhookVerifier.builder().maxBodyBytes(0));
    }
}
