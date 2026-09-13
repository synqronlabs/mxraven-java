package com.mxraven.mail.model;

public record Recipient(Path address, DSNRecipientParams dsnParams) {
    public static Recipient of(Path address) {
        return new Recipient(address, null);
    }

    public static Recipient of(String address) {
        return new Recipient(Path.of(address), null);
    }
}
