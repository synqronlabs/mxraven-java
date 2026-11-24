package com.mxraven.mail.webhook;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.mxraven.mail.webhook.WebhookTestSupport.KID;
import static com.mxraven.mail.webhook.WebhookTestSupport.OTHER_SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.PAYLOAD;
import static com.mxraven.mail.webhook.WebhookTestSupport.SECRET;
import static com.mxraven.mail.webhook.WebhookTestSupport.URI;
import static com.mxraven.mail.webhook.WebhookTestSupport.bytes;
import static com.mxraven.mail.webhook.WebhookTestSupport.signedHeaders;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebhookVerifierTest {

    private static String now() {
        return String.valueOf(Instant.now().getEpochSecond());
    }

    private static WebhookVerifier verifier() {
        return WebhookVerifier.builder().secret(SECRET).build();
    }

    @Test
    void verifiesASignedRequestWithAnEndpointDefinedBody() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);

        assertDoesNotThrow(() -> verifier().verify("POST", URI, headers, body));
    }

    @Test
    void unsignedListenerAndRevisionHeadersDoNotAffectTheSignature() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);
        headers.put(WebhookVerifier.HEADER_LISTENER, "another-listener");
        headers.put(WebhookVerifier.HEADER_REVISION, "another-revision");

        assertDoesNotThrow(() -> verifier().verify("POST", URI, headers, body));
    }

    @Test
    void rejectsATamperedBody() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);
        byte[] tampered = bytes(PAYLOAD + " ");

        assertThrows(InvalidSignatureException.class, () -> verifier().verify("POST", URI, headers, tampered));
    }

    @Test
    void rejectsTheWrongSecret() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", OTHER_SECRET);

        assertThrows(InvalidSignatureException.class, () -> verifier().verify("POST", URI, headers, body));
    }

    @Test
    void rejectsADifferentRequestTarget() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);

        assertThrows(InvalidSignatureException.class,
                () -> verifier().verify("POST", java.net.URI.create("https://hooks.example.com/other"), headers, body));
    }

    @Test
    void rejectsMissingRequiredHeaders() {
        for (String header : new String[]{
                WebhookVerifier.HEADER_WEBHOOK_ID,
                WebhookVerifier.HEADER_TIMESTAMP,
                WebhookVerifier.HEADER_SIGNATURE}) {
            byte[] body = bytes(PAYLOAD);
            Map<String, String> headers = new HashMap<>(
                    signedHeaders("POST", URI, body, now(), "call-123", SECRET));
            headers.remove(header);

            assertThrows(WebhookException.class, () -> verifier().verify("POST", URI, headers, body));
        }
    }

    @Test
    void rejectsStaleTimestampsUnlessToleranceIsDisabled() {
        String stale = String.valueOf(Instant.now().minus(Duration.ofMinutes(10)).getEpochSecond());
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, stale, "call-123", SECRET);

        assertThrows(WebhookException.class, () -> verifier().verify("POST", URI, headers, body));

        WebhookVerifier relaxed = WebhookVerifier.builder()
                .secret(SECRET)
                .tolerance(Duration.ZERO)
                .build();
        relaxed.verify("POST", URI, headers, body);
    }

    @Test
    void selectsTheSecretByKeyId() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);

        WebhookVerifier verifier = WebhookVerifier.builder().key(KID, SECRET).build();
        verifier.verify("POST", URI, headers, body);

        WebhookVerifier unknown = WebhookVerifier.builder().key("whk_other", SECRET).build();
        assertThrows(WebhookException.class, () -> unknown.verify("POST", URI, headers, body));

        headers.remove(WebhookVerifier.HEADER_SIGNATURE_KID);
        assertThrows(WebhookException.class, () -> verifier.verify("POST", URI, headers, body));
    }

    @Test
    void rejectsOversizedBodies() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);
        WebhookVerifier verifier = WebhookVerifier.builder().secret(SECRET).maxBodyBytes(16).build();

        assertThrows(PayloadTooLargeException.class, () -> verifier.verify("POST", URI, headers, body));
    }

    @Test
    void rejectsUnsupportedSignatureScheme() {
        byte[] body = bytes(PAYLOAD);
        Map<String, String> headers = signedHeaders("POST", URI, body, now(), "call-123", SECRET);
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
