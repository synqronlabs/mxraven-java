package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One customer-facing DNS instruction required to onboard a domain.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DnsInstructionRecord {
    private final String purpose;
    private final String name;
    private final DnsRecordType type;
    private final String value;
    private final Integer priority;
    private final Boolean verified;

    /** optional human-readable purpose */
    public String purpose() {
        return purpose;
    }

    /** record host/name */
    public String name() {
        return name;
    }

    /** one of {@code CNAME}, {@code TXT}, or {@code MX} */
    public DnsRecordType type() {
        return type;
    }

    /** record value/target */
    public String value() {
        return value;
    }

    /** optional MX priority */
    public Integer priority() {
        return priority;
    }

    /** whether the record is currently verified */
    public Boolean verified() {
        return verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DnsInstructionRecord that = (DnsInstructionRecord) o;
        return Objects.equals(this.purpose, that.purpose)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.type, that.type)
                && Objects.equals(this.value, that.value)
                && Objects.equals(this.priority, that.priority)
                && Objects.equals(this.verified, that.verified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.purpose, this.name, this.type, this.value, this.priority, this.verified);
    }

    @Override
    public String toString() {
        return "DnsInstructionRecord[" + "purpose=" + this.purpose + ", " + "name=" + this.name + ", " + "type=" + this.type + ", " + "value=" + this.value + ", " + "priority=" + this.priority + ", " + "verified=" + this.verified + "]";
    }

    /**
     * Creates a new DnsInstructionRecord.
     *
     * @param purpose optional human-readable purpose
     * @param name record host/name
     * @param type one of {@code CNAME}, {@code TXT}, or {@code MX}
     * @param value record value/target
     * @param priority optional MX priority
     * @param verified whether the record is currently verified
     */
    @JsonCreator
    public DnsInstructionRecord(String purpose, String name, DnsRecordType type, String value, Integer priority, Boolean verified) {
        this.purpose = purpose;
        this.name = name;
        this.type = type;
        this.value = value;
        this.priority = priority;
        this.verified = verified;
    }
}
