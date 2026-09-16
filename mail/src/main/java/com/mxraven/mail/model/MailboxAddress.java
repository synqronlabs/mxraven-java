package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * An RFC 5322 mailbox: a local part, a domain, and an optional display name.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MailboxAddress {
    private final String localPart;
    private final String domain;
    private final String displayName;

    /** the part before the {@code @}; a {@code null} value is replaced with {@code ""} */
    public String localPart() {
        return localPart;
    }

    /** the part after the {@code @}; a {@code null} value is replaced with {@code ""} */
    public String domain() {
        return domain;
    }

    /** the display name, or {@code null} */
    public String displayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailboxAddress that = (MailboxAddress) o;
        return Objects.equals(this.localPart, that.localPart)
                && Objects.equals(this.domain, that.domain)
                && Objects.equals(this.displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.localPart, this.domain, this.displayName);
    }

    /**
     * Creates a mailbox address, replacing {@code null} parts with empty strings.
     */
    @JsonCreator
    public MailboxAddress(String localPart, String domain, String displayName) {

        localPart = localPart == null ? "" : localPart;
        domain = domain == null ? "" : domain;
    
        this.localPart = localPart;
        this.domain = domain;
        this.displayName = displayName;
    }

    /**
     * Creates an address from its local part and domain.
     *
     * @param localPart the part before the {@code @}
     * @param domain    the part after the {@code @}
     * @return the new address
     */
    public static MailboxAddress of(String localPart, String domain) {
        return new MailboxAddress(localPart, domain, null);
    }

    /**
     * Builds an address from its local part and domain.
     *
     * @param localPart the part before the {@code @}
     * @param domain    the part after the {@code @}
     * @return the new address
     */
    public static MailboxAddress ofLocalDomain(String localPart, String domain) {
        return new MailboxAddress(localPart, domain, null);
    }

    /**
     * Builds an address with an explicit display name.
     *
     * @param localPart   the part before the {@code @}
     * @param domain      the part after the {@code @}
     * @param displayName the display name
     * @return the new address
     */
    public static MailboxAddress withDisplayName(String localPart, String domain, String displayName) {
        return new MailboxAddress(localPart, domain, displayName);
    }

    /**
     * Parses an address string, extracting an optional display name and the
     * address inside angle brackets when present.
     *
     * @param address the address string, or {@code null}
     * @return the parsed address, with empty parts when {@code address} is
     *         {@code null} or blank
     */
    public static MailboxAddress of(String address) {
        if (address == null || Java8.isBlank(address)) {
            return new MailboxAddress("", "", null);
        }
        String value = address.trim();
        String display = null;
        int lt = value.lastIndexOf('<');
        int gt = value.lastIndexOf('>');
        if (lt >= 0 && gt > lt) {
            String inner = value.substring(lt + 1, gt).trim();
            display = value.substring(0, lt).trim();
            if (display.length() >= 2 && display.startsWith("\"") && display.endsWith("\"")) {
                display = display.substring(1, display.length() - 1).replace("\\\"", "\"");
            }
            value = inner;
        }
        String displayName = display == null || display.isEmpty() ? null : display;

        int at = value.lastIndexOf('@');
        if (at < 0) {
            return new MailboxAddress(value, "", displayName);
        }
        return new MailboxAddress(value.substring(0, at), value.substring(at + 1), displayName);
    }

    @Override
    public String toString() {
        if (localPart.isEmpty() && domain.isEmpty()) {
            return "";
        }
        return localPart + "@" + domain;
    }
}
