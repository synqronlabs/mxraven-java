package com.mxraven.admin.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

/**
 * One synthesized attachment's metadata in a simulation fixture. Its content is
 * never needed: only what {@code mail.body:attachments()} reports.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class SimulationAttachment {
    private final String filename;
    private final String contentType;
    private final Long sizeBytes;
    private final Boolean inline;

    /** the attachment filename */
    public String filename() {
        return filename;
    }

    /** the attachment content type */
    public String contentType() {
        return contentType;
    }

    /** the attachment size in bytes */
    public Long sizeBytes() {
        return sizeBytes;
    }

    public Boolean inline() {
        return inline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimulationAttachment that = (SimulationAttachment) o;
        return Objects.equals(this.filename, that.filename) && Objects.equals(this.contentType, that.contentType) && Objects.equals(this.sizeBytes, that.sizeBytes) && Objects.equals(this.inline, that.inline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.filename, this.contentType, this.sizeBytes, this.inline);
    }

    @Override
    public String toString() {
        return "SimulationAttachment[" + "filename=" + this.filename + ", " + "contentType=" + this.contentType + ", " + "sizeBytes=" + this.sizeBytes + ", " + "inline=" + this.inline + "]";
    }

    /**
     * Creates a new SimulationAttachment.
     *
     * @param filename the attachment filename
     * @param contentType the attachment content type
     * @param sizeBytes the attachment size in bytes
     * @param inline inline
     */
    @JsonCreator
    public SimulationAttachment(String filename, String contentType, Long sizeBytes, Boolean inline) {
        this.filename = filename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.inline = inline;
    }
}
