package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** The kind of effect a routing script staged. */
public enum ScriptIntentKind {
    /** Sets a header to one value. */
    HEADER_SET("header.set"),
    /** Appends a header occurrence. */
    HEADER_APPEND("header.append"),
    /** Removes every occurrence of a header. */
    HEADER_REMOVE("header.remove"),
    /** Adds an envelope recipient (submission listeners only). */
    RECIPIENTS_ADD("recipients.add"),
    /** Archives the message to object storage (MTA listeners only). */
    ARCHIVE_S3("archive.s3"),
    /** Sends a copy to an SMTP forward destination (MTA listeners only). */
    FORWARD_SMTP("forward.smtp"),
    /** Delivers through the platform shared pool. */
    DELIVER_SHARED("deliver.shared"),
    /** Delivers through a dedicated IP pool. */
    DELIVER_DEDICATED("deliver.dedicated"),
    /** Delivers through a smarthost relay. */
    DELIVER_SMARTHOST("deliver.smarthost"),
    /** Relays the message through a smarthost. */
    RELAY_SMTP("relay.smtp"),
    /** Sends an automatic reply from a template. */
    REPLY("reply"),
    /** Accepts and discards the message. */
    DROP("drop"),
    /** Rejects the transaction with a 5xx reply. */
    REJECT("reject"),
    /** Defers the transaction with a 4xx reply. */
    DEFER("defer");

    private final String wire;

    ScriptIntentKind(String wire) {
        this.wire = wire;
    }

    /**
     * The value used on the wire.
     *
     * @return the wire value
     */
    @JsonValue
    public String wire() {
        return wire;
    }

    /**
     * Resolves the constant for a wire value.
     *
     * @param value wire value, or {@code null}
     * @return the matching constant, or {@code null} when {@code value} is {@code null}
     * @throws IllegalArgumentException if the value is unknown
     */
    @JsonCreator
    public static ScriptIntentKind fromWire(String value) {
        if (value == null) {
            return null;
        }
        for (ScriptIntentKind candidate : values()) {
            if (candidate.wire.equals(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("unknown ScriptIntentKind value: " + value);
    }
}
