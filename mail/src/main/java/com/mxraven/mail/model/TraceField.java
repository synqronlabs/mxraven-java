package com.mxraven.mail.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * One trace field taken from a message header, for example {@code Return-Path}
 * or a {@code Received} field (RFC 5321 §4.4).
 *
 * @param type       the field token, such as {@code "Return-Path"} or
 *                   {@code "Received"}
 * @param fromDomain the {@code from} domain, or {@code null}
 * @param fromIp     the {@code from} IP address, or {@code null}
 * @param byDomain   the {@code by} domain, or {@code null}
 * @param via        the {@code via} link name, or {@code null}
 * @param with       the {@code with} protocol, or {@code null}
 * @param id         the trace identifier, or {@code null}
 * @param for_       the {@code for} address, or {@code null}
 * @param timestamp  the trace timestamp, or {@code null}
 * @param tls        whether the hop used TLS
 * @param raw        raw field text to use verbatim, or {@code null}
 */
public record TraceField(
        String type,
        String fromDomain,
        String fromIp,
        String byDomain,
        String via,
        String with,
        String id,
        String for_,
        OffsetDateTime timestamp,
        boolean tls,
        String raw) {

    /**
     * Creates a {@code Return-Path} trace field for a reverse path.
     *
     * @param reversePath the reverse path
     * @return the trace field, with the mailbox text in the {@code for} slot
     */
    public static TraceField returnPath(Path reversePath) {
        return new TraceField("Return-Path", null, null, null, null, null, null,
                reversePath.mailbox().toString(), null, false, null);
    }

    @Override
    public String toString() {
        if (raw != null && !raw.isEmpty()) {
            return raw;
        }
        if ("Return-Path".equals(type)) {
            return for_ == null || for_.isEmpty() ? "<>" : "<" + for_ + ">";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(fromDomain);
        if (fromIp != null && !fromIp.isEmpty()) {
            sb.append(" (").append(fromIp).append(")");
        }
        if (byDomain != null && !byDomain.isEmpty()) {
            sb.append(" by ").append(byDomain);
        }
        if (via != null && !via.isEmpty()) {
            sb.append(" via ").append(via);
        }
        if (with != null && !with.isEmpty()) {
            sb.append(" with ").append(with);
        }
        if (id != null && !id.isEmpty()) {
            sb.append(" id ").append(id);
        }
        if (for_ != null && !for_.isEmpty()) {
            sb.append(" for <").append(for_).append(">");
        }
        if (timestamp != null) {
            sb.append("; ").append(timestamp.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        }
        return sb.toString();
    }
}
