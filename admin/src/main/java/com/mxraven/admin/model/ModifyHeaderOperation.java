package com.mxraven.admin.model;

/**
 * One header operation in a {@code MODIFY_HEADER} routing-rule action.
 *
 * <p>{@code value} is {@code null} for {@link ModifyHeaderOp#REMOVE}.
 *
 * @param op     operation to perform
 * @param header name of the affected header
 * @param value  header value, or {@code null} for {@link ModifyHeaderOp#REMOVE}
 */
public record ModifyHeaderOperation(ModifyHeaderOp op, String header, String value) {

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
