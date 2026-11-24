package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * The principal that approved a connected-application installation.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ConnectedAppGrantor {
    private final String issuer;
    private final String subject;
    private final String email;

    /** identity-provider issuer */
    public String issuer() {
        return issuer;
    }

    /** identity-provider subject */
    public String subject() {
        return subject;
    }

    public String email() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectedAppGrantor that = (ConnectedAppGrantor) o;
        return Objects.equals(this.issuer, that.issuer) && Objects.equals(this.subject, that.subject) && Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.issuer, this.subject, this.email);
    }

    @Override
    public String toString() {
        return "ConnectedAppGrantor[" + "issuer=" + this.issuer + ", " + "subject=" + this.subject + ", " + "email=" + this.email + "]";
    }

    /**
     * Creates a new ConnectedAppGrantor.
     *
     * @param issuer identity-provider issuer
     * @param subject identity-provider subject
     * @param email email
     */
    @JsonCreator
    public ConnectedAppGrantor(String issuer, String subject, String email) {
        this.issuer = issuer;
        this.subject = subject;
        this.email = email;
    }
}
