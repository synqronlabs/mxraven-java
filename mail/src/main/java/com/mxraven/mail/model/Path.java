package com.mxraven.mail.model;

import java.util.List;

public record Path(MailboxAddress mailbox, List<String> sourceRoutes) {
    public Path {
        mailbox = mailbox == null ? new MailboxAddress("", "", null) : mailbox;
        sourceRoutes = sourceRoutes == null ? List.of() : List.copyOf(sourceRoutes);
    }

    public static Path of(MailboxAddress mailbox) {
        return new Path(mailbox, List.of());
    }

    public static Path of(String address) {
        return of(MailboxAddress.of(address));
    }

    public static Path nullPath() {
        return new Path(new MailboxAddress("", "", null), List.of());
    }

    public boolean isNull() {
        return mailbox.localPart().isEmpty() && mailbox.domain().isEmpty();
    }

    @Override
    public String toString() {
        return isNull() ? "<>" : "<" + mailbox + ">";
    }
}
