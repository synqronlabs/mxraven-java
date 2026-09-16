package com.mxraven.mail.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The DSN parameters carried on the SMTP envelope (RFC 3461): the {@code RET}
 * value.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class DSNEnvelopeParams {
    private final String ret;

    /** the {@code RET} value selecting how much of the original message is returned, for example {@code FULL} or {@code HDRS} */
    public String ret() {
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DSNEnvelopeParams that = (DSNEnvelopeParams) o;
        return Objects.equals(this.ret, that.ret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ret);
    }

    @Override
    public String toString() {
        return "DSNEnvelopeParams[" + "ret=" + this.ret + "]";
    }

    /**
     * Creates a new DSNEnvelopeParams.
     *
     * @param ret the {@code RET} value selecting how much of the original message is returned, for example {@code FULL} or {@code HDRS}
     */
    @JsonCreator
    public DSNEnvelopeParams(String ret) {
        this.ret = ret;
    }
}
