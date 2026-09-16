package com.mxraven.mail.model;

import com.mxraven.mail.mime.ContentTransferEncoding;
import com.mxraven.mail.mime.MimeType;
import com.mxraven.mail.mime.MimeWriter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fluent builder for an outbound message.
 *
 * <p>Setting both {@link #textBody(String)} and {@link #htmlBody(String)}
 * produces a {@code multipart/alternative}; adding attachments wraps the result
 * in a {@code multipart/mixed}. Transfer encodings are applied automatically
 * (quoted-printable for non-ASCII text, base64 for attachments) and non-ASCII
 * header text is encoded per RFC 2047.
 */
public final class MailBuilder {
    private final Headers headers = new Headers();
    private Path from;
    private String sender;
    private final List<Recipient> to = new ArrayList<>();
    private final List<Recipient> cc = new ArrayList<>();
    private final List<Recipient> bcc = new ArrayList<>();
    private byte[] rawBody;
    private String rawContentType;
    private ContentTransferEncoding rawEncoding;
    private String textBody;
    private String htmlBody;
    private final List<PendingAttachment> attachments = new ArrayList<>();

    private record PendingAttachment(String filename, String contentType, byte[] data,
                                     boolean inline, String contentId) {
    }

    private record Entity(String contentType, ContentTransferEncoding encoding, byte[] body) {
    }

    /**
     * Creates a new, empty builder.
     *
     * @return a new builder
     */
    public static MailBuilder create() {
        return new MailBuilder();
    }

    /**
     * Sets the reverse path from an address string.
     *
     * @param address the sender address
     * @return this builder
     */
    public MailBuilder from(String address) {
        this.from = Path.of(address);
        return this;
    }

    /**
     * Sets the reverse path from a mailbox.
     *
     * @param mailbox the sender mailbox
     * @return this builder
     */
    public MailBuilder from(MailboxAddress mailbox) {
        this.from = Path.of(mailbox);
        return this;
    }

    /**
     * Sets the reverse path to the null address, {@code <>}.
     *
     * @return this builder
     */
    public MailBuilder nullSender() {
        this.from = Path.nullPath();
        return this;
    }

    /**
     * Sets the {@code Sender} header value.
     *
     * @param address the sender address
     * @return this builder
     */
    public MailBuilder sender(String address) {
        this.sender = address;
        return this;
    }

    /**
     * Adds recipients to the {@code To} list.
     *
     * @param addresses the recipient addresses
     * @return this builder
     */
    public MailBuilder to(String... addresses) {
        for (String address : addresses) {
            to.add(Recipient.of(address));
        }
        return this;
    }

    /**
     * Adds recipients to the {@code Cc} list.
     *
     * @param addresses the recipient addresses
     * @return this builder
     */
    public MailBuilder cc(String... addresses) {
        for (String address : addresses) {
            cc.add(Recipient.of(address));
        }
        return this;
    }

    /**
     * Adds recipients to the {@code Bcc} list. Blind recipients are placed on the
     * envelope but no {@code Bcc} header is written.
     *
     * @param addresses the recipient addresses
     * @return this builder
     */
    public MailBuilder bcc(String... addresses) {
        for (String address : addresses) {
            bcc.add(Recipient.of(address));
        }
        return this;
    }

    /**
     * Sets the {@code Subject} header.
     *
     * @param subject the subject text
     * @return this builder
     */
    public MailBuilder subject(String subject) {
        headers.add("Subject", subject);
        return this;
    }

    /**
     * Adds an arbitrary header field.
     *
     * @param name  the field name
     * @param value the field value
     * @return this builder
     */
    public MailBuilder header(String name, String value) {
        headers.add(name, value);
        return this;
    }

    /**
     * Sets the {@code Message-ID} header, adding angle brackets when the identifier
     * does not already carry them.
     *
     * @param id the message identifier
     * @return this builder
     */
    public MailBuilder messageId(String id) {
        headers.add("Message-ID", bracket(id));
        return this;
    }

    /**
     * Sets the {@code In-Reply-To} header, adding angle brackets when the
     * identifier does not already carry them.
     *
     * @param messageId the identifier of the message being replied to
     * @return this builder
     */
    public MailBuilder inReplyTo(String messageId) {
        headers.add("In-Reply-To", bracket(messageId));
        return this;
    }

    /**
     * Sets the {@code References} header from the given message identifiers,
     * separated by spaces.
     *
     * @param messageIds the referenced message identifiers
     * @return this builder
     */
    public MailBuilder references(String... messageIds) {
        StringBuilder value = new StringBuilder();
        for (String messageId : messageIds) {
            if (value.length() > 0) {
                value.append(' ');
            }
            value.append(bracket(messageId));
        }
        headers.add("References", value.toString());
        return this;
    }

    /**
     * Sets the {@code Reply-To} header.
     *
     * @param address the reply address
     * @return this builder
     */
    public MailBuilder replyTo(String address) {
        headers.add("Reply-To", address);
        return this;
    }

    /**
     * Sets the {@code Date} header from an instant, rendered in the system default
     * time zone.
     *
     * @param date the message date
     * @return this builder
     */
    public MailBuilder date(Instant date) {
        headers.add("Date", DateTimeFormatter.RFC_1123_DATE_TIME
                .format(date.atZone(ZoneId.systemDefault())));
        return this;
    }

    /**
     * Sets a plain-text body. Combined with {@link #htmlBody} this becomes
     * {@code multipart/alternative}.
     *
     * @param text the plain-text body
     * @return this builder
     */
    public MailBuilder textBody(String text) {
        this.textBody = text;
        return this;
    }

    /**
     * Sets an HTML body. Combined with {@link #textBody} this becomes
     * {@code multipart/alternative}.
     *
     * @param html the HTML body
     * @return this builder
     */
    public MailBuilder htmlBody(String html) {
        this.htmlBody = html;
        return this;
    }

    /**
     * Sets a raw body whose bytes are already encoded and written as-is.
     *
     * @param data        the raw body bytes
     * @param contentType the body content type
     * @param encoding    the content transfer encoding
     * @return this builder
     */
    public MailBuilder body(byte[] data, String contentType, ContentTransferEncoding encoding) {
        this.rawBody = data;
        this.rawContentType = contentType;
        this.rawEncoding = encoding;
        return this;
    }

    /**
     * Adds a file attachment, base64-encoded.
     *
     * @param filename    the attachment filename
     * @param data        the attachment bytes
     * @param contentType the media type
     * @return this builder
     */
    public MailBuilder attachFile(String filename, byte[] data, MimeType contentType) {
        return attachFile(filename, data, wire(contentType));
    }

    /**
     * Adds a file attachment with a custom (non-enumerated) content type.
     *
     * @param filename    the attachment filename
     * @param data        the attachment bytes
     * @param contentType the content type string
     * @return this builder
     */
    public MailBuilder attachFile(String filename, byte[] data, String contentType) {
        attachments.add(new PendingAttachment(filename, orDefault(contentType), data, false, null));
        return this;
    }

    /**
     * Adds an inline attachment (for example an HTML-embedded image).
     *
     * @param filename    the attachment filename
     * @param contentId   the {@code Content-ID} to reference the part
     * @param data        the attachment bytes
     * @param contentType the media type
     * @return this builder
     */
    public MailBuilder attachInline(String filename, String contentId, byte[] data, MimeType contentType) {
        return attachInline(filename, contentId, data, wire(contentType));
    }

    /**
     * Adds an inline attachment with a custom (non-enumerated) content type.
     *
     * @param filename    the attachment filename
     * @param contentId   the {@code Content-ID} to reference the part
     * @param data        the attachment bytes
     * @param contentType the content type string
     * @return this builder
     */
    public MailBuilder attachInline(String filename, String contentId, byte[] data, String contentType) {
        attachments.add(new PendingAttachment(filename, orDefault(contentType), data, true, contentId));
        return this;
    }

    /**
     * Adds a file attachment, reading {@code file} and guessing its content type.
     *
     * @param file the file to attach
     * @return this builder
     * @throws IOException if {@code file} cannot be read
     */
    public MailBuilder attachFile(File file) throws IOException {
        return attachFile(file.toPath());
    }

    /**
     * Adds a file attachment, reading {@code file} with an explicit content type.
     *
     * @param file        the file to attach
     * @param contentType the media type
     * @return this builder
     * @throws IOException if {@code file} cannot be read
     */
    public MailBuilder attachFile(File file, MimeType contentType) throws IOException {
        return attachFile(file.toPath(), contentType);
    }

    /**
     * Adds a file attachment, reading {@code path} and guessing its content type.
     *
     * @param path the file to attach
     * @return this builder
     * @throws IOException if {@code path} cannot be read
     */
    public MailBuilder attachFile(java.nio.file.Path path) throws IOException {
        return attachFile(path, MimeType.fromPath(path));
    }

    /**
     * Adds a file attachment, reading {@code path} with an explicit content type.
     *
     * @param path        the file to attach
     * @param contentType the media type
     * @return this builder
     * @throws IOException if {@code path} cannot be read
     */
    public MailBuilder attachFile(java.nio.file.Path path, MimeType contentType) throws IOException {
        return attachFile(path.getFileName().toString(), Files.readAllBytes(path), contentType);
    }

    /**
     * Adds an inline attachment, reading {@code file} and guessing its content type.
     *
     * @param file      the file to attach
     * @param contentId the {@code Content-ID} to reference the part
     * @return this builder
     * @throws IOException if {@code file} cannot be read
     */
    public MailBuilder attachInline(File file, String contentId) throws IOException {
        return attachInline(file.toPath(), contentId);
    }

    /**
     * Adds an inline attachment, reading {@code path} and guessing its content type.
     *
     * @param path      the file to attach
     * @param contentId the {@code Content-ID} to reference the part
     * @return this builder
     * @throws IOException if {@code path} cannot be read
     */
    public MailBuilder attachInline(java.nio.file.Path path, String contentId) throws IOException {
        return attachInline(path, contentId, MimeType.fromPath(path));
    }

    /**
     * Adds an inline attachment, reading {@code path} with an explicit content type.
     *
     * @param path        the file to attach
     * @param contentId   the {@code Content-ID} to reference the part
     * @param contentType the media type
     * @return this builder
     * @throws IOException if {@code path} cannot be read
     */
    public MailBuilder attachInline(java.nio.file.Path path, String contentId, MimeType contentType)
            throws IOException {
        return attachInline(path.getFileName().toString(), contentId, Files.readAllBytes(path), contentType);
    }

    private static String wire(MimeType contentType) {
        return contentType == null ? MimeType.APPLICATION_OCTET_STREAM.wire() : contentType.wire();
    }

    private static String orDefault(String contentType) {
        return contentType == null || contentType.isBlank()
                ? MimeType.APPLICATION_OCTET_STREAM.wire()
                : contentType;
    }

    /**
     * Builds the message, encoding bodies, assembling multipart structures, and
     * generating default {@code Date} and {@code Message-ID} headers when absent.
     *
     * @return the built message
     * @throws IllegalStateException if no reverse path was set or no recipient was
     *                               added
     */
    public Mail build() {
        if (from == null) {
            throw new IllegalStateException("from address is required (or call nullSender())");
        }
        if (to.isEmpty() && cc.isEmpty() && bcc.isEmpty()) {
            throw new IllegalStateException("at least one recipient is required");
        }

        Entity entity = topEntity();
        Headers contentHeaders = contentHeaders(entity);

        Content content = new Content(contentHeaders, entity.body(), entity.encoding(), null);

        List<Recipient> all = new ArrayList<>();
        all.addAll(to);
        all.addAll(cc);
        all.addAll(bcc);

        Envelope envelope = Envelope.builder()
                .from(from)
                .to(all)
                .bodyType(bodyTypeFor(entity.encoding()))
                .size(content.toRaw().length)
                .smtpUtf8(requiresSmtpUtf8(all))
                .build();

        return new Mail(envelope, content, List.of(), Instant.now());
    }

    /**
     * Maps the transfer encoding of the outermost entity to the SMTP {@code BODY}
     * type. Seven-bit and encoded (quoted-printable, base64) bodies stay 7-bit.
     */
    private static BodyType bodyTypeFor(ContentTransferEncoding encoding) {
        return switch (encoding) {
            case EIGHT_BIT -> BodyType.EIGHT_BIT_MIME;
            case BINARY -> BodyType.BINARY_MIME;
            default -> BodyType.SEVEN_BIT;
        };
    }

    /**
     * Whether any envelope address contains a non-ASCII byte in its local part or
     * domain, which requires the {@code SMTPUTF8} extension (RFC 6531).
     */
    private boolean requiresSmtpUtf8(List<Recipient> recipients) {
        if (!from.isNull() && needsUtf8(from.mailbox())) {
            return true;
        }
        for (Recipient recipient : recipients) {
            if (needsUtf8(recipient.address().mailbox())) {
                return true;
            }
        }
        return false;
    }

    private static boolean needsUtf8(MailboxAddress mailbox) {
        return !MimeWriter.isAscii(mailbox.localPart().getBytes(StandardCharsets.UTF_8))
                || !MimeWriter.isAscii(mailbox.domain().getBytes(StandardCharsets.UTF_8));
    }

    private Entity topEntity() {
        Entity base = baseEntity();
        if (attachments.isEmpty()) {
            return base;
        }

        String boundary = MimeWriter.newBoundary();
        List<MimeWriter.Section> sections = new ArrayList<>();
        sections.add(sectionFor("multipart/", base));
        for (PendingAttachment attachment : attachments) {
            sections.add(attachmentSection(attachment));
        }
        return new Entity("multipart/mixed; boundary=\"" + boundary + "\"",
                ContentTransferEncoding.SEVEN_BIT, MimeWriter.multipart(boundary, sections));
    }

    private Entity baseEntity() {
        if (rawBody != null) {
            return new Entity(rawContentType == null ? "text/plain" : rawContentType,
                    rawEncoding == null ? ContentTransferEncoding.SEVEN_BIT : rawEncoding,
                    rawBody);
        }
        if (textBody != null && htmlBody != null) {
            String boundary = MimeWriter.newBoundary();
            List<MimeWriter.Section> sections = List.of(
                    textSection("plain", textBody),
                    textSection("html", htmlBody));
            return new Entity("multipart/alternative; boundary=\"" + boundary + "\"",
                    ContentTransferEncoding.SEVEN_BIT, MimeWriter.multipart(boundary, sections));
        }
        if (htmlBody != null) {
            return textEntity("html", htmlBody);
        }
        if (textBody != null) {
            return textEntity("plain", textBody);
        }
        return new Entity("text/plain; charset=utf-8", ContentTransferEncoding.SEVEN_BIT, new byte[0]);
    }

    private Headers contentHeaders(Entity entity) {
        Headers contentHeaders = new Headers();
        for (Header header : headers.fields()) {
            String name = header.name();
            if (name.equalsIgnoreCase("Content-Type")
                    || name.equalsIgnoreCase("Content-Transfer-Encoding")
                    || name.equalsIgnoreCase("MIME-Version")) {
                continue;
            }
            if (name.equalsIgnoreCase("Subject")) {
                contentHeaders.add("Subject", MimeWriter.encodeWord(header.value()));
            } else {
                contentHeaders.add(header);
            }
        }

        if (!from.isNull()) {
            contentHeaders.add("From", formatAddress(from.mailbox()));
        }
        if (sender != null) {
            contentHeaders.add("Sender", sender);
        }
        if (!to.isEmpty()) {
            contentHeaders.add("To", joinRecipients(to));
        }
        if (!cc.isEmpty()) {
            contentHeaders.add("Cc", joinRecipients(cc));
        }
        if (contentHeaders.first("Date").isEmpty()) {
            contentHeaders.add("Date", DateTimeFormatter.RFC_1123_DATE_TIME
                    .format(Instant.now().atZone(ZoneId.systemDefault())));
        }
        if (contentHeaders.first("Message-ID").isEmpty()) {
            contentHeaders.add("Message-ID", generateMessageId());
        }

        contentHeaders.add("MIME-Version", "1.0");
        contentHeaders.add("Content-Type", entity.contentType());
        if (!entity.contentType().startsWith("multipart/")) {
            contentHeaders.add("Content-Transfer-Encoding", entity.encoding().wire());
        }
        return contentHeaders;
    }

    private static MimeWriter.Section sectionFor(String multipartPrefix, Entity entity) {
        List<Header> partHeaders = new ArrayList<>();
        partHeaders.add(new Header("Content-Type", entity.contentType()));
        if (!entity.contentType().startsWith(multipartPrefix)) {
            partHeaders.add(new Header("Content-Transfer-Encoding", entity.encoding().wire()));
        }
        return new MimeWriter.Section(partHeaders, entity.body());
    }

    private static MimeWriter.Section attachmentSection(PendingAttachment attachment) {
        List<Header> partHeaders = new ArrayList<>();
        partHeaders.add(new Header("Content-Type", attachment.contentType()));
        partHeaders.add(new Header("Content-Transfer-Encoding", "base64"));
        partHeaders.add(new Header("Content-Disposition", MimeWriter.contentDisposition(
                attachment.inline() ? "inline" : "attachment", attachment.filename())));
        if (attachment.contentId() != null && !attachment.contentId().isBlank()) {
            partHeaders.add(new Header("Content-ID", "<" + attachment.contentId() + ">"));
        }
        return new MimeWriter.Section(partHeaders, MimeWriter.base64(attachment.data()));
    }

    private static MimeWriter.Section textSection(String subtype, String text) {
        Entity entity = textEntity(subtype, text);
        return new MimeWriter.Section(List.of(
                new Header("Content-Type", entity.contentType()),
                new Header("Content-Transfer-Encoding", entity.encoding().wire())), entity.body());
    }

    private static Entity textEntity(String subtype, String text) {
        byte[] raw = normalizeLineEndings(text).getBytes(StandardCharsets.UTF_8);
        String contentType = "text/" + subtype + "; charset=utf-8";
        if (MimeWriter.isAscii(raw)) {
            return new Entity(contentType, ContentTransferEncoding.SEVEN_BIT, raw);
        }
        return new Entity(contentType, ContentTransferEncoding.QUOTED_PRINTABLE,
                MimeWriter.quotedPrintable(raw));
    }

    private static String joinRecipients(List<Recipient> recipients) {
        return recipients.stream()
                .map(recipient -> formatAddress(recipient.address().mailbox()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private static String formatAddress(MailboxAddress mailbox) {
        String email = mailbox.toString();
        String displayName = mailbox.displayName();
        if (displayName == null || displayName.isBlank()) {
            return email;
        }
        return formatDisplayName(displayName) + " <" + email + ">";
    }

    /**
     * Renders a display name as an RFC 2047 encoded phrase when non-ASCII, as a
     * quoted-string when it contains RFC 5322 specials, or verbatim otherwise.
     */
    private static String formatDisplayName(String displayName) {
        if (!MimeWriter.isAscii(displayName.getBytes(StandardCharsets.UTF_8))) {
            return MimeWriter.encodeWord(displayName);
        }
        if (needsQuoting(displayName)) {
            return "\"" + displayName.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        return displayName;
    }

    private static boolean needsQuoting(String value) {
        if (!value.equals(value.trim())) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '(' || c == ')' || c == '<' || c == '>' || c == '[' || c == ']'
                    || c == ':' || c == ';' || c == '@' || c == '\\' || c == ',' || c == '"') {
                return true;
            }
        }
        return false;
    }

    private String generateMessageId() {
        String domain = from.isNull() || from.mailbox().domain().isEmpty()
                ? (to.isEmpty() ? "localhost" : to.get(0).address().mailbox().domain())
                : from.mailbox().domain();
        if (domain == null || domain.isEmpty()) {
            domain = "localhost";
        }
        return "<" + System.nanoTime() + "." + UUID.randomUUID() + "@" + domain + ">";
    }

    private static String bracket(String messageId) {
        String value = messageId == null ? "" : messageId.trim();
        if (value.startsWith("<") && value.endsWith(">")) {
            return value;
        }
        return "<" + value + ">";
    }

    private static String normalizeLineEndings(String value) {
        return value.replace("\r\n", "\n").replace('\r', '\n').replace("\n", "\r\n");
    }
}
