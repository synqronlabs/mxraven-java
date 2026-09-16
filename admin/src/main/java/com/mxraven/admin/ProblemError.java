package com.mxraven.admin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One actionable validation issue from an RFC 9457 problem response.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ProblemError {
    private final String pointer;
    private final String code;
    private final String detail;

    /** RFC 6901 JSON Pointer to the invalid request value, when applicable */
    public String pointer() {
        return pointer;
    }

    /** stable machine-readable validation issue code */
    public String code() {
        return code;
    }

    /** safe human-readable explanation */
    public String detail() {
        return detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProblemError that = (ProblemError) o;
        return Objects.equals(this.pointer, that.pointer)
                && Objects.equals(this.code, that.code)
                && Objects.equals(this.detail, that.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pointer, this.code, this.detail);
    }

    @Override
    public String toString() {
        return "ProblemError[" + "pointer=" + this.pointer + ", " + "code=" + this.code + ", " + "detail=" + this.detail + "]";
    }

    /**
     * Creates a new ProblemError.
     *
     * @param pointer RFC 6901 JSON Pointer to the invalid request value, when applicable
     * @param code stable machine-readable validation issue code
     * @param detail safe human-readable explanation
     */
    @JsonCreator
    public ProblemError(String pointer, String code, String detail) {
        this.pointer = pointer;
        this.code = code;
        this.detail = detail;
    }
}
