package com.mxraven.mail.model;

import java.util.List;

/**
 * An SMTP path: a mailbox together with any source route (RFC 5321 §4.1.2).
 *
 * @param mailbox      the mailbox; a {@code null} value is replaced with an
 *                     empty mailbox
 * @param sourceRoutes the source route domains; a {@code null} value is replaced
 *                     with an empty list
 */
public record Path(MailboxAddress mailbox, List<String> sourceRoutes) {
    /**
     * Creates a path, replacing {@code null} fields with empty defaults.
     */
    public Path {
        mailbox = mailbox == null ? new MailboxAddress("", "", null) : mailbox;
        sourceRoutes = sourceRoutes == null ? List.of() : List.copyOf(sourceRoutes);
    }

    /**
     * Creates a path from a mailbox with no source route.
     *
     * @param mailbox the mailbox
     * @return the new path
     */
    public static Path of(MailboxAddress mailbox) {
        return new Path(mailbox, List.of());
    }

    /**
     * Creates a path by parsing an address string, with no source route.
     *
     * @param address the address string
     * @return the new path
     */
    public static Path of(String address) {
        return of(MailboxAddress.of(address));
    }

    /**
     * Creates the null reverse path, {@code <>}.
     *
     * @return a path with an empty mailbox
     */
    public static Path nullPath() {
        return new Path(new MailboxAddress("", "", null), List.of());
    }

    /**
     * Whether this path is the null path, having an empty mailbox.
     *
     * @return {@code true} when both the local part and domain are empty
     */
    public boolean isNull() {
        return mailbox.localPart().isEmpty() && mailbox.domain().isEmpty();
    }

    @Override
    public String toString() {
        return isNull() ? "<>" : "<" + mailbox + ">";
    }
}
