package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One header operation in a {@code MODIFY_HEADER} routing-rule action.
 *
 * <p>{@code value} is {@code null} for {@link ModifyHeaderOp#REMOVE}.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ModifyHeaderOperation {
    private final ModifyHeaderOp op;
    private final String header;
    private final String value;

    /** operation to perform */
    public ModifyHeaderOp op() {
        return op;
    }

    /** name of the affected header */
    public String header() {
        return header;
    }

    /** header value, or {@code null} for {@link ModifyHeaderOp#REMOVE} */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModifyHeaderOperation that = (ModifyHeaderOperation) o;
        return Objects.equals(this.op, that.op)
                && Objects.equals(this.header, that.header)
                && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.op, this.header, this.value);
    }

    @Override
    public String toString() {
        return "ModifyHeaderOperation[" + "op=" + this.op + ", " + "header=" + this.header + ", " + "value=" + this.value + "]";
    }

    /**
     * Creates a new ModifyHeaderOperation.
     *
     * @param op operation to perform
     * @param header name of the affected header
     * @param value header value, or {@code null} for {@link ModifyHeaderOp#REMOVE}
     */
    @JsonCreator
    public ModifyHeaderOperation(ModifyHeaderOp op, String header, String value) {
        this.op = op;
        this.header = header;
        this.value = value;
    }

    /**
     * Creates an append operation.
     *
     * @param header name of the header to append to
     * @param value  value to append
     * @return an append operation
     */
    public static ModifyHeaderOperation append(String header, String value) {
        return new ModifyHeaderOperation(ModifyHeaderOp.APPEND, header, value);
    }

    /**
     * Creates a set operation.
     *
     * @param header name of the header to set
     * @param value  value to set
     * @return a set operation
     */
    public static ModifyHeaderOperation set(String header, String value) {
        return new ModifyHeaderOperation(ModifyHeaderOp.SET, header, value);
    }

    /**
     * Creates a remove operation.
     *
     * @param header name of the header to remove
     * @return a remove operation
     */
    public static ModifyHeaderOperation remove(String header) {
        return new ModifyHeaderOperation(ModifyHeaderOp.REMOVE, header, null);
    }
}
