package com.mxraven.mail.webhook;

import java.net.URI;
import java.util.Map;

/**
 * Portable webhook handler: verifies a request, deduplicates it, and dispatches
 * the verified event to a {@link WebhookListener}.
 *
 * <p>It is the single seam every HTTP framework adapts to. Feed it the raw
 * request primitives and map the returned {@link WebhookResult} back to your
 * framework's response:
 *
 * <pre>{@code
 * WebhookHandler handler = WebhookHandler.builder()
 *         .verifier(verifier)
 *         .listener(event -> app.handle(event))
 *         .build();
 *
 * WebhookResult result = handler.handle(method, requestUri, headers, body);
 * response.setStatus(result.status());
 * }</pre>
 *
 * <p>Signature and decoding problems are reported as {@link WebhookResult}
 * values, not thrown, so adapters stay small. A runtime exception thrown by the
 * listener propagates unchanged so the caller can map it to {@code 500} and let
 * the sender retry.
 *
 * <p>A configured {@code WebhookHandler} is immutable and safe for concurrent
 * use as long as its listener and store are.
 */
public final class WebhookHandler {
    private final WebhookVerifier verifier;
    private final WebhookListener listener;
    private final WebhookStore store;

    private WebhookHandler(Builder builder) {
        this.verifier = builder.verifier;
        this.listener = builder.listener;
        this.store = builder.store;
    }

    /** Creates a handler builder. */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Verifies and dispatches a webhook request.
     *
     * @param method  the HTTP method
     * @param uri     the full request URI, including scheme, host, path, and query
     * @param headers the request headers; looked up case-insensitively
     * @param body    the exact raw request body bytes
     * @return the outcome to reflect in the HTTP response
     */
    public WebhookResult handle(String method, URI uri, Map<String, String> headers, byte[] body) {
        WebhookEvent event;
        try {
            event = verifier.verifyAndDecode(method, uri, headers, body);
        } catch (InvalidSignatureException e) {
            return WebhookResult.INVALID_SIGNATURE;
        } catch (WebhookException e) {
            return WebhookResult.BAD_REQUEST;
        }

        String taskId = event.taskId();
        boolean dedupe = store != null && taskId != null && !taskId.isBlank();
        if (dedupe && store.seen(taskId)) {
            return WebhookResult.DUPLICATE;
        }

        listener.onEvent(event);

        if (dedupe) {
            store.remember(taskId);
        }
        return WebhookResult.ACCEPTED;
    }

    /** Builds a {@link WebhookHandler}. */
    public static final class Builder {
        private WebhookVerifier verifier;
        private WebhookListener listener;
        private WebhookStore store;

        private Builder() {
        }

        /** Sets the verifier used to authenticate requests. */
        public Builder verifier(WebhookVerifier verifier) {
            if (verifier == null) {
                throw new IllegalArgumentException("verifier must not be null");
            }
            this.verifier = verifier;
            return this;
        }

        /** Sets the callback invoked for each verified event. */
        public Builder listener(WebhookListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            this.listener = listener;
            return this;
        }

        /** Sets an optional idempotency store. */
        public Builder store(WebhookStore store) {
            this.store = store;
            return this;
        }

        /** Builds the handler. A verifier and a listener are required. */
        public WebhookHandler build() {
            if (verifier == null) {
                throw new IllegalStateException("verifier is required (call verifier(...))");
            }
            if (listener == null) {
                throw new IllegalStateException("listener is required (call listener(...))");
            }
            return new WebhookHandler(this);
        }
    }
}
