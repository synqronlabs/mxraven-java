package com.mxraven.mail.mime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A decoded MIME attachment.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Attachment {
    private final String filename;
    private final String contentType;
    private final boolean inline;
    private final String contentId;
    private final byte[] data;

    /** the decoded filename, or {@code ""} when none was supplied */
    public String filename() {
        return filename;
    }

    /** the declared media type */
    public String contentType() {
        return contentType;
    }

    /** whether the part was {@code Content-Disposition: inline} */
    public boolean inline() {
        return inline;
    }

    /** the {@code Content-ID} (without angle brackets), or {@code null} */
    public String contentId() {
        return contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attachment that = (Attachment) o;
        return Objects.equals(this.filename, that.filename)
                && Objects.equals(this.contentType, that.contentType)
                && this.inline == that.inline
                && Objects.equals(this.contentId, that.contentId)
                && Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.filename, this.contentType, this.inline, this.contentId, this.data);
    }

    @Override
    public String toString() {
        return "Attachment[" + "filename=" + this.filename + ", " + "contentType=" + this.contentType + ", " + "inline=" + this.inline + ", " + "contentId=" + this.contentId + ", " + "data=" + this.data + "]";
    }

    /**
     * Creates an attachment, replacing {@code null} fields with empty defaults.
     */
    @JsonCreator
    public Attachment(String filename, String contentType, boolean inline, String contentId, byte[] data) {

        filename = filename == null ? "" : filename;
        contentType = contentType == null ? "application/octet-stream" : contentType;
        data = data == null ? new byte[0] : data.clone();
    
        this.filename = filename;
        this.contentType = contentType;
        this.inline = inline;
        this.contentId = contentId;
        this.data = data;
    }

    public byte[] data() {
        return data.clone();
    }

    /**
     * The decoded content length in bytes.
     *
     * @return the content length
     */
    public long size() {
        return data.length;
    }

    /**
     * Writes the attachment to {@code destination}. When {@code destination} is an
     * existing directory and the attachment has a filename, the file is written
     * inside it under that name.
     *
     * @param destination the target file or directory
     * @return the path actually written
     * @throws IOException if the file cannot be written
     */
    public Path download(Path destination) throws IOException {
        Path target = Files.isDirectory(destination) && !filename.isEmpty()
                ? destination.resolve(filename)
                : destination;
        Files.write(target, data);
        return target;
    }

    /**
     * Writes the attachment to {@code destination}.
     *
     * @param destination the target file or directory
     * @return the path actually written
     * @throws IOException if the file cannot be written
     */
    public Path download(File destination) throws IOException {
        return download(destination.toPath());
    }

    /**
     * Writes the attachment to {@code destination}.
     *
     * @param destination the target file or directory
     * @return the path actually written
     * @throws IOException if the file cannot be written
     */
    public Path download(String destination) throws IOException {
        return download(Paths.get(destination));
    }

    /**
     * The decoded content as a stream. Callers own the returned stream.
     *
     * @return a stream over the decoded content
     */
    public InputStream openStream() {
        return new ByteArrayInputStream(data);
    }

    /**
     * Writes the decoded content to {@code out} without closing it.
     *
     * @param out the stream to write to
     * @throws IOException if writing fails
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(data);
    }
}
