package com.mxraven.mail.model;

/**
 * An envelope recipient: a forward path and its optional DSN parameters.
 *
 * @param address   the recipient path
 * @param dsnParams the recipient DSN parameters, or {@code null}
 */
public record Recipient(Path address, DSNRecipientParams dsnParams) {
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
