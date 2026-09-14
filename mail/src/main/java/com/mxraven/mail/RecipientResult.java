package com.mxraven.mail;

import com.mxraven.mail.model.Recipient;

public record RecipientResult(Recipient recipient, boolean accepted, String status) {
}
