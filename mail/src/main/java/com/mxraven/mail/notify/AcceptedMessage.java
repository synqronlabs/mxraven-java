package com.mxraven.mail.notify;

import com.mxraven.mail.mime.ParsedEmail;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

/**
 * A message a listener accepted.
 *
 * <p>The event is published after acceptance and after the routing script
 * resolved, including when the terminal action drops the message: acceptance
 * and disposal are separate facts.
 *
 * @param taskId identifies the delivery task, stable across retries
 * @param tenantId the owning tenant
 * @param listenerId the listener that accepted the message
 * @param messageRef the mxRaven message reference; for an outbound listener it
 *                   is the value a submission reply reports as {@code message_ref=}
 * @param attempt the delivery attempt number, starting at 1
 * @param acceptedAtUtc Unix time, in seconds, when mxRaven accepted the message
 * @param occurredAtUtc Unix time, in seconds, when the event was built
 * @param routingDecision what the listener's routing script did for this message
 * @param verdicts spam and malware scan results, when scanning ran
 * @param envelope the SMTP envelope
 * @param message a summary of the parsed message headers
 * @param headers every message header
 * @param rawEmail time-limited access to the raw RFC 822 message
 */
public record AcceptedMessage(
        String taskId,
        String tenantId,
        String listenerId,
        String messageRef,
        long attempt,
        long acceptedAtUtc,
        long occurredAtUtc,
        RoutingDecision routingDecision,
        Verdicts verdicts,
        Envelope envelope,
        MessageSummary message,
        List<HeaderField> headers,
        RawEmail rawEmail) {

    /**
     * Creates an accepted message, copying the header list.
     */
    public AcceptedMessage {
        headers = headers == null ? List.of() : List.copyOf(headers);
    }

    /**
     * Downloads and parses the full message referenced by {@link #rawEmail()}.
     *
     * @return the parsed message
     * @throws IOException when the download fails
     * @throws NotificationException when the event carries no raw-message access
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
     * @throws IOException when the download fails
     * @throws NotificationException when the event carries no raw-message access
     */
    public ParsedEmail parse(HttpClient client) throws IOException {
        if (rawEmail == null) {
            throw new NotificationException("accepted message has no raw message reference");
        }
        return rawEmail.parse(client);
    }
}
