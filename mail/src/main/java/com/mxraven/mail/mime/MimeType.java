package com.mxraven.mail.mime;

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
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    TEXT_CSV("text/csv"),
    TEXT_MARKDOWN("text/markdown"),
    TEXT_CALENDAR("text/calendar"),
    TEXT_VCARD("text/vcard"),

    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_GZIP("application/gzip"),
    APPLICATION_TAR("application/x-tar"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_MSWORD("application/msword"),
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    APPLICATION_XLS("application/vnd.ms-excel"),
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    APPLICATION_PPT("application/vnd.ms-powerpoint"),
    APPLICATION_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    APPLICATION_ODT("application/vnd.oasis.opendocument.text"),
    APPLICATION_ODS("application/vnd.oasis.opendocument.spreadsheet"),

    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_WEBP("image/webp"),
    IMAGE_BMP("image/bmp"),
    IMAGE_SVG("image/svg+xml"),
    IMAGE_ICON("image/x-icon"),

    AUDIO_MPEG("audio/mpeg"),
    AUDIO_WAV("audio/wav"),
    AUDIO_OGG("audio/ogg"),

    VIDEO_MP4("video/mp4"),
    VIDEO_WEBM("video/webm"),

    MESSAGE_RFC822("message/rfc822");

    private final String wire;

    MimeType(String wire) {
        this.wire = wire;
    }

    /** The media type value used in a {@code Content-Type} header. */
    public String wire() {
        return wire;
    }

    /** Resolves a media type from its wire value, when it is known. */
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

    /** Guesses a media type from a filename extension. */
    public static MimeType fromFilename(String filename) {
        if (filename == null) {
            return APPLICATION_OCTET_STREAM;
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return APPLICATION_OCTET_STREAM;
        }
        String extension = filename.substring(dot + 1).toLowerCase(Locale.ROOT);
        return switch (extension) {
            case "txt", "text", "log" -> TEXT_PLAIN;
            case "html", "htm" -> TEXT_HTML;
            case "css" -> TEXT_CSS;
            case "csv" -> TEXT_CSV;
            case "md", "markdown" -> TEXT_MARKDOWN;
            case "ics" -> TEXT_CALENDAR;
            case "vcf" -> TEXT_VCARD;
            case "json" -> APPLICATION_JSON;
            case "xml" -> APPLICATION_XML;
            case "pdf" -> APPLICATION_PDF;
            case "zip" -> APPLICATION_ZIP;
            case "gz" -> APPLICATION_GZIP;
            case "tar" -> APPLICATION_TAR;
            case "doc" -> APPLICATION_MSWORD;
            case "docx" -> APPLICATION_DOCX;
            case "xls" -> APPLICATION_XLS;
            case "xlsx" -> APPLICATION_XLSX;
            case "ppt" -> APPLICATION_PPT;
            case "pptx" -> APPLICATION_PPTX;
            case "odt" -> APPLICATION_ODT;
            case "ods" -> APPLICATION_ODS;
            case "png" -> IMAGE_PNG;
            case "jpg", "jpeg" -> IMAGE_JPEG;
            case "gif" -> IMAGE_GIF;
            case "webp" -> IMAGE_WEBP;
            case "bmp" -> IMAGE_BMP;
            case "svg" -> IMAGE_SVG;
            case "ico" -> IMAGE_ICON;
            case "mp3" -> AUDIO_MPEG;
            case "wav" -> AUDIO_WAV;
            case "ogg" -> AUDIO_OGG;
            case "mp4" -> VIDEO_MP4;
            case "webm" -> VIDEO_WEBM;
            case "eml" -> MESSAGE_RFC822;
            default -> APPLICATION_OCTET_STREAM;
        };
    }

    /** Guesses a media type from a path, consulting the platform as a fallback. */
    public static MimeType fromPath(Path path) {
        MimeType byName = fromFilename(path.getFileName().toString());
        if (byName != APPLICATION_OCTET_STREAM) {
            return byName;
        }
        try {
            String probed = Files.probeContentType(path);
            if (probed != null && !probed.isBlank()) {
                return fromWire(probed).orElse(APPLICATION_OCTET_STREAM);
            }
        } catch (IOException ignored) {
            // Fall through to the default.
        }
        return APPLICATION_OCTET_STREAM;
    }
}
