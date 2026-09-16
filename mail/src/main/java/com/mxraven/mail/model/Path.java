package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * An SMTP path: a mailbox together with any source route (RFC 5321 §4.1.2).
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Path {
    private final MailboxAddress mailbox;
    private final List<String> sourceRoutes;

    /** the mailbox; a {@code null} value is replaced with an empty mailbox */
    public MailboxAddress mailbox() {
        return mailbox;
    }

    /** the source route domains; a {@code null} value is replaced with an empty list */
    public List<String> sourceRoutes() {
        return sourceRoutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path that = (Path) o;
        return Objects.equals(this.mailbox, that.mailbox)
                && Objects.equals(this.sourceRoutes, that.sourceRoutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mailbox, this.sourceRoutes);
    }

    /**
     * Creates a path, replacing {@code null} fields with empty defaults.
     */
    @JsonCreator
    public Path(MailboxAddress mailbox, List<String> sourceRoutes) {

        mailbox = mailbox == null ? new MailboxAddress("", "", null) : mailbox;
        sourceRoutes = sourceRoutes == null ? Java8.list() : Java8.copyList(sourceRoutes);
    
        this.mailbox = mailbox;
        this.sourceRoutes = sourceRoutes;
    }

    /**
     * Creates a path from a mailbox with no source route.
     *
     * @param mailbox the mailbox
     * @return the new path
     */
    public static Path of(MailboxAddress mailbox) {
        return new Path(mailbox, Java8.list());
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
        return new Path(new MailboxAddress("", "", null), Java8.list());
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
