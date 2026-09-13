package com.mxraven.mail.mime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A decoded MIME attachment.
 *
 * @param filename    the decoded filename, or {@code ""} when none was supplied
 * @param contentType the declared media type
 * @param inline      whether the part was {@code Content-Disposition: inline}
 * @param contentId   the {@code Content-ID} (without angle brackets), or {@code null}
 * @param data        the transfer-decoded content
 */
public record Attachment(String filename, String contentType, boolean inline, String contentId, byte[] data) {

    public Attachment {
        filename = filename == null ? "" : filename;
        contentType = contentType == null ? "application/octet-stream" : contentType;
        data = data == null ? new byte[0] : data.clone();
    }

    @Override
    public byte[] data() {
        return data.clone();
    }

    /** The decoded content length in bytes. */
    public long size() {
        return data.length;
    }

    /**
     * Writes the attachment to {@code destination}. When {@code destination} is an
     * existing directory and the attachment has a filename, the file is written
     * inside it under that name.
     *
     * @return the path actually written
     */
    public Path download(Path destination) throws IOException {
        Path target = Files.isDirectory(destination) && !filename.isEmpty()
                ? destination.resolve(filename)
                : destination;
        Files.write(target, data);
        return target;
    }

    /** Writes the attachment to {@code destination}. */
    public Path download(File destination) throws IOException {
        return download(destination.toPath());
    }

    /** Writes the attachment to {@code destination}. */
    public Path download(String destination) throws IOException {
        return download(Path.of(destination));
    }

    /** The decoded content as a stream. Callers own the returned stream. */
    public InputStream openStream() {
        return new ByteArrayInputStream(data);
    }

    /** Writes the decoded content to {@code out} without closing it. */
    public void writeTo(OutputStream out) throws IOException {
        out.write(data);
    }
}
