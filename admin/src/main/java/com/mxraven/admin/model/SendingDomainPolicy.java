package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.util.List;

/**
 * A submission listener's complete sending-domain policy.
 *
 * <p>Runtime sending requires each granted domain to be {@code verified}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SendingDomainPolicy {
    private final String listenerId;
    private final List<SendingDomainPolicyGrant> grants;

    /** identifier of the owning listener */
    public String listenerId() {
        return listenerId;
    }

    /** domains the listener is authorized to send from */
    public List<SendingDomainPolicyGrant> grants() {
        return grants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendingDomainPolicy that = (SendingDomainPolicy) o;
        return Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.grants, that.grants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.listenerId, this.grants);
    }

    @Override
    public String toString() {
        return "SendingDomainPolicy[" + "listenerId=" + this.listenerId + ", " + "grants=" + this.grants + "]";
    }

    /**
     * Creates a new SendingDomainPolicy.
     *
     * @param listenerId identifier of the owning listener
     * @param grants domains the listener is authorized to send from
     */
    @JsonCreator
    public SendingDomainPolicy(String listenerId, List<SendingDomainPolicyGrant> grants) {
        this.listenerId = listenerId;
        this.grants = grants;
    }
}
