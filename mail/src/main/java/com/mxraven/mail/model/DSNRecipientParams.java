package com.mxraven.mail.model;

import java.util.List;

/**
 * The DSN parameters carried on an envelope recipient (RFC 3461): the
 * {@code NOTIFY} flags and the optional {@code ORCPT} address.
 *
 * @param notifyFlags the {@code NOTIFY} flags; a {@code null} value is replaced
 *                    with an empty list
 * @param orcpt       the original recipient address for {@code ORCPT}, or
 *                    {@code null}
 */
public record DSNRecipientParams(List<String> notifyFlags, String orcpt) {
    /**
     * Creates DSN recipient parameters, copying the notify flags.
     */
    public DSNRecipientParams {
        notifyFlags = notifyFlags == null ? List.of() : List.copyOf(notifyFlags);
    }
}
