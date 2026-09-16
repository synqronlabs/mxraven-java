package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Wire representation backing the {@link RecipientSetMember} entity.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class RecipientSetMemberData {
    private final String emailAddress;
    private final String addedAt;

    /** member email address */
    public String emailAddress() {
        return emailAddress;
    }

    /** time the member was added */
    public String addedAt() {
        return addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipientSetMemberData that = (RecipientSetMemberData) o;
        return Objects.equals(this.emailAddress, that.emailAddress)
                && Objects.equals(this.addedAt, that.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.emailAddress, this.addedAt);
    }

    @Override
    public String toString() {
        return "RecipientSetMemberData[" + "emailAddress=" + this.emailAddress + ", " + "addedAt=" + this.addedAt + "]";
    }

    /**
     * Creates a new RecipientSetMemberData.
     *
     * @param emailAddress member email address
     * @param addedAt time the member was added
     */
    @JsonCreator
    public RecipientSetMemberData(String emailAddress, String addedAt) {
        this.emailAddress = emailAddress;
        this.addedAt = addedAt;
    }
}
