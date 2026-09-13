package com.mxraven.mail.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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
