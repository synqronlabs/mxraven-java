package com.mxraven.mail.mime;

import com.mxraven.mail.internal.Java8;

import com.mxraven.mail.model.Headers;
import com.mxraven.mail.model.MailboxAddress;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A convenience view of a parsed email: decoded common headers plus the text,
 * HTML, and attachment content found anywhere in the MIME tree.
 *
 * <pre>
 * ParsedEmail email = ParsedEmail.parse(rawBytes);
 * System.out.println(email.subject());
 * email.textBody().ifPresent(System.out::println);
 * for (Attachment file : email.attachments()) {
 *     Files.write(Paths.get(file.filename()), file.data());
 * }
 * </pre>
 *
 * <p>For full control over the MIME structure use {@link #mime()} or
 * {@link MimeParser} directly.
 */
public final class ParsedEmail {
    private final MimePart root;
    private final List<MimePart> leaves;

    private ParsedEmail(MimePart root) {
        this.root = root;
        List<MimePart> collected = new ArrayList<>();
        collectLeaves(root, collected);
        this.leaves = Java8.copyList(collected);
    }

    /**
     * Parses a complete raw message.
     *
     * @param raw the raw message bytes
     * @return the parsed email
     */
    public static ParsedEmail parse(byte[] raw) {
        return new ParsedEmail(MimeParser.parse(raw));
    }

    /**
     * Wraps an already-parsed MIME tree.
     *
     * @param root the root MIME entity
     * @return the parsed email view
     */
    public static ParsedEmail of(MimePart root) {
        return new ParsedEmail(root);
    }

    /**
     * The root MIME entity.
     *
     * @return the root part
     */
    public MimePart mime() {
        return root;
    }

    /**
     * The top-level message headers (raw, undecoded).
     *
     * @return the root headers
     */
    public Headers headers() {
        return root.headers();
    }

    /**
     * The decoded {@code Subject}, or {@code ""} when absent.
     *
     * @return the decoded subject
     */
    public String subject() {
        return EncodedWords.decode(root.headers().first("Subject").orElse(""));
    }

    /**
     * The decoded {@code From} mailboxes.
     *
     * @return the sender mailboxes, empty when the header is absent
     */
    public List<MailboxAddress> from() {
        return addresses(root.headers().first("From").orElse(null));
    }

    /**
     * The decoded {@code To} mailboxes.
     *
     * @return the recipient mailboxes, empty when the header is absent
     */
    public List<MailboxAddress> to() {
        return addresses(root.headers().first("To").orElse(null));
    }

    /**
     * The decoded {@code Cc} mailboxes.
     *
     * @return the carbon-copy mailboxes, empty when the header is absent
     */
    public List<MailboxAddress> cc() {
        return addresses(root.headers().first("Cc").orElse(null));
    }

    /**
     * The {@code Message-ID}, when present.
     *
     * @return the trimmed message identifier, or empty
     */
    public Optional<String> messageId() {
        return root.headers().first("Message-ID").map(String::trim).filter(value -> !value.isEmpty());
    }

    /**
     * The parsed {@code Date}, when present and recognizable.
     *
     * @return the parsed instant, or empty
     */
    public Optional<Instant> date() {
        return root.headers().first("Date").flatMap(ParsedEmail::parseDate);
    }

    /**
     * The first non-attachment {@code text/plain} body.
     *
     * @return the plain-text body, or empty when none is present
     */
    public Optional<String> textBody() {
        return firstBody("text/plain");
    }

    /**
     * The first non-attachment {@code text/html} body.
     *
     * @return the HTML body, or empty when none is present
     */
    public Optional<String> htmlBody() {
        return firstBody("text/html");
    }

    /**
     * Every attachment (or inline part) in the message.
     *
     * @return an immutable list of the decoded attachments
     */
    public List<Attachment> attachments() {
        List<Attachment> out = new ArrayList<>();
        for (MimePart part : leaves) {
            if (!isAttachment(part)) {
                continue;
            }
            boolean inline = part.disposition()
                    .map(value -> value.equalsIgnoreCase("inline"))
                    .orElse(false)
                    || (part.contentId().isPresent() && !part.isTextBody());
            out.add(new Attachment(
                    part.filename().orElse(""),
                    part.mediaType(),
                    inline,
                    part.contentId().orElse(null),
                    part.decodedBody()));
        }
        return Java8.copyList(out);
    }

    private Optional<String> firstBody(String mediaType) {
        for (MimePart part : leaves) {
            if (part.mediaType().equalsIgnoreCase(mediaType) && !isAttachment(part)) {
                String text = part.text();
                if (!text.isEmpty()) {
                    return Optional.of(text);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isAttachment(MimePart part) {
        boolean attachmentDisposition = part.disposition()
                .map(value -> value.equalsIgnoreCase("attachment"))
                .orElse(false);
        if (attachmentDisposition || part.filename().isPresent()) {
            return true;
        }
        return part.contentId().isPresent() && !part.isTextBody();
    }

    private static void collectLeaves(MimePart part, List<MimePart> out) {
        if (part.isMultipart()) {
            for (MimePart child : part.parts()) {
                collectLeaves(child, out);
            }
        } else {
            out.add(part);
        }
    }

    private static Optional<Instant> parseDate(String value) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(ZonedDateTime.parse(trimmed, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant());
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private static List<MailboxAddress> addresses(String headerValue) {
        if (headerValue == null || Java8.isBlank(headerValue)) {
            return Java8.list();
        }
        List<MailboxAddress> out = new ArrayList<>();
        for (String token : splitAddresses(headerValue)) {
            String value = token.trim();
            if (value.isEmpty()) {
                continue;
            }
            String display = null;
            String address = value;
            int lt = value.lastIndexOf('<');
            int gt = value.lastIndexOf('>');
            if (lt >= 0 && gt > lt) {
                address = value.substring(lt + 1, gt).trim();
                display = value.substring(0, lt).trim();
                if (display.length() >= 2 && display.startsWith("\"") && display.endsWith("\"")) {
                    display = display.substring(1, display.length() - 1).replace("\\\"", "\"");
                }
                display = EncodedWords.decode(display).trim();
            }
            MailboxAddress base = MailboxAddress.of(address);
            out.add(new MailboxAddress(base.localPart(), base.domain(),
                    display == null || display.isEmpty() ? null : display));
        }
        return Java8.copyList(out);
    }

    /** Splits an address list on commas outside quotes and angle brackets. */
    private static List<String> splitAddresses(String value) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean escaped = false;
        int angleDepth = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (escaped) {
                current.append(c);
                escaped = false;
            } else if (c == '\\' && quoted) {
                current.append(c);
                escaped = true;
            } else if (c == '"') {
                quoted = !quoted;
                current.append(c);
            } else if (!quoted && c == '<') {
                angleDepth++;
                current.append(c);
            } else if (!quoted && c == '>') {
                angleDepth = Math.max(0, angleDepth - 1);
                current.append(c);
            } else if (!quoted && angleDepth == 0 && c == ',') {
                out.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        out.add(current.toString());
        return out;
    }

    @Override
    public String toString() {
        return "ParsedEmail[subject=" + subject().toLowerCase(Locale.ROOT)
                + ", attachments=" + attachments().size() + "]";
    }
}
