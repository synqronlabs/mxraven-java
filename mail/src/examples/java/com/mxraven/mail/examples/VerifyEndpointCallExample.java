package com.mxraven.mail.examples;

import com.mxraven.mail.webhook.InvalidSignatureException;
import com.mxraven.mail.webhook.WebhookVerifier;

import java.net.URI;
import com.mxraven.mail.internal.Java8;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Verifies the signature of an {@code mx.http} call from a routing script.
 *
 * <p>The script decides the request body, so after verification parse the
 * schema your endpoint defines. Map an {@link InvalidSignatureException} to
 * {@code 401}.
 *
 * <p>Set the endpoint signing secret in {@code MXRAVEN_WEBHOOK_SECRET}. Run
 * with:
 * {@code ./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.VerifyEndpointCallExample}
 */
public final class VerifyEndpointCallExample {

    private VerifyEndpointCallExample() {
    }

    public static void main(String[] args) throws Exception {
        String signingSecret = System.getenv("MXRAVEN_WEBHOOK_SECRET");

        WebhookVerifier verifier = WebhookVerifier.builder()
                .secret(signingSecret)
                .build();

        // In a handler, these come from the request:
        String method = "POST";
        URI requestUri = URI.create("https://hooks.example.com/mxraven/risk-check");
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(WebhookVerifier.HEADER_WEBHOOK_ID, "call-123");
        headers.put(WebhookVerifier.HEADER_TIMESTAMP, "1789302600");
        headers.put(WebhookVerifier.HEADER_SIGNATURE, "sha256=...");
        headers.put(WebhookVerifier.HEADER_SIGNATURE_KID, "whk_test");
        byte[] body = "{\"decision\":\"allow\"}".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        try {
            verifier.verify(method, requestUri, headers, body);
        } catch (InvalidSignatureException e) {
            System.err.println("unauthorized: " + e.getMessage());
            return;
        }

        // The body is yours to decode after verification.
        System.out.println(new String(body, java.nio.charset.StandardCharsets.UTF_8));
        System.out.println("listener=" + headers.get(WebhookVerifier.HEADER_LISTENER));
        System.out.println("revision=" + headers.get(WebhookVerifier.HEADER_REVISION));
    }
}
