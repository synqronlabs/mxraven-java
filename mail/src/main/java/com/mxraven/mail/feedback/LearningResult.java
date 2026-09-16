package com.mxraven.mail.feedback;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The outcome of a successful learning request.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class LearningResult {
    private final String status;
    private final Disposition disposition;
    private final String tenantId;
    private final String listenerId;
    private final String matchedHashKind;

    /** the service status, normally {@code "learned"} */
    public String status() {
        return status;
    }

    /** the training label that was applied */
    public Disposition disposition() {
        return disposition;
    }

    /** the tenant that owns the matched message */
    public String tenantId() {
        return tenantId;
    }

    /** the listener that processed the matched message */
    public String listenerId() {
        return listenerId;
    }

    /** which stored hash matched the submitted bytes; {@code rendered_eml_sha256} or {@code accepted_eml_sha256} */
    public String matchedHashKind() {
        return matchedHashKind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LearningResult that = (LearningResult) o;
        return Objects.equals(this.status, that.status)
                && Objects.equals(this.disposition, that.disposition)
                && Objects.equals(this.tenantId, that.tenantId)
                && Objects.equals(this.listenerId, that.listenerId)
                && Objects.equals(this.matchedHashKind, that.matchedHashKind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.disposition, this.tenantId, this.listenerId, this.matchedHashKind);
    }

    @Override
    public String toString() {
        return "LearningResult[" + "status=" + this.status + ", " + "disposition=" + this.disposition + ", " + "tenantId=" + this.tenantId + ", " + "listenerId=" + this.listenerId + ", " + "matchedHashKind=" + this.matchedHashKind + "]";
    }

    /**
     * Creates a new LearningResult.
     *
     * @param status the service status, normally {@code "learned"}
     * @param disposition the training label that was applied
     * @param tenantId the tenant that owns the matched message
     * @param listenerId the listener that processed the matched message
     * @param matchedHashKind which stored hash matched the submitted bytes; {@code rendered_eml_sha256} or {@code accepted_eml_sha256}
     */
    @JsonCreator
    public LearningResult(String status, Disposition disposition, String tenantId, String listenerId, String matchedHashKind) {
        this.status = status;
        this.disposition = disposition;
        this.tenantId = tenantId;
        this.listenerId = listenerId;
        this.matchedHashKind = matchedHashKind;
    }
}
