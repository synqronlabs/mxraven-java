package com.mxraven.admin.model;

/** Wire representation backing the {@link RecipientSetMember} entity. */
public record RecipientSetMemberData(
        String emailAddress,
        String addedAt) {
}
