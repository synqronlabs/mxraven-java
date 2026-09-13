package com.mxraven.mail.model;

import java.util.List;
import java.util.Map;

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

    public Envelope {
        to = to == null ? List.of() : List.copyOf(to);
        extensionParams = extensionParams == null ? Map.of() : Map.copyOf(extensionParams);
    }

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder from(Path from) {
            this.from = from;
            return this;
        }

        public Builder to(List<Recipient> to) {
            this.to = to;
            return this;
        }

        public Builder bodyType(BodyType bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        public Builder size(long size) {
            this.size = size;
            return this;
        }

        public Builder smtpUtf8(boolean smtpUtf8) {
            this.smtpUtf8 = smtpUtf8;
            return this;
        }

        public Builder requireTls(boolean requireTls) {
            this.requireTls = requireTls;
            return this;
        }

        public Builder deliveryBy(DeliveryBy deliveryBy) {
            this.deliveryBy = deliveryBy;
            return this;
        }

        public Builder envId(String envId) {
            this.envId = envId;
            return this;
        }

        public Builder dsnParams(DSNEnvelopeParams dsnParams) {
            this.dsnParams = dsnParams;
            return this;
        }

        public Builder auth(String auth) {
            this.auth = auth;
            return this;
        }

        public Builder extensionParams(Map<String, String> extensionParams) {
            this.extensionParams = extensionParams;
            return this;
        }

        public Envelope build() {
            return new Envelope(from, to, bodyType, size, smtpUtf8, requireTls,
                    deliveryBy, envId, dsnParams, auth, extensionParams);
        }
    }
}
