# mxRaven Mail SDK (Java)

The `mail` module is the Java client for the mxRaven email service. It sends mail
over SMTP, builds RFC 5322 / MIME messages (text, HTML, attachments), parses raw
messages, verifies and receives webhooks, and submits recipient feedback.

- Group / artifact: `com.mxraven` / `mail`
- Current version: `2.0.2`
- JavaDoc: https://java.mxraven.com/mail/
- Requirements: **Java 17+** (this `main` branch). For Java 8+, use the
  [`jdk8` branch](https://github.com/synqronlabs/mxraven-java/tree/jdk8) and the
  `com.mxraven:mail-jdk8` artifact.
- Runtime dependency: `com.fasterxml.jackson.core:jackson-databind` (webhook and
  feedback JSON only; SMTP and MIME are pure JDK)

What it covers:

- **SMTP submission** — STARTTLS / implicit TLS, AUTH PLAIN and LOGIN, the SMTP
  extension parameters (SIZE, 8BITMIME/BINARYMIME, SMTPUTF8, REQUIRETLS,
  DELIVERBY, DSN, AUTH), automatic CHUNKING/`BDAT` and PIPELINING, and a
  thread-safe connection pool (`SmtpPool`) for high-throughput sending.
- **Message building** — a fluent builder producing `multipart/alternative` and
  `multipart/mixed`, transfer encodings, and RFC 2047 header encoding.
- **MIME parsing** — a part tree plus a convenience `ParsedEmail` view.
- **Webhooks** — HMAC verification, decoding, typed callbacks, and an optional
  standalone server.
- **Feedback** — spam/ham learning and one-click unsubscribe.

---

## Installation

### From source

```sh
./gradlew :mail:build               # compile, test and package the module
./gradlew :mail:publishToMavenLocal
```

### Gradle (Maven Central)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mxraven:mail:2.0.2")
}
```

### Maven (Maven Central)

```xml
<dependency>
    <groupId>com.mxraven</groupId>
    <artifactId>mail</artifactId>
    <version>2.0.2</version>
</dependency>
```

### JitPack

Builds straight from GitHub, so you can target `main` or a commit.

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.synqronlabs.mxraven-java:mail:2.0.2")
    // Latest main build:  ...:mail:main-SNAPSHOT
    // Or pin a commit:    ...:mail:<commit-sha>
}
```

---

## Quick start

Send a message:

```java
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.SendResult;
import com.mxraven.mail.mime.MimeType;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

public class MailExample {
    public static void main(String[] args) throws Exception {
        SmtpConfig config = SmtpConfig.builder()
                .host("smtp.mxraven.email")
                .port(587)
                .startTls()
                .credentials("smtp-user", "smtp-secret")
                .build();

        Mail mail = MailBuilder.create()
                .from("Sender <sender@example.com>")
                .to("recipient@example.com")
                .subject("Hello from mxRaven")
                .textBody("Plain text body")
                .htmlBody("<p>HTML body</p>")
                .attachFile("report.pdf", reportBytes, MimeType.APPLICATION_PDF)
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.send(mail);
            System.out.println("accepted=" + result.success() + " " + result.message());
        }
    }
}
```

### High-throughput sending (pool + BDAT)

For bulk or multi-threaded submission, use `SmtpPool`. It keeps a bounded set of
authenticated connections, reuses them across sends, and is safe for concurrent
use (a bare `SmtpClient` is not).

```java
import com.mxraven.mail.SmtpPool;
import com.mxraven.mail.SendResult;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

SmtpPool pool = new SmtpPool(config, 8);          // at most 8 connections
ExecutorService workers = Executors.newFixedThreadPool(8);
try {
    for (Mail message : batch) {
        workers.submit(() -> {
            SendResult result = pool.send(message);   // borrows a connection
            System.out.println(result.messageRef() + " ok=" + result.success());
        });
    }
} finally {
    workers.shutdown();
    pool.close();
}
```

The high-throughput path is automatic when the server supports it:

- **`CHUNKING` / `BDAT`** — instead of `DATA`, the message is streamed with
  `BDAT … LAST`. There is no dot-stuffing and one fewer round trip per message,
  which is fastest for large bodies. You can also drive it directly with
  `client.bdat(bytes, true)`.
- **`PIPELINING`** — `MAIL FROM` and every `RCPT TO` are sent in one batch before
  the replies are read, hiding round-trip latency for multi-recipient sends.
- **`SIZE` / `BODY` / `SMTPUTF8`** — derived automatically by `MailBuilder`, so
  the server can accept or reject before the body is transferred.

No opt-in is required; each is used only when the server advertises the
extension. To abort an in-flight send, call `SmtpClient.cancel()` from another
thread (use a per-connection client for that, not a shared pool).

Receive and parse an inbound webhook:

```java
import com.mxraven.mail.mime.Attachment;
import com.mxraven.mail.mime.ParsedEmail;
import com.mxraven.mail.webhook.*;
import java.io.IOException;
import java.io.UncheckedIOException;

WebhookVerifier verifier = WebhookVerifier.builder().secret(signingSecret).build();

WebhookListener listener = new WebhookCallbacks() {
    @Override
    public void onInboundEmail(InboundEmail email) {
        try {
            ParsedEmail parsed = email.parse();          // download + parse the raw MIME
            System.out.println(parsed.subject());
            parsed.textBody().ifPresent(System.out::println);
            for (Attachment file : parsed.attachments()) {
                System.out.println(file.filename() + " (" + file.size() + " bytes)");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
};

WebhookHandler handler = WebhookHandler.builder()
        .verifier(verifier)
        .listener(listener)
        .build();

try (WebhookServer server = WebhookServer.builder()
        .port(8080)
        .path("/mxraven/webhook")
        .handler(handler)
        .build()) {
    server.start();       // prints a development-server banner to stderr
}
```

---

## SMTP client

### Configuration

`SmtpConfig` is built with a fluent builder.

| Builder | Default | Description |
| --- | --- | --- |
| `host(String)` | `smtp.mxraven.email` | Server hostname. |
| `port(int)` | `587` | Server port. |
| `startTls()` | default | STARTTLS (typically 587). |
| `implicitTls()` | | Implicit TLS (typically 465). |
| `noTls()` | | No TLS (testing only). |
| `security(SecurityMode)` | `STARTTLS` | Explicit security mode. |
| `credentials(username, password)` | | Enables SMTP AUTH. |
| `sslContext(SSLContext)` | JDK default | Custom TLS context. |
| `localName(String)` | `localhost` | EHLO name. |
| `connectTimeout(Duration)` | 30s | Socket connect timeout. |
| `readTimeout(Duration)` | 2m | Socket read timeout. |
| `writeTimeout(Duration)` | 2m | Socket write timeout. |

```java
SmtpConfig startTls = SmtpConfig.builder()
        .host("smtp.mxraven.email").port(587).startTls()
        .credentials("user", "pass").build();

SmtpConfig implicit = SmtpConfig.builder()
        .host("smtp.mxraven.email").port(465).implicitTls()
        .credentials("user", "pass").build();

SmtpConfig none = SmtpConfig.builder()
        .host("smtp.mxraven.email").port(25).noTls().build();
```

The host defaults to `SmtpConfig.DEFAULT_HOST` (`smtp.mxraven.email`); pass an explicit host to target
another environment.

### The client

`SmtpClient` is `AutoCloseable`. `connect` performs the greeting, EHLO, STARTTLS
when configured, and AUTH.

```java
try (SmtpClient client = SmtpClient.connect(config)) {
    System.out.println(client.greeting());
    System.out.println("tls=" + client.isTls() + " auth=" + client.isAuthenticated());
    System.out.println("extensions=" + client.extensions());

    SendResult result = client.send(mail);
}
```

Low-level commands are available for manual transactions: `mail(String)`,
`rcpt(String)`, `data(byte[])`, `bdat(byte[], boolean)`, `noop()`, `reset()`,
`quit()`, and `lastResponse()`. `cancel()` aborts a blocked exchange from another
thread.

> **Concurrency:** a single `SmtpClient` owns one socket and is **not** safe for
> concurrent use. For concurrent submissions use `SmtpPool` (see
> [High-throughput sending](#high-throughput-sending-pool--bdat)).

### Send result

`send` returns a `SendResult`:

| Member | Description |
| --- | --- |
| `success()` | Whether MAIL FROM was accepted, at least one recipient was accepted, and the body was accepted. |
| `message()` | The final server reply text. |
| `messageRef()` | The mxRaven `message_ref` id from the final reply, or `null` when absent. |
| `recipients()` | One `RecipientResult` per recipient. |

`RecipientResult` exposes `recipient()`, `accepted()`, and `status()`. A failed
`MAIL FROM` or a recipient-only failure short-circuits the transaction — the
client sends `RSET` instead of `DATA`, so a send is never accepted while every
recipient was rejected.

`SmtpResponse.isSuccess()` is true only for 2xx replies; `isPositive()` also
covers 3xx intermediates. When the server returns an RFC 3463 code,
`response.enhancedStatus()` yields an `EnhancedStatus` (`5.1.1`, class, subject,
detail).

### Raw messages

`sendRaw` sends prebuilt RFC 5322 bytes with an explicit envelope.

```java
Envelope envelope = Envelope.builder()
        .from(Path.of("sender@example.com"))
        .to(List.of(Recipient.of("to@example.com")))
        .build();

client.sendRaw(envelope, rawMessageBytes);
```

Unlike `send(Mail)`, `sendRaw` does **not** add `Date`, `Message-ID`, or `MIME-Version`; the bytes must
already be a valid RFC 5322 message (at minimum `From`, `To`, and `Date`).

### SMTP extension parameters

When the server advertises support, the envelope's parameters are sent with the
transaction:

- `MAIL FROM`: `SIZE`, `BODY=8BITMIME`/`BINARYMIME`, `SMTPUTF8`, `REQUIRETLS`,
  `BY=` (DELIVERBY), `AUTH`, `RET`, `ENVID`, and free-form `extensionParams`.
- `RCPT TO`: `NOTIFY`, `ORCPT` (per recipient).

`MailBuilder` derives `SIZE`, `BODY`, and `SMTPUTF8` automatically: the size is
the serialized message length, `BODY` follows the content transfer encoding, and
`SMTPUTF8` is requested when any envelope address is non-ASCII. Set them
explicitly only on an envelope you build by hand (as below) or to override.

```java
import com.mxraven.mail.model.*;

Envelope envelope = Envelope.builder()
        .from(Path.of("sender@example.com"))
        .to(List.of(new Recipient(Path.of("to@example.com"),
                new DSNRecipientParams(List.of("SUCCESS", "FAILURE"), "rfc822;orig@example.com"))))
        .size(raw.length)
        .bodyType(BodyType.EIGHT_BIT_MIME)
        .smtpUtf8(true)
        .dsnParams(new DSNEnvelopeParams("FULL"))
        .envId("abc123")
        .build();
```

`REQUIRETLS` requires an active TLS session and server support, otherwise the
send fails with an `SmtpException`. ENVID/ORCPT values are xtext-encoded
(RFC 3461); `DsnXText.encode` / `DsnXText.decode` are available directly. An
`ORCPT` without an address type is sent as `rfc822;<value>`.

Parameter values are validated: an unknown `NOTIFY` flag, `NEVER` combined with
another flag, a malformed `ORCPT` address type, or an ENVID over 100 encoded
characters fails the send with an `SmtpException` rather than being sent or
silently dropped.

---

## Building messages

`MailBuilder` composes a `Mail` (envelope + content).

### Addresses and headers

```java
Mail mail = MailBuilder.create()
        .from("Sender Name <sender@example.com>")   // or .from(MailboxAddress), or .nullSender()
        .sender("bounce@example.com")
        .to("a@example.com", "b@example.com")
        .cc("c@example.com")
        .bcc("hidden@example.com")
        .replyTo("support@example.com")
        .subject("Subject line")
        .messageId("<id@example.com>")
        .inReplyTo("<parent@example.com>")
        .references("<one@example.com>", "<two@example.com>")
        .date(Instant.now())
        .header("X-Custom-Header", "custom-value")
        .build();
```

`Date` and `Message-ID` are generated when omitted. Setting both `textBody` and
`htmlBody` produces a `multipart/alternative`; non-ASCII text is quoted-printable
encoded and non-ASCII header text is RFC 2047 encoded automatically.

`messageId`, `inReplyTo`, and `references` accept bare or angle-bracketed ids; the
builder adds the `<...>` brackets when they are missing.

Addresses are parsed and validated: `MailboxAddress.of(String)` requires a
`local@domain` form and rejects `"not an address"`, and `Header`/`MailboxAddress`
reject CR, LF, and NUL so a value cannot smuggle in extra header fields or SMTP
commands. Display names containing RFC 5322 specials are quoted
(`"Doe, Jane" <jane@example.com>`). Long header values are folded, and non-ASCII
header values are emitted as one or more RFC 2047 encoded-words.

### Attachments

Attachments wrap the message in a `multipart/mixed`. Content types use the
`MimeType` enum (or a raw string for a type not in the enum).

```java
import com.mxraven.mail.mime.MimeType;

Mail mail = MailBuilder.create()
        .from("sender@example.com")
        .to("a@example.com")
        .textBody("See attached.")
        .htmlBody("<p>See attached. <img src=\"cid:logo\"></p>")
        .attachFile("report.pdf", pdfBytes, MimeType.APPLICATION_PDF)
        .attachInline("logo.png", "logo", logoBytes, MimeType.IMAGE_PNG)   // cid:logo
        .build();
```

Read straight from disk with a `File` or `Path`; the type is guessed from the
extension (`MimeType.fromFilename` / `fromPath`) and can be overridden:

```java
.attachFile(new File("report.pdf"))
.attachFile(Path.of("chart.png"), MimeType.IMAGE_PNG)
.attachInline(Path.of("logo.png"), "logo")
```

### Raw body and serialization

Use `body(byte[], contentType, encoding)` for a pre-encoded body. `Content.toRaw()`
serializes headers and body to wire bytes.

```java
byte[] raw = mail.content().toRaw();
```

---

## Parsing messages

Parsing is pure MIME: multipart boundaries, nested parts, base64 and
quoted-printable transfer encodings, charsets, RFC 2047 encoded-words, and
RFC 2231 parameter values are all handled.

### `ParsedEmail`

A convenience view of a raw message.

```java
import com.mxraven.mail.mime.Attachment;
import com.mxraven.mail.mime.ParsedEmail;

ParsedEmail parsed = ParsedEmail.parse(rawBytes);
System.out.println(parsed.subject());
System.out.println(parsed.from() + " -> " + parsed.to());
parsed.textBody().ifPresent(System.out::println);
parsed.htmlBody().ifPresent(System.out::println);
parsed.date().ifPresent(System.out::println);
for (Attachment file : parsed.attachments()) {
    file.download(Path.of("/tmp/attachments"));   // writes into the directory under file.filename()
}
```

`Attachment` also exposes `data()`, `openStream()`, and `writeTo(OutputStream)`; `download(Path)` writes to
an explicit file, or into a directory using the attachment's filename.

| Member | Description |
| --- | --- |
| `subject()` | Decoded `Subject`. |
| `from()` / `to()` / `cc()` | `List<MailboxAddress>`. |
| `messageId()` / `date()` | `Optional`. |
| `textBody()` / `htmlBody()` | First non-attachment body of that type. |
| `attachments()` | `List<Attachment>` (includes inline parts). |
| `headers()` | Raw top-level `Headers`. |
| `mime()` | The root `MimePart`. |

`Attachment` exposes `filename()`, `contentType()`, `inline()`, `contentId()`,
`data()`, and `size()`.

### `MimePart` and `MimeParser`

For full control, walk the MIME tree.

```java
import com.mxraven.mail.mime.MimeParser;
import com.mxraven.mail.mime.MimePart;

MimePart root = MimeParser.parse(rawBytes);
for (MimePart part : root.parts()) {
    System.out.println(part.mediaType() + " " + part.filename().orElse(""));
}
```

`MimePart` exposes `headers()`, `mediaType()`, `charset()`, `encoding()`,
`filename()`, `contentId()`, `disposition()`, `isMultipart()`, `parts()`,
`rawBody()`, `decodedBody()`, `text()`, `isAttachment()`, `isTextBody()`.

From a webhook, `InboundEmail.parse()` downloads (verifying size and SHA-256) and
parses in one call; `RawEmail.fetch()` / `parse()` are available too.

---

## Webhooks

mxRaven signs every webhook delivery with HMAC-SHA256 over a canonical request
string and sends the signature in `X-MxRaven-*` headers. Two actions are
supported: `DELIVER_WEBHOOK` (a full inbound message) and `NOTIFY_WEBHOOK`
(best-effort SMTP / object-storage status).

### Verifying a delivery

```java
import com.mxraven.mail.webhook.WebhookEvent;
import com.mxraven.mail.webhook.WebhookVerifier;

WebhookVerifier verifier = WebhookVerifier.builder()
        .secret(signingSecret)     // shown once when the endpoint is created or rotated
        .build();

// Pass the exact raw body bytes and the request headers (case-insensitive Map):
WebhookEvent event = verifier.verifyAndDecode(method, requestUri, headers, rawBody);
```

`headers` is any `Map<String, String>`. `verifyAndDecode` throws
`InvalidSignatureException` when the HMAC does not match, and `WebhookException`
for missing headers, a stale timestamp, or an unrecognized payload. Use
`verify(...)` alone when you only need authentication.

| Builder | Description |
| --- | --- |
| `.secret(String)` | Signing secret, used as literal bytes. |
| `.key(kid, secret)` | Per-key secret; repeat to accept multiple keys during rotation. |
| `.tolerance(Duration)` | Maximum clock skew. Default 5 minutes; `Duration.ZERO` disables. |
| `.maxBodyBytes(long)` | Maximum body size accepted. Default 1 MiB. |
| `.clock(Clock)` | Clock used for timestamp checks. |

### Decoding

`WebhookEvent` is a sealed interface over `InboundEmail`, `DeliveryStatus`, and
`StorageStatus`, so dispatch is exhaustive.

```java
if (event instanceof InboundEmail email) {
    System.out.println(email.taskId() + " " + email.message().subject());
    byte[] raw = email.rawEmail().fetch();    // verified against declared size + SHA-256
} else if (event instanceof DeliveryStatus status) {
    System.out.println(status.status() + " smtp=" + status.smtpCode());
} else if (event instanceof StorageStatus storage) {
    System.out.println(storage.status() + " " + storage.bucketName() + "/" + storage.objectKey());
}
```

`WebhookEvent.decode(byte[])` decodes without verifying; only use it for
already-verified input.

### Receiving: handler, listener, callbacks

`WebhookHandler` is the framework-agnostic seam. It verifies, deduplicates, and
dispatches, and returns a `WebhookResult` to map into your response.

```java
import com.mxraven.mail.webhook.WebhookHandler;
import com.mxraven.mail.webhook.WebhookResult;

WebhookHandler handler = WebhookHandler.builder()
        .verifier(verifier)
        .listener(event -> app.handle(event))    // or a WebhookCallbacks
        .store(store)                            // optional idempotency
        .build();

WebhookResult result = handler.handle(method, requestUri, headers, body);
response.setStatus(result.status());             // 204 / 200 / 401 / 400
```

| Result | Status | Meaning |
| --- | --- | --- |
| `ACCEPTED` | `204` | Verified and dispatched. |
| `DUPLICATE` | `200` | Already processed; acknowledged without dispatching. |
| `INVALID_SIGNATURE` | `401` | Missing or mismatched signature. |
| `BAD_REQUEST` | `400` | Missing headers, stale, oversized, or not decodable. |

A listener is either a single lambda, or typed callbacks:

```java
import com.mxraven.mail.webhook.WebhookCallbacks;

WebhookListener byType = new WebhookCallbacks() {
    @Override
    public void onInboundEmail(InboundEmail email) { app.processInbound(email); }

    @Override
    public void onDeliveryStatus(DeliveryStatus status) { app.recordStatus(status); }
};
```

`WebhookCallbacks` extends `WebhookListener`, so it plugs in anywhere a listener
is accepted. A runtime exception from the listener propagates unchanged (mapped
to `500` by `WebhookServer`) so the sender retries.

Idempotency (at-least-once `DELIVER_WEBHOOK`) uses a `WebhookStore`; a task is
marked processed only after the listener returns normally.

### Standalone server

`WebhookServer` serves a handler over the JDK HTTP server (no extra dependency).

```java
WebhookServer server = WebhookServer.builder()
        .port(8080)
        .path("/mxraven/webhook")
        .handler(handler)
        .build();

server.start();
```

`start()` prints a development-server banner and warning to `System.err` (like
Flask/Werkzeug); disable it with `.banner(false)` for production. No threads
start until `start()` and they stop on `close()`. Behind a TLS-terminating proxy
set `X-Forwarded-Proto` so the signed request target is reconstructed correctly.

### In a framework

Skip `WebhookServer` and call the handler directly.

```java
@PostMapping("/mxraven/webhook")
ResponseEntity<Void> receive(@RequestBody byte[] body, HttpServletRequest req) throws IOException {
    Map<String, String> headers = Collections.list(req.getHeaderNames()).stream()
            .collect(Collectors.toMap(n -> n, req::getHeader));
    WebhookResult result = handler.handle(req.getMethod(),
            URI.create(req.getRequestURL().toString()), headers, body);
    return ResponseEntity.status(result.status()).build();
}
```

---

## Feedback

Teach the mxRaven spam filter by submitting misclassified messages. The service
matches a message by SHA-256, so submit the exact raw RFC 822 bytes mxRaven
processed.

```java
import com.mxraven.mail.feedback.FeedbackClient;
import com.mxraven.mail.feedback.LearningResult;

FeedbackClient feedback = FeedbackClient.builder()
        .baseUrl("https://feedback.mxraven.com")
        .credentials("mxr_tx_ab12cd34ef56", apiKeySecret)   // submission API key
        .build();

LearningResult result = feedback.learnSpam(rawMessage);
System.out.println(result.status() + " " + result.disposition()
        + " matched " + result.matchedHashKind());

feedback.learnHam(rawMessage);
feedback.unsubscribe(token);   // unauthenticated RFC 8058 one-click
```

| Member | Description |
| --- | --- |
| `.baseUrl(String)` | Required feedback service base URL. |
| `.credentials(username, secret)` | Submission API key used by the learning methods. |
| `.httpClient(HttpClient)` | Custom client. |
| `.requestTimeout(Duration)` | Per-request timeout. Default 30s. |
| `learnSpam(byte[])` / `learnSpam(InputStream)` | Teach that the message is spam. |
| `learnHam(byte[])` / `learnHam(InputStream)` | Teach that the message is not spam. |
| `learn(Disposition, byte[])` | Explicit disposition (`SPAM` / `HAM`). |
| `unsubscribe(String token)` | RFC 8058 one-click unsubscribe. |
| `learnSpamAsync(byte[])` / `learnHamAsync(byte[])` | Non-blocking variants returning `CompletableFuture<LearningResult>`. |
| `learnAsync(Disposition, byte[])` | Non-blocking explicit disposition. |
| `unsubscribeAsync(String)` | Non-blocking one-click unsubscribe. |

The `*Async` methods return a `CompletableFuture`; cancelling it aborts that one
request. Responses are read through a 64 KiB cap, so a misbehaving service cannot
exhaust memory. Failures are thrown as `FeedbackException`; branch on
`statusCode()` rather than `detail()`. `retryable()` is true for `429` and `5xx`.
A `404` means no stored evidence matched the submitted bytes.

---

## Exceptions

| Exception | When |
| --- | --- |
| `com.mxraven.mail.exception.MxRavenException` | Base exception for mail SDK errors (runtime). |
| `com.mxraven.mail.SmtpException` | SMTP failures: auth rejected, STARTTLS unavailable, etc. |
| `com.mxraven.mail.webhook.WebhookException` | Webhook verification or decoding failures. |
| `com.mxraven.mail.webhook.InvalidSignatureException` | Webhook HMAC did not match; respond `401`. |
| `com.mxraven.mail.feedback.FeedbackException` | Feedback service errors; `statusCode()`, `detail()`, `retryable()`. |

Transport failures (SMTP I/O, raw message download, feedback HTTP) surface as
`IOException`.

---

## Packages and models

- `com.mxraven.mail` — `SmtpClient`, `SmtpPool`, `SmtpConfig`, `SendResult`,
  `RecipientResult`, `SmtpResponse`, `EnhancedStatus`, `SmtpException`, `DsnXText`.
- `com.mxraven.mail.model` — `Mail`, `MailBuilder`, `Envelope`, `Path`,
  `Recipient`, `MailboxAddress`, `Headers`, `Header`, `Content`, `BodyType`,
  `DeliveryBy`, `DeliveryByMode`, `DSNEnvelopeParams`, `DSNRecipientParams`,
  `Extension`, `TraceField`.
- `com.mxraven.mail.mime` — `MimeParser`, `MimePart`, `ParsedEmail`, `Attachment`,
  `MimeWriter`, `MimeType`, `ContentTransferEncoding`.
- `com.mxraven.mail.webhook` — `WebhookVerifier`, `WebhookEvent`, `InboundEmail`,
  `DeliveryStatus`, `StorageStatus`, `RawEmail`, `WebhookListener`,
  `WebhookCallbacks`, `WebhookHandler`, `WebhookResult`, `WebhookStore`,
  `WebhookServer`, and the verification exceptions.
- `com.mxraven.mail.feedback` — `FeedbackClient`, `Disposition`, `LearningResult`,
  `FeedbackException`.

Contract enums expose `wire()` and `fromWire(String)` (`MimeType.fromWire` returns
`Optional<MimeType>`). E-mail addresses use `MailboxAddress` (`localPart`, `domain`, `displayName`).

---

## Building and testing

```sh
./gradlew :mail:test      # unit tests
./gradlew :mail:build     # compile, test and package
./gradlew build           # all modules
```

The `Driver` class under `mail/src/test/java/com/mxraven/mail` is a runnable
scratchpad with end-to-end examples (SMTP send, webhook server, MIME parse,
feedback).

## License

See the repository root for license information. Contributions are welcome: see
[`CONTRIBUTING.md`](../CONTRIBUTING.md).
