package com.mxraven.mail.model;

import java.util.List;

public record DSNRecipientParams(List<String> notifyFlags, String orcpt) {
    public DSNRecipientParams {
        notifyFlags = notifyFlags == null ? List.of() : List.copyOf(notifyFlags);
    }
}
