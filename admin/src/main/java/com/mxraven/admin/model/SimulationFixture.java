package com.mxraven.admin.model;

import com.mxraven.admin.internal.Java8;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;
import java.util.List;
import java.util.Map;

/**
 * A synthesized message for a simulated routing-script run.
 *
 * <p>Omitted fields take neutral defaults. {@code auth} and {@code verdicts}
 * apply to MTA listeners only and use the fact field names.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SimulationFixture {
    private final String mailFrom;
    private final List<String> rcptTo;
    private final List<SimulationHeader> headers;
    private final String remoteAddr;
    private final Boolean tlsActive;
    private final String ehloName;
    private final Long sizeBytes;
    private final String bodyText;
    private final String bodyHtml;
    private final List<SimulationAttachment> attachments;
    private final String receivedAt;
    private final Map<String, Object> auth;
    private final Map<String, Object> verdicts;

    /** envelope sender */
    public String mailFrom() {
        return mailFrom;
    }

    /** envelope recipients */
    public List<String> rcptTo() {
        return rcptTo;
    }

    /** message headers */
    public List<SimulationHeader> headers() {
        return headers;
    }

    /** client address */
    public String remoteAddr() {
        return remoteAddr;
    }

    /** whether TLS is active */
    public Boolean tlsActive() {
        return tlsActive;
    }

    /** EHLO name */
    public String ehloName() {
        return ehloName;
    }

    /** message size in bytes */
    public Long sizeBytes() {
        return sizeBytes;
    }

    /** text/plain body read by {@code mail.body:text()} (MTA with body access) */
    public String bodyText() {
        return bodyText;
    }

    /** text/html body read by {@code mail.body:links()} and as a text fallback */
    public String bodyHtml() {
        return bodyHtml;
    }

    /** attachments reported by {@code mail.body:attachments()} */
    public List<SimulationAttachment> attachments() {
        return attachments;
    }

    /** receipt timestamp */
    public String receivedAt() {
        return receivedAt;
    }

    /** overrides for {@code mail.auth} (spf, dkim, dmarc, arc) */
    public Map<String, Object> auth() {
        return auth;
    }

    public Map<String, Object> verdicts() {
        return verdicts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulationFixture that = (SimulationFixture) o;
        return Objects.equals(this.mailFrom, that.mailFrom) && Objects.equals(this.rcptTo, that.rcptTo) && Objects.equals(this.headers, that.headers) && Objects.equals(this.remoteAddr, that.remoteAddr) && Objects.equals(this.tlsActive, that.tlsActive) && Objects.equals(this.ehloName, that.ehloName) && Objects.equals(this.sizeBytes, that.sizeBytes) && Objects.equals(this.bodyText, that.bodyText) && Objects.equals(this.bodyHtml, that.bodyHtml) && Objects.equals(this.attachments, that.attachments) && Objects.equals(this.receivedAt, that.receivedAt) && Objects.equals(this.auth, that.auth) && Objects.equals(this.verdicts, that.verdicts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mailFrom, this.rcptTo, this.headers, this.remoteAddr, this.tlsActive, this.ehloName, this.sizeBytes, this.bodyText, this.bodyHtml, this.attachments, this.receivedAt, this.auth, this.verdicts);
    }

    @Override
    public String toString() {
        return "SimulationFixture[" + "mailFrom=" + this.mailFrom + ", " + "rcptTo=" + this.rcptTo + ", " + "headers=" + this.headers + ", " + "remoteAddr=" + this.remoteAddr + ", " + "tlsActive=" + this.tlsActive + ", " + "ehloName=" + this.ehloName + ", " + "sizeBytes=" + this.sizeBytes + ", " + "bodyText=" + this.bodyText + ", " + "bodyHtml=" + this.bodyHtml + ", " + "attachments=" + this.attachments + ", " + "receivedAt=" + this.receivedAt + ", " + "auth=" + this.auth + ", " + "verdicts=" + this.verdicts + "]";
    }

    /**
     * Creates a new SimulationFixture.
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
     * @param verdicts verdicts
     */
    @JsonCreator
    public SimulationFixture(String mailFrom, List<String> rcptTo, List<SimulationHeader> headers, String remoteAddr, Boolean tlsActive, String ehloName, Long sizeBytes, String bodyText, String bodyHtml, List<SimulationAttachment> attachments, String receivedAt, Map<String, Object> auth, Map<String, Object> verdicts) {
        this.mailFrom = mailFrom;
        this.remoteAddr = remoteAddr;
        this.tlsActive = tlsActive;
        this.ehloName = ehloName;
        this.sizeBytes = sizeBytes;
        this.bodyText = bodyText;
        this.bodyHtml = bodyHtml;
        this.receivedAt = receivedAt;
        this.rcptTo = rcptTo == null ? Java8.list() : Java8.copyList(rcptTo);
        this.headers = headers == null ? Java8.list() : Java8.copyList(headers);
        this.attachments = attachments == null ? Java8.list() : Java8.copyList(attachments);
        this.auth = auth == null ? Java8.map() : Java8.copyMap(auth);
        this.verdicts = verdicts == null ? Java8.map() : Java8.copyMap(verdicts);
    }

    /**
         * Creates a fixture, copying every collection.
         */
}
