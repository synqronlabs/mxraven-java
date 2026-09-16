package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * One trace field taken from a message header, for example {@code Return-Path}
 * or a {@code Received} field (RFC 5321 §4.4).
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class TraceField {
    private final String type;
    private final String fromDomain;
    private final String fromIp;
    private final String byDomain;
    private final String via;
    private final String with;
    private final String id;
    private final String for_;
    private final OffsetDateTime timestamp;
    private final boolean tls;
    private final String raw;

    /** the field token, such as {@code "Return-Path"} or {@code "Received"} */
    public String type() {
        return type;
    }

    /** the {@code from} domain, or {@code null} */
    public String fromDomain() {
        return fromDomain;
    }

    /** the {@code from} IP address, or {@code null} */
    public String fromIp() {
        return fromIp;
    }

    /** the {@code by} domain, or {@code null} */
    public String byDomain() {
        return byDomain;
    }

    /** the {@code via} link name, or {@code null} */
    public String via() {
        return via;
    }

    /** the {@code with} protocol, or {@code null} */
    public String with() {
        return with;
    }

    /** the trace identifier, or {@code null} */
    public String id() {
        return id;
    }

    /** the {@code for} address, or {@code null} */
    public String for_() {
        return for_;
    }

    /** the trace timestamp, or {@code null} */
    public OffsetDateTime timestamp() {
        return timestamp;
    }

    /** whether the hop used TLS */
    public boolean tls() {
        return tls;
    }

    /** raw field text to use verbatim, or {@code null} */
    public String raw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TraceField that = (TraceField) o;
        return Objects.equals(this.type, that.type)
                && Objects.equals(this.fromDomain, that.fromDomain)
                && Objects.equals(this.fromIp, that.fromIp)
                && Objects.equals(this.byDomain, that.byDomain)
                && Objects.equals(this.via, that.via)
                && Objects.equals(this.with, that.with)
                && Objects.equals(this.id, that.id)
                && Objects.equals(this.for_, that.for_)
                && Objects.equals(this.timestamp, that.timestamp)
                && this.tls == that.tls
                && Objects.equals(this.raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.fromDomain, this.fromIp, this.byDomain, this.via, this.with, this.id, this.for_, this.timestamp, this.tls, this.raw);
    }

    /**
     * Creates a new TraceField.
     *
     * @param type the field token, such as {@code "Return-Path"} or {@code "Received"}
     * @param fromDomain the {@code from} domain, or {@code null}
     * @param fromIp the {@code from} IP address, or {@code null}
     * @param byDomain the {@code by} domain, or {@code null}
     * @param via the {@code via} link name, or {@code null}
     * @param with the {@code with} protocol, or {@code null}
     * @param id the trace identifier, or {@code null}
     * @param for_ the {@code for} address, or {@code null}
     * @param timestamp the trace timestamp, or {@code null}
     * @param tls whether the hop used TLS
     * @param raw raw field text to use verbatim, or {@code null}
     */
    @JsonCreator
    public TraceField(String type, String fromDomain, String fromIp, String byDomain, String via, String with, String id, String for_, OffsetDateTime timestamp, boolean tls, String raw) {
        this.type = type;
        this.fromDomain = fromDomain;
        this.fromIp = fromIp;
        this.byDomain = byDomain;
        this.via = via;
        this.with = with;
        this.id = id;
        this.for_ = for_;
        this.timestamp = timestamp;
        this.tls = tls;
        this.raw = raw;
    }

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
