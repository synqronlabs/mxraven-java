package com.mxraven.admin.model;

/**
 * One header operation in a {@code MODIFY_HEADER} routing-rule action.
 *
 * <p>{@code value} is {@code null} for {@link ModifyHeaderOp#REMOVE}.
 */
public record ModifyHeaderOperation(ModifyHeaderOp op, String header, String value) {

    public static ModifyHeaderOperation append(String header, String value) {
        return new ModifyHeaderOperation(ModifyHeaderOp.APPEND, header, value);
    }

    public static ModifyHeaderOperation set(String header, String value) {
        return new ModifyHeaderOperation(ModifyHeaderOp.SET, header, value);
    }

    public static ModifyHeaderOperation remove(String header) {
        return new ModifyHeaderOperation(ModifyHeaderOp.REMOVE, header, null);
    }
}
