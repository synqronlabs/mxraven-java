package com.mxraven.mail.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

/**
 * A {@code DELIVER_WEBHOOK} delivery: a complete inbound message.
 *
 * <p>Delivery is at-least-once. {@link #taskId()} is stable across retries, so
 * deduplicate on it. {@link #attempt()} starts at 1.
 *
 * @param eventType       always {@code "inbound_email"}
 * @param taskId          identifies the delivery task; stable across retries
 * @param tenantId        the owning tenant
 * @param listenerId      the listener that accepted the message
 * @param attempt         the delivery attempt number, starting at 1
 * @param acceptedAtUtc   Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc   Unix time, in seconds, when this delivery was built
 * @param routingDecision the final routing outcome
 * @param verdicts        spam and malware scan results, when scanning ran
 * @param envelope        the SMTP envelope
 * @param message         a summary of the parsed message headers
 * @param headers         every message header, in the order received
 * @param rawEmail        time-limited access to the raw RFC 822 message
 */
public record InboundEmail(
        String eventType,
        String taskId,
        String tenantId,
        String listenerId,
        long attempt,
        long acceptedAtUtc,
        long occurredAtUtc,
        RoutingDecision routingDecision,
        Verdicts verdicts,
        Envelope envelope,
        MessageSummary message,
        List<HeaderField> headers,
        RawEmail rawEmail) implements WebhookEvent {

    /**
     * Creates an inbound email delivery, copying the header list.
     */
    public InboundEmail {
        headers = headers == null ? List.of() : List.copyOf(headers);
    }

    @Override
    @JsonIgnore
    public WebhookEventType type() {
        return WebhookEventType.INBOUND_EMAIL;
    }

    /**
     * Downloads and parses the full message referenced by {@link #rawEmail()}.
     *
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the delivery carries no raw message reference
     */
    public ParsedEmail parse() throws IOException {
        return parse(null);
    }

    /**
     * Downloads with {@code client} and parses the full message referenced by
     * {@link #rawEmail()}.
     *
     * @param client the HTTP client to download with, or {@code null} for a default client
     * @return the parsed message
     * @throws IOException      when the download fails
     * @throws WebhookException when the delivery carries no raw message reference
     */
    public ParsedEmail parse(HttpClient client) throws IOException {
        if (rawEmail == null) {
            throw new WebhookException("inbound email has no raw message reference");
        }
        return rawEmail.parse(client);
    }
}
