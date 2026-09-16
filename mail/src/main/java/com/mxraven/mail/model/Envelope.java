package com.mxraven.mail.model;

import java.util.List;
import java.util.Map;

/**
 * The SMTP envelope of a message (RFC 5321): the reverse and forward paths plus
 * the ESMTP parameters requested for delivery.
 *
 * @param from            the reverse path (MAIL FROM)
 * @param to              the forward paths (RCPT TO); a {@code null} value is
 *                        replaced with an empty list
 * @param bodyType        the requested {@code BODY} type, or {@code null}
 * @param size            the declared message size in bytes
 * @param smtpUtf8        whether the {@code SMTPUTF8} extension is requested
 * @param requireTls      whether the {@code REQUIRETLS} extension is requested
 * @param deliveryBy      the DELIVERBY parameter, or {@code null}
 * @param envId           the envelope identifier ({@code ENVID}), or {@code null}
 * @param dsnParams       the DSN envelope parameters, or {@code null}
 * @param auth            the AUTH identity, or {@code null}
 * @param extensionParams additional extension parameters by name; a {@code null}
 *                        value is replaced with an empty map
 */
public record Envelope(
        Path from,
        List<Recipient> to,
        BodyType bodyType,
        long size,
        boolean smtpUtf8,
        boolean requireTls,
        DeliveryBy deliveryBy,
        String envId,
        DSNEnvelopeParams dsnParams,
        String auth,
        Map<String, String> extensionParams) {

    /**
     * Creates an envelope, copying the recipient list and extension parameters.
     */
    public Envelope {
        to = to == null ? List.of() : List.copyOf(to);
        extensionParams = extensionParams == null ? Map.of() : Map.copyOf(extensionParams);
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
        private List<Recipient> to = List.of();
        private BodyType bodyType;
        private long size;
        private boolean smtpUtf8;
        private boolean requireTls;
        private DeliveryBy deliveryBy;
        private String envId;
        private DSNEnvelopeParams dsnParams;
        private String auth;
        private Map<String, String> extensionParams = Map.of();

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
