package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * Presence and length metadata for a masked template field.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MaskedTemplateField {
    private final boolean present;
    private final int length;

    /** whether the field is present */
    public boolean present() {
        return present;
    }

    /** length of the field value */
    public int length() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaskedTemplateField that = (MaskedTemplateField) o;
        return this.present == that.present
                && this.length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.present, this.length);
    }

    @Override
    public String toString() {
        return "MaskedTemplateField[" + "present=" + this.present + ", " + "length=" + this.length + "]";
    }

    /**
     * Creates a new MaskedTemplateField.
     *
     * @param present whether the field is present
     * @param length length of the field value
     */
    @JsonCreator
    public MaskedTemplateField(boolean present, int length) {
        this.present = present;
        this.length = length;
    }
}
