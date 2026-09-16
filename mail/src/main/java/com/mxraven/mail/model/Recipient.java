package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * An envelope recipient: a forward path and its optional DSN parameters.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Recipient {
    private final Path address;
    private final DSNRecipientParams dsnParams;

    /** the recipient path */
    public Path address() {
        return address;
    }

    /** the recipient DSN parameters, or {@code null} */
    public DSNRecipientParams dsnParams() {
        return dsnParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipient that = (Recipient) o;
        return Objects.equals(this.address, that.address)
                && Objects.equals(this.dsnParams, that.dsnParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address, this.dsnParams);
    }

    @Override
    public String toString() {
        return "Recipient[" + "address=" + this.address + ", " + "dsnParams=" + this.dsnParams + "]";
    }

    /**
     * Creates a new Recipient.
     *
     * @param address the recipient path
     * @param dsnParams the recipient DSN parameters, or {@code null}
     */
    @JsonCreator
    public Recipient(Path address, DSNRecipientParams dsnParams) {
        this.address = address;
        this.dsnParams = dsnParams;
    }

    /**
     * Creates a recipient from a path with no DSN parameters.
     *
     * @param address the recipient path
     * @return the new recipient
     */
    public static Recipient of(Path address) {
        return new Recipient(address, null);
    }

    /**
     * Creates a recipient from an address string with no DSN parameters.
     *
     * @param address the recipient address
     * @return the new recipient
     */
    public static Recipient of(String address) {
        return new Recipient(Path.of(address), null);
    }
}
