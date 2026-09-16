package com.mxraven.mail.mime;

import com.mxraven.mail.internal.Java8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

/**
 * Common media types, with helpers to resolve one from a filename or path.
 *
 * <p>Use {@link #wire()} for the header value. Types not listed here can still
 * be passed to the builder as a raw string.
 */
public enum MimeType {
    /** {@code text/plain}. */
    TEXT_PLAIN("text/plain"),
    /** {@code text/html}. */
    TEXT_HTML("text/html"),
    /** {@code text/css}. */
    TEXT_CSS("text/css"),
    /** {@code text/csv}. */
    TEXT_CSV("text/csv"),
    /** {@code text/markdown}. */
    TEXT_MARKDOWN("text/markdown"),
    /** {@code text/calendar}. */
    TEXT_CALENDAR("text/calendar"),
    /** {@code text/vcard}. */
    TEXT_VCARD("text/vcard"),

    /** {@code application/json}. */
    APPLICATION_JSON("application/json"),
    /** {@code application/xml}. */
    APPLICATION_XML("application/xml"),
    /** {@code application/pdf}. */
    APPLICATION_PDF("application/pdf"),
    /** {@code application/zip}. */
    APPLICATION_ZIP("application/zip"),
    /** {@code application/gzip}. */
    APPLICATION_GZIP("application/gzip"),
    /** {@code application/x-tar}. */
    APPLICATION_TAR("application/x-tar"),
    /** {@code application/octet-stream}, the fallback media type. */
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    /** {@code application/msword}. */
    APPLICATION_MSWORD("application/msword"),
    /** {@code application/vnd.openxmlformats-officedocument.wordprocessingml.document}. */
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    /** {@code application/vnd.ms-excel}. */
    APPLICATION_XLS("application/vnd.ms-excel"),
    /** {@code application/vnd.openxmlformats-officedocument.spreadsheetml.sheet}. */
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    /** {@code application/vnd.ms-powerpoint}. */
    APPLICATION_PPT("application/vnd.ms-powerpoint"),
    /** {@code application/vnd.openxmlformats-officedocument.presentationml.presentation}. */
    APPLICATION_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    /** {@code application/vnd.oasis.opendocument.text}. */
    APPLICATION_ODT("application/vnd.oasis.opendocument.text"),
    /** {@code application/vnd.oasis.opendocument.spreadsheet}. */
    APPLICATION_ODS("application/vnd.oasis.opendocument.spreadsheet"),

    /** {@code image/png}. */
    IMAGE_PNG("image/png"),
    /** {@code image/jpeg}. */
    IMAGE_JPEG("image/jpeg"),
    /** {@code image/gif}. */
    IMAGE_GIF("image/gif"),
    /** {@code image/webp}. */
    IMAGE_WEBP("image/webp"),
    /** {@code image/bmp}. */
    IMAGE_BMP("image/bmp"),
    /** {@code image/svg+xml}. */
    IMAGE_SVG("image/svg+xml"),
    /** {@code image/x-icon}. */
    IMAGE_ICON("image/x-icon"),

    /** {@code audio/mpeg}. */
    AUDIO_MPEG("audio/mpeg"),
    /** {@code audio/wav}. */
    AUDIO_WAV("audio/wav"),
    /** {@code audio/ogg}. */
    AUDIO_OGG("audio/ogg"),

    /** {@code video/mp4}. */
    VIDEO_MP4("video/mp4"),
    /** {@code video/webm}. */
    VIDEO_WEBM("video/webm"),

    /** {@code message/rfc822}. */
    MESSAGE_RFC822("message/rfc822");

    private final String wire;

    MimeType(String wire) {
        this.wire = wire;
    }

    /**
     * The media type value used in a {@code Content-Type} header.
     *
     * @return the wire value
     */
    public String wire() {
        return wire;
    }

    /**
     * Resolves a media type from its wire value, when it is known. Any parameters
     * after a semicolon are ignored.
     *
     * @param value the wire value, or {@code null}
     * @return the matching media type, or empty when unknown
     */
    public static Optional<MimeType> fromWire(String value) {
        if (value == null) {
            return Optional.empty();
        }
        String mediaType = value.trim().toLowerCase(Locale.ROOT);
        int semicolon = mediaType.indexOf(';');
        if (semicolon >= 0) {
            mediaType = mediaType.substring(0, semicolon).trim();
        }
        for (MimeType candidate : values()) {
            if (candidate.wire.equals(mediaType)) {
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }

    /**
     * Guesses a media type from a filename extension, ignoring case.
     *
     * @param filename the filename, or {@code null}
     * @return the guessed media type, or {@link #APPLICATION_OCTET_STREAM} when the
     *         extension is missing or unknown
     */
    public static MimeType fromFilename(String filename) {
        if (filename == null) {
            return APPLICATION_OCTET_STREAM;
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return APPLICATION_OCTET_STREAM;
        }
        String extension = filename.substring(dot + 1).toLowerCase(Locale.ROOT);
        switch (extension) {
            case "txt":
            case "text":
            case "log":
                return TEXT_PLAIN;
            case "html":
            case "htm":
                return TEXT_HTML;
            case "css":
                return TEXT_CSS;
            case "csv":
                return TEXT_CSV;
            case "md":
            case "markdown":
                return TEXT_MARKDOWN;
            case "ics":
                return TEXT_CALENDAR;
            case "vcf":
                return TEXT_VCARD;
            case "json":
                return APPLICATION_JSON;
            case "xml":
                return APPLICATION_XML;
            case "pdf":
                return APPLICATION_PDF;
            case "zip":
                return APPLICATION_ZIP;
            case "gz":
                return APPLICATION_GZIP;
            case "tar":
                return APPLICATION_TAR;
            case "doc":
                return APPLICATION_MSWORD;
            case "docx":
                return APPLICATION_DOCX;
            case "xls":
                return APPLICATION_XLS;
            case "xlsx":
                return APPLICATION_XLSX;
            case "ppt":
                return APPLICATION_PPT;
            case "pptx":
                return APPLICATION_PPTX;
            case "odt":
                return APPLICATION_ODT;
            case "ods":
                return APPLICATION_ODS;
            case "png":
                return IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return IMAGE_JPEG;
            case "gif":
                return IMAGE_GIF;
            case "webp":
                return IMAGE_WEBP;
            case "bmp":
                return IMAGE_BMP;
            case "svg":
                return IMAGE_SVG;
            case "ico":
                return IMAGE_ICON;
            case "mp3":
                return AUDIO_MPEG;
            case "wav":
                return AUDIO_WAV;
            case "ogg":
                return AUDIO_OGG;
            case "mp4":
                return VIDEO_MP4;
            case "webm":
                return VIDEO_WEBM;
            case "eml":
                return MESSAGE_RFC822;
            default:
                return APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * Guesses a media type from a path, consulting the platform as a fallback when
     * the filename extension is unknown.
     *
     * @param path the path to inspect
     * @return the guessed media type, or {@link #APPLICATION_OCTET_STREAM} when
     *         unknown
     */
    public static MimeType fromPath(Path path) {
        MimeType byName = fromFilename(path.getFileName().toString());
        if (byName != APPLICATION_OCTET_STREAM) {
            return byName;
        }
        try {
            String probed = Files.probeContentType(path);
            if (probed != null && !Java8.isBlank(probed)) {
                return fromWire(probed).orElse(APPLICATION_OCTET_STREAM);
            }
        } catch (IOException ignored) {
            // Fall through to the default.
        }
        return APPLICATION_OCTET_STREAM;
    }
}
