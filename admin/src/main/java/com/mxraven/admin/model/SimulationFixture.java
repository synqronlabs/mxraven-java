package com.mxraven.admin.model;

import java.util.List;
import java.util.Map;

/**
 * A synthesized message for a simulated routing-script run.
 *
 * <p>Omitted fields take neutral defaults. {@code auth} and {@code verdicts}
 * apply to MTA listeners only and use the fact field names.
 *
 * @param mailFrom envelope sender
 * @param rcptTo envelope recipients
 * @param headers message headers
 * @param remoteAddr client address
 * @param tlsActive whether TLS is active
 * @param ehloName EHLO name
 * @param sizeBytes message size in bytes
 * @param bodyText text/plain body read by {@code mail.body:text()} (MTA with body access)
 * @param bodyHtml text/html body read by {@code mail.body:links()} and as a text fallback
 * @param attachments attachments reported by {@code mail.body:attachments()}
 * @param receivedAt receipt timestamp
 * @param auth overrides for {@code mail.auth} (spf, dkim, dmarc, arc)
 * @param verdicts overrides for {@code mail.verdicts}
 */
public record SimulationFixture(
        String mailFrom,
        List<String> rcptTo,
        List<SimulationHeader> headers,
        String remoteAddr,
        Boolean tlsActive,
        String ehloName,
        Long sizeBytes,
        String bodyText,
        String bodyHtml,
        List<SimulationAttachment> attachments,
        String receivedAt,
        Map<String, Object> auth,
        Map<String, Object> verdicts) {

    /**
     * Creates a fixture, copying every collection.
     */
    public SimulationFixture {
        rcptTo = rcptTo == null ? List.of() : List.copyOf(rcptTo);
        headers = headers == null ? List.of() : List.copyOf(headers);
        attachments = attachments == null ? List.of() : List.copyOf(attachments);
        auth = auth == null ? Map.of() : Map.copyOf(auth);
        verdicts = verdicts == null ? Map.of() : Map.copyOf(verdicts);
    }
}
