package com.mxraven.mail.model;

/**
 * An RFC 5322 mailbox: a local part, a domain, and an optional display name.
 *
 * @param localPart   the part before the {@code @}; a {@code null} value is
 *                    replaced with {@code ""}
 * @param domain      the part after the {@code @}; a {@code null} value is
 *                    replaced with {@code ""}
 * @param displayName the display name, or {@code null}
 */
public record MailboxAddress(String localPart, String domain, String displayName) {
    /**
     * Creates a mailbox address, replacing {@code null} parts with empty strings.
     *
     * @throws IllegalArgumentException when any component contains CR, LF, or NUL
     */
    public MailboxAddress {
        localPart = localPart == null ? "" : localPart;
        domain = domain == null ? "" : domain;
        rejectInjection(localPart, "local part");
        rejectInjection(domain, "domain");
        rejectInjection(displayName, "display name");
    }

    private static void rejectInjection(String text, String what) {
        if (text == null) {
            return;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\r' || c == '\n' || c == '\0') {
                throw new IllegalArgumentException(what + " must not contain CR, LF, or NUL characters");
            }
        }
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
     * <p>A non-blank address must contain a non-empty local part and domain
     * separated by {@code @}. An address such as {@code "not an address"} is
     * rejected rather than silently parsed into an empty domain.
     *
     * @param address the address string, or {@code null}
     * @return the parsed address, with empty parts when {@code address} is
     *         {@code null} or blank
     * @throws IllegalArgumentException when {@code address} is non-blank but not a
     *                                  valid {@code local@domain} mailbox
     */
    public static MailboxAddress of(String address) {
        if (address == null || address.isBlank()) {
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
        if (at <= 0 || at == value.length() - 1) {
            throw new IllegalArgumentException("invalid email address: " + address);
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
