package com.mxraven.mail.webhook;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Test fixtures and an independent reimplementation of the signing algorithm. */
final class WebhookTestSupport {
    static final String SECRET = "whsec_dGVzdHNlY3JldA";
    static final String OTHER_SECRET = "whsec_other";
    static final String KID = "whk_test";
    static final URI URI = java.net.URI.create("https://hooks.example.com/mxraven/risk-check?tenant=acme");

    static final String PAYLOAD = "{\"decision\":\"allow\",\"risk\":0.02,\"reason\":\"known-sender\"}";

    private WebhookTestSupport() {
    }

    static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    static Map<String, String> signedHeaders(String method, URI uri, byte[] body, String timestamp,
                                             String webhookId, String secret) {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(WebhookVerifier.HEADER_WEBHOOK_ID, webhookId);
        headers.put(WebhookVerifier.HEADER_TIMESTAMP, timestamp);
        headers.put(WebhookVerifier.HEADER_SIGNATURE_KID, KID);
        headers.put(WebhookVerifier.HEADER_SIGNATURE, sign(secret, method, uri, body, timestamp, webhookId));
        headers.put(WebhookVerifier.HEADER_LISTENER, "listener-1");
        headers.put(WebhookVerifier.HEADER_REVISION, "revision-1");
        return headers;
    }

    /** Reproduces the mxRaven signing algorithm independently of the class under test. */
    static String sign(String secret, String method, URI uri, byte[] body, String timestamp, String webhookId) {
        String target = uri.getRawPath() == null || uri.getRawPath().isEmpty() ? "/" : uri.getRawPath();
        if (uri.getRawQuery() != null && !uri.getRawQuery().isEmpty()) {
            target += "?" + uri.getRawQuery();
        }
        try {
            String bodyHash = com.mxraven.mail.internal.Java8.toHex(
                    MessageDigest.getInstance("SHA-256").digest(body));
            String canonical = String.join("\n",
                    timestamp,
                    webhookId,
                    method,
                    uri.getRawAuthority().toLowerCase(Locale.ROOT),
                    target,
                    bodyHash);
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return "sha256=" + com.mxraven.mail.internal.Java8.toHex(mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
