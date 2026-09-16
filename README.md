# mxRaven Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.mxraven/mail?label=Maven%20Central&style=for-the-badge)](https://central.sonatype.com/artifact/com.mxraven/mail)
[![JitPack](https://img.shields.io/jitpack/v/github/synqronlabs/mxraven-java?color=blue&logo=jitpack&style=for-the-badge)](https://jitpack.io/#synqronlabs/mxraven-java)
[![JavaDoc](https://img.shields.io/badge/docs-java.mxraven.com-blue?style=for-the-badge&logo=readthedocs)](https://java.mxraven.com/)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue?style=for-the-badge)](./LICENSE)
![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge)
![Maintenance](https://img.shields.io/maintenance/yes/2026?logo=github&style=for-the-badge)
[![Repo size](https://img.shields.io/github/repo-size/synqronlabs/mxraven-java?style=for-the-badge)](https://github.com/synqronlabs/mxraven-java)

The official Java client for the [mxRaven](https://mxraven.email) email platform.
It has two modules:

- **`mail`**: send mail over SMTP, build and parse MIME messages, receive signed
  inbound webhooks, and submit feedback.
- **`admin`**: drive the control-plane REST API: domains, listeners, routing
  rules, suppressions, recipient sets, analytics, and the rest of your workspace.

## Documentation

| | |
| --- | --- |
| **JavaDoc (all modules)** | https://java.mxraven.com/ |
| `mail` JavaDoc | https://java.mxraven.com/mail/ |
| `admin` JavaDoc | https://java.mxraven.com/admin/ |
| `mail` guide | [`mail/README.md`](mail/README.md) |
| `admin` guide | [`admin/README.md`](admin/README.md) |

Both modules target **Java 17+**. The only runtime dependency they bring in is
Jackson (webhook/feedback and REST JSON); SMTP and MIME use just the JDK.

## Modules

| Module | Artifact | Guide | Use it when |
| --- | --- | --- | --- |
| `mail` | `com.mxraven:mail` | [`mail/README.md`](mail/README.md) | You need to **send** mail, **build/parse** MIME, or **receive** inbound webhooks and feedback. |
| `admin` | `com.mxraven:admin` | [`admin/README.md`](admin/README.md) | You need to **manage** your mxRaven workspace through the control-plane API. |

## Installation

Install only the module(s) you need: `mail` to send/receive mail, `admin` to
manage your workspace. Both are published to Maven Central; snapshots and
unreleased builds come from JitPack.

### Maven Central (releases)

**Gradle**

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mxraven:mail:2.0.2")    // send & receive mail
    implementation("com.mxraven:admin:2.0.2")   // control-plane API
}
```

**Maven**

```xml
<dependencies>
    <dependency>
        <groupId>com.mxraven</groupId>
        <artifactId>mail</artifactId>
        <version>2.0.2</version>
    </dependency>
    <dependency>
        <groupId>com.mxraven</groupId>
        <artifactId>admin</artifactId>
        <version>2.0.2</version>
    </dependency>
</dependencies>
```

### JitPack (snapshots / unreleased commits)

Use **GitHub Releases + Maven Central** for stable versions. If you need an
unreleased build (for example the latest `main` or a specific commit), pull it
from [JitPack](https://jitpack.io/#synqronlabs/mxraven-java):

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Latest commit on main:
    implementation("com.github.synqronlabs.mxraven-java:mail:main-SNAPSHOT")
    implementation("com.github.synqronlabs.mxraven-java:admin:main-SNAPSHOT")
    // or pin an exact commit:
    // implementation("com.github.synqronlabs.mxraven-java:mail:<commit-sha>")
    // implementation("com.github.synqronlabs.mxraven-java:admin:<commit-sha>")
}
```

JitPack builds on demand, so the first request for a branch/commit may take a
minute while it compiles.

## Quick start: send an email

```java
import com.mxraven.mail.SendResult;
import com.mxraven.mail.SmtpClient;
import com.mxraven.mail.SmtpConfig;
import com.mxraven.mail.model.Mail;
import com.mxraven.mail.model.MailBuilder;

public class SendEmail {
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
                .build();

        try (SmtpClient client = SmtpClient.connect(config)) {
            SendResult result = client.send(mail);
            System.out.println("success=" + result.success() + " " + result.message());
        }
    }
}
```

### Commonly used operations

Multiple recipients, and copies:

```java
Mail mail = MailBuilder.create()
        .from("sender@example.com")
        .to("a@example.com", "b@example.com")
        .cc("copy@example.com")
        .bcc("hidden@example.com")
        .subject("Hello")
        .textBody("Plain text body")
        .build();
```

Attachments and inline images (`cid:` references). Read from memory or straight
from a `File`/`Path` (the type is inferred from the extension):

```java
import com.mxraven.mail.mime.MimeType;
import java.io.File;

Mail mail = MailBuilder.create()
        .from("sender@example.com")
        .to("recipient@example.com")
        .subject("Monthly report")
        .textBody("See attached.")
        .htmlBody("<p>See attached. <img src=\"cid:logo\"></p>")
        .attachFile("report.pdf", pdfBytes, MimeType.APPLICATION_PDF)
        .attachFile(new File("chart.png"))
        .attachInline("logo.png", "logo", logoBytes, MimeType.IMAGE_PNG)
        .build();
```

Receiving mail is webhook-driven: verify the signature, then decode and parse
the inbound MIME. See
[`mail/README.md` → Webhooks](mail/README.md#webhooks) for the full handler and
standalone-server setup.

The `mail` guide covers the rest: implicit TLS, raw RFC 5322 sends, DSN/extension
parameters, MIME parsing, and feedback. See
[`mail/README.md`](mail/README.md).

## Quick start: receive mail (webhooks)

Inbound mail is delivered to your webhook endpoint as a signed event. Verify the
signature, decode it, then download and parse the raw MIME:

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
            ParsedEmail parsed = email.parse();            // download + parse the raw MIME
            System.out.println("subject=" + parsed.subject());
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
    server.start();   // standalone receiver for local development
}
```

`WebhookServer` is a small standalone receiver for local development and simple
deployments. To run inside an existing service, wire the `WebhookHandler` into
your HTTP framework instead. See [`mail/README.md` → Webhooks](mail/README.md#webhooks)
covers the handler, typed callbacks (bounces, status changes, feedback), and
framework integration.

## Quick start: manage your workspace

```java
import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.Listener;
import com.mxraven.admin.model.ListenerType;
import com.mxraven.admin.model.StreamType;
import com.mxraven.admin.model.Tenant;
import com.mxraven.admin.model.TerminalActionType;

public class ManageWorkspace {
    public static void main(String[] args) throws Exception {
        AdminClient admin = new AdminClient("your-access-token");
        Workspace ws = admin.workspace("my-workspace");

        // Workspace identity and provisioning state.
        Tenant tenant = ws.tenant();
        System.out.println(tenant.slug() + " " + tenant.status());

        // List domains. Paged<T> is Iterable and follows cursor pages lazily.
        for (Domain domain : ws.domains().list()) {
            System.out.println(domain.domainName() + " status=" + domain.status());
        }

        // Onboard a sending domain and operate on the hydrated entity.
        Domain created = ws.domains().create("example.com");
        created.replace("dmarc@example.com");

        // Create a submission listener, then manage it through the same entity.
        Listener listener = ws.listeners().create(l -> l
                .displayName("Outbound transactional")
                .listenerType(ListenerType.SUBMISSION)
                .streamType(StreamType.TRANSACTIONAL)
                .defaultTerminalAction(TerminalActionType.DELIVER));

        for (Listener l : ws.listeners().list()) {
            System.out.println(l.id() + " " + l.displayName() + " " + l.listenerType());
        }

        listener.rename("Outbound primary");
        listener.updateRspamdScanning(true);

        admin.close();
    }
}
```

`AdminClient` is `AutoCloseable`. Listeners expose child clients for routing
rules, API keys, sending-domain policy, and MTA rate limits. For authentication,
pagination, typed filters, error handling, and every other resource family, see
[`admin/README.md`](admin/README.md). For sending and receiving mail, see
[`mail/README.md`](mail/README.md).

## Building from source

```bash
./gradlew build          # compile, test, and package every module
./gradlew assemble       # compile only
./gradlew test           # run the test suite
./gradlew :mail:test     # tests for one module
./gradlew javadoc        # generate the API docs into <module>/build/docs/javadoc
./gradlew clean          # remove build output
```

Build a fat jar (bundles runtime dependencies) or a sources jar for a module:

```bash
./gradlew :mail:fatJar
./gradlew :admin:fatJar
./gradlew :mail:sourcesJar
```

## Examples

Runnable examples live in a non-published `examples` source set in each module
(`mail/src/examples/java`, `admin/src/examples/java`). They are not part of the
jars or the normal build, so they never affect the published artifacts.

```bash
./gradlew :mail:compileExamplesJava      # compile the mail examples
./gradlew :admin:compileExamplesJava     # compile the admin examples

./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendEmailExample
./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.ManageListenersExample
```

The `mail` examples cover sending (text/HTML, attachments, raw messages),
receiving webhooks, MIME parsing, and feedback. The `admin` examples cover
domains, listeners, pagination and filters, error handling, and workspace
identity.

## Contributing

Contributions are welcome: bug reports, feature requests, and pull requests.
See [`CONTRIBUTING.md`](CONTRIBUTING.md) for development setup, build and test
commands, coding guidelines, commit conventions, and the pull request checklist.

By contributing, you agree that your contributions are licensed under the
[Apache License 2.0](./LICENSE).

## License

Apache License 2.0. See [`LICENSE`](./LICENSE) and [`NOTICE`](./NOTICE).
