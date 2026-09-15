package com.mxraven.admin.model;

import java.util.List;

/**
 * A submission listener's complete sending-domain policy.
 *
 * <p>Runtime sending requires each granted domain to be {@code verified}.
 *
 * @param listenerId identifier of the owning listener
 * @param grants     domains the listener is authorized to send from
 */
public record SendingDomainPolicy(
        String listenerId,
        List<SendingDomainPolicyGrant> grants) {
}
