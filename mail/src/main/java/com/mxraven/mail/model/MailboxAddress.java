package com.mxraven.mail.model;

public record MailboxAddress(String localPart, String domain, String displayName) {
    public MailboxAddress {
        localPart = localPart == null ? "" : localPart;
        domain = domain == null ? "" : domain;
    }

    public static MailboxAddress of(String localPart, String domain) {
        return new MailboxAddress(localPart, domain, null);
    }

    /** Builds an address from its local part and domain. */
    public static MailboxAddress ofLocalDomain(String localPart, String domain) {
        return new MailboxAddress(localPart, domain, null);
    }

    /** Builds an address with an explicit display name. */
    public static MailboxAddress withDisplayName(String localPart, String domain, String displayName) {
        return new MailboxAddress(localPart, domain, displayName);
    }

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
