package com.mxraven.mail.model;

import com.mxraven.mail.internal.Java8;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * The DSN parameters carried on an envelope recipient (RFC 3461): the
 * {@code NOTIFY} flags and the optional {@code ORCPT} address.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DSNRecipientParams {
    private final List<String> notifyFlags;
    private final String orcpt;

    /** the {@code NOTIFY} flags; a {@code null} value is replaced with an empty list */
    public List<String> notifyFlags() {
        return notifyFlags;
    }

    /** the original recipient address for {@code ORCPT}, or {@code null} */
    public String orcpt() {
        return orcpt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DSNRecipientParams that = (DSNRecipientParams) o;
        return Objects.equals(this.notifyFlags, that.notifyFlags)
                && Objects.equals(this.orcpt, that.orcpt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.notifyFlags, this.orcpt);
    }

    @Override
    public String toString() {
        return "DSNRecipientParams[" + "notifyFlags=" + this.notifyFlags + ", " + "orcpt=" + this.orcpt + "]";
    }

    /**
     * Creates DSN recipient parameters, copying the notify flags.
     */
    @JsonCreator
    public DSNRecipientParams(List<String> notifyFlags, String orcpt) {

        notifyFlags = notifyFlags == null ? Java8.list() : Java8.copyList(notifyFlags);
    
        this.notifyFlags = notifyFlags;
        this.orcpt = orcpt;
    }
}
