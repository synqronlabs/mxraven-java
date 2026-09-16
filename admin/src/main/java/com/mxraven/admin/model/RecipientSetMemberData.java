package com.mxraven.admin.model;

/**
 * Wire representation backing the {@link RecipientSetMember} entity.
 *
 * @param emailAddress member email address
 * @param addedAt      time the member was added
 */
public record RecipientSetMemberData(
        String emailAddress,
        String addedAt) {
}
