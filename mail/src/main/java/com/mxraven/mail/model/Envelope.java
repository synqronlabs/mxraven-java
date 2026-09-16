package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;
import java.util.Map;

/**
 * The SMTP envelope of a message (RFC 5321): the reverse and forward paths plus
 * the ESMTP parameters requested for delivery.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Envelope {
    private final Path from;
    private final List<Recipient> to;
    private final BodyType bodyType;
    private final long size;
    private final boolean smtpUtf8;
    private final boolean requireTls;
    private final DeliveryBy deliveryBy;
    private final String envId;
    private final DSNEnvelopeParams dsnParams;
    private final String auth;
    private final Map<String, String> extensionParams;

    /** the reverse path (MAIL FROM) */
    public Path from() {
        return from;
    }

    /** the forward paths (RCPT TO); a {@code null} value is replaced with an empty list */
    public List<Recipient> to() {
        return to;
    }

    /** the requested {@code BODY} type, or {@code null} */
    public BodyType bodyType() {
        return bodyType;
    }

    /** the declared message size in bytes */
    public long size() {
        return size;
    }

    /** whether the {@code SMTPUTF8} extension is requested */
    public boolean smtpUtf8() {
        return smtpUtf8;
    }

    /** whether the {@code REQUIRETLS} extension is requested */
    public boolean requireTls() {
        return requireTls;
    }

    /** the DELIVERBY parameter, or {@code null} */
    public DeliveryBy deliveryBy() {
        return deliveryBy;
    }

    /** the envelope identifier ({@code ENVID}), or {@code null} */
    public String envId() {
        return envId;
    }

    /** the DSN envelope parameters, or {@code null} */
    public DSNEnvelopeParams dsnParams() {
        return dsnParams;
    }

    /** the AUTH identity, or {@code null} */
    public String auth() {
        return auth;
    }

    /** additional extension parameters by name; a {@code null} value is replaced with an empty map */
    public Map<String, String> extensionParams() {
        return extensionParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Envelope that = (Envelope) o;
        return Objects.equals(this.from, that.from)
                && Objects.equals(this.to, that.to)
                && Objects.equals(this.bodyType, that.bodyType)
                && this.size == that.size
                && this.smtpUtf8 == that.smtpUtf8
                && this.requireTls == that.requireTls
                && Objects.equals(this.deliveryBy, that.deliveryBy)
                && Objects.equals(this.envId, that.envId)
                && Objects.equals(this.dsnParams, that.dsnParams)
                && Objects.equals(this.auth, that.auth)
                && Objects.equals(this.extensionParams, that.extensionParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.from, this.to, this.bodyType, this.size, this.smtpUtf8, this.requireTls, this.deliveryBy, this.envId, this.dsnParams, this.auth, this.extensionParams);
    }

    @Override
    public String toString() {
        return "Envelope[" + "from=" + this.from + ", " + "to=" + this.to + ", " + "bodyType=" + this.bodyType + ", " + "size=" + this.size + ", " + "smtpUtf8=" + this.smtpUtf8 + ", " + "requireTls=" + this.requireTls + ", " + "deliveryBy=" + this.deliveryBy + ", " + "envId=" + this.envId + ", " + "dsnParams=" + this.dsnParams + ", " + "auth=" + this.auth + ", " + "extensionParams=" + this.extensionParams + "]";
    }

    /**
     * Creates an envelope, copying the recipient list and extension parameters.
     */
    @JsonCreator
    public Envelope(Path from, List<Recipient> to, BodyType bodyType, long size, boolean smtpUtf8, boolean requireTls, DeliveryBy deliveryBy, String envId, DSNEnvelopeParams dsnParams, String auth, Map<String, String> extensionParams) {

        to = to == null ? Java8.list() : Java8.copyList(to);
        extensionParams = extensionParams == null ? Java8.map() : Java8.copyMap(extensionParams);
    
        this.from = from;
        this.to = to;
        this.bodyType = bodyType;
        this.size = size;
        this.smtpUtf8 = smtpUtf8;
        this.requireTls = requireTls;
        this.deliveryBy = deliveryBy;
        this.envId = envId;
        this.dsnParams = dsnParams;
        this.auth = auth;
        this.extensionParams = extensionParams;
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** A fluent builder for {@link Envelope} instances. */
    public static final class Builder {
        private Path from;
        private List<Recipient> to = Java8.list();
        private BodyType bodyType;
        private long size;
        private boolean smtpUtf8;
        private boolean requireTls;
        private DeliveryBy deliveryBy;
        private String envId;
        private DSNEnvelopeParams dsnParams;
        private String auth;
        private Map<String, String> extensionParams = Java8.map();

        /**
         * Sets the reverse path.
         *
         * @param from the reverse path
         * @return this builder
         */
        public Builder from(Path from) {
            this.from = from;
            return this;
        }

        /**
         * Sets the forward paths.
         *
         * @param to the recipients
         * @return this builder
         */
        public Builder to(List<Recipient> to) {
            this.to = to;
            return this;
        }

        /**
         * Sets the requested {@code BODY} type.
         *
         * @param bodyType the body type
         * @return this builder
         */
        public Builder bodyType(BodyType bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        /**
         * Sets the declared message size.
         *
         * @param size the size in bytes
         * @return this builder
         */
        public Builder size(long size) {
            this.size = size;
            return this;
        }

        /**
         * Sets whether the {@code SMTPUTF8} extension is requested.
         *
         * @param smtpUtf8 whether SMTPUTF8 is requested
         * @return this builder
         */
        public Builder smtpUtf8(boolean smtpUtf8) {
            this.smtpUtf8 = smtpUtf8;
            return this;
        }

        /**
         * Sets whether the {@code REQUIRETLS} extension is requested.
         *
         * @param requireTls whether REQUIRETLS is requested
         * @return this builder
         */
        public Builder requireTls(boolean requireTls) {
            this.requireTls = requireTls;
            return this;
        }

        /**
         * Sets the DELIVERBY parameter.
         *
         * @param deliveryBy the delivery-by value
         * @return this builder
         */
        public Builder deliveryBy(DeliveryBy deliveryBy) {
            this.deliveryBy = deliveryBy;
            return this;
        }

        /**
         * Sets the envelope identifier.
         *
         * @param envId the {@code ENVID} value
         * @return this builder
         */
        public Builder envId(String envId) {
            this.envId = envId;
            return this;
        }

        /**
         * Sets the DSN envelope parameters.
         *
         * @param dsnParams the DSN parameters
         * @return this builder
         */
        public Builder dsnParams(DSNEnvelopeParams dsnParams) {
            this.dsnParams = dsnParams;
            return this;
        }

        /**
         * Sets the AUTH identity.
         *
         * @param auth the AUTH value
         * @return this builder
         */
        public Builder auth(String auth) {
            this.auth = auth;
            return this;
        }

        /**
         * Sets additional extension parameters by name.
         *
         * @param extensionParams the extension parameters
         * @return this builder
         */
        public Builder extensionParams(Map<String, String> extensionParams) {
            this.extensionParams = extensionParams;
            return this;
        }

        /**
         * Builds the envelope.
         *
         * @return a new envelope
         */
        public Envelope build() {
            return new Envelope(from, to, bodyType, size, smtpUtf8, requireTls,
                    deliveryBy, envId, dsnParams, auth, extensionParams);
        }
    }
}
