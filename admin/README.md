# mxRaven Admin SDK (Java)

The `admin` module is the Java client for the mxRaven **control-plane REST API**. It
manages a workspace end to end: domains, listeners, routing rules, API keys,
suppressions, recipient sets, auto-reply templates, forwarding destinations,
inbound routes, relays, storage integrations, webhook endpoints, identity
providers, quotas, rate limits, governance, mail analytics, plus the public auth
login context.

It mirrors the same `/v2` surface the customer dashboard uses, with typed
request/response models and a small dependency footprint (Jackson only).

- Group / artifact: `com.mxraven` / `admin`
- Current version: `2.0.2`
- JavaDoc: https://java.mxraven.com/admin/
- Requirements: **Java 17+** (the build toolchain targets Java 17)
- Runtime dependency: `com.fasterxml.jackson.core:jackson-databind`

---

## Installation

### From source

```sh
./gradlew :admin:build              # compile, test and package the module
./gradlew :admin:publishToMavenLocal
```

### Gradle (Maven Central)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mxraven:admin:2.0.2")
}
```

### Maven (Maven Central)

```xml
<dependency>
    <groupId>com.mxraven</groupId>
    <artifactId>admin</artifactId>
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
    implementation("com.github.synqronlabs.mxraven-java:admin:2.0.2")
    // Latest main build:  ...:admin:main-SNAPSHOT
    // Or pin a commit:    ...:admin:<commit-sha>
}
```

---

## Quick start

```java
import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.Tenant;

public class AdminExample {
    public static void main(String[] args) throws Exception {
        AdminClient admin = new AdminClient("your-access-token");

        // Bind a tenant once. Every tenant-scoped family hangs off the Workspace.
        Workspace ws = admin.workspace("my-workspace");

        // Workspace identity and provisioning state.
        Tenant tenant = ws.tenant();
        System.out.println(tenant.slug() + " " + tenant.status() + " " + tenant.provisioningState());

        // List domains. Paged<T> is Iterable and follows cursor pages lazily.
        for (Domain domain : ws.domains().list()) {
            System.out.println(domain.domainName() + " status=" + domain.status()
                    + " dkimVerified=" + domain.dkimVerified());
        }

        // Onboard a sending domain and operate on the hydrated entity.
        Domain created = ws.domains().create("example.com");
        if (!created.dkimVerified()) {
            created.replace("dmarc@example.com");
        }

        admin.close();
    }
}
```

`AdminClient` is `AutoCloseable`. Closing it releases the internal HTTP client
when the client created it.

---

## Authentication and client configuration

The control plane authenticates every request with a bearer token (JWT or opaque
access token) issued by ZITADEL. The token is sent as
`Authorization: Bearer <token>` on every request.

```java
// Uses the default base URL (https://api.mxraven.email).
AdminClient admin = new AdminClient(accessToken);

// Or point at another environment:
AdminClient selfHosted = new AdminClient("https://api.example.com", accessToken);
```

Public operations work with a blank token: `admin.auth().loginContext(...)` and
`ws.smtpForwardDestinations().confirm(...)`.

Authorization is enforced by the control plane. A request the token is not
allowed to perform fails with `403` and a `PermissionDeniedException`.

### Constructors

| Constructor | Notes |
| --- | --- |
| `AdminClient(String token)` | Default base URL, API version, and rate limits |
| `AdminClient(String token, RateLimitConfig rateLimitConfig)` | Default base URL, custom retry behaviour |
| `AdminClient(String token, HttpClient http, ObjectMapper json)` | Default base URL, custom transport and codecs |
| `AdminClient(String baseUrl, String token)` | Explicit base URL |
| `AdminClient(String baseUrl, String token, RateLimitConfig rateLimitConfig)` | Custom retry behaviour |
| `AdminClient(String baseUrl, String token, String apiVersion)` | Custom API version prefix |
| `AdminClient(String baseUrl, String token, RateLimitConfig rateLimitConfig, String apiVersion)` | Both |
| `AdminClient(String baseUrl, String token, HttpClient http, ObjectMapper json)` | Custom transport and codecs |

`baseUrl` is normalised by stripping trailing slashes; when omitted it defaults
to `AdminClient.DEFAULT_BASE_URL` (`"https://api.mxraven.email"`). A blank or
missing `apiVersion` falls back to `AdminClient.DEFAULT_API_VERSION` (`"v2"`).

### API version

Every client-relative path is prefixed with the configured API version in one
place. Client code never hardcodes the version. `"/tenants/acme/domains"` becomes
`/v2/tenants/acme/domains` by default. Paths that already carry the prefix are
left untouched.

```java
AdminClient admin = new AdminClient("https://api.mxraven.com", token, "v3");

admin.baseUrl();        // https://api.mxraven.com
admin.apiVersion();     // v3
admin.json();           // the Jackson ObjectMapper in use
admin.rateLimitConfig();
```

### Rate limiting

When the control plane returns `429` (`rate_limited`) the client waits for the
`Retry-After` header (delta-seconds or HTTP date) and retries **GET** and
**DELETE** up to a bounded number of attempts. `POST`/`PUT` are never retried
automatically. If retries are exhausted a `RateLimitException` surfaces with
`retryAfter()` set.

```java
import com.mxraven.admin.RateLimitConfig;
import java.time.Duration;

RateLimitConfig rateLimits = RateLimitConfig.builder()
        .enabled(true)                            // default true
        .maxRetries(5)                            // default 3
        .defaultBackoff(Duration.ofSeconds(2))    // default 1s
        .maxBackoff(Duration.ofMinutes(1))        // default 60s
        .build();

AdminClient admin = new AdminClient("https://api.mxraven.com", token, rateLimits);

RateLimitConfig.defaults();   // retry up to 3 times, honour Retry-After
RateLimitConfig.disabled();   // surface 429 immediately
```

### Custom HTTP client and ObjectMapper

```java
import java.net.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
ObjectMapper mapper = new ObjectMapper();

AdminClient admin = new AdminClient("https://api.mxraven.com", token, http, mapper);
```

This constructor uses the default API version and rate-limit config.

---

## Client structure

`AdminClient` is the connection plus the public `auth()` family.
`admin.workspace(slug)` binds a tenant and exposes every tenant-scoped collection.

| Owner | Accessor | Client | Manages |
| --- | --- | --- | --- |
| `Workspace` | `tenant()` | `Tenant` | Workspace identity and provisioning state |
| `Workspace` | `domains()` | `DomainsClient` | Sending-domain onboarding and DNS verification |
| `Workspace` | `listeners()` | `ListenersClient` | Submission and MTA listeners |
| `Workspace` | `suppressions()` | `SuppressionsClient` | Tenant recipient suppressions |
| `Workspace` | `recipientSets()` | `RecipientSetsClient` | Reusable recipient collections and members |
| `Workspace` | `autoReplyTemplates()` | `AutoReplyTemplatesClient` | Auto-reply templates |
| `Workspace` | `smtpForwardDestinations()` | `SmtpForwardDestinationsClient` | Verified SMTP forwarding destinations |
| `Workspace` | `inboundRoutes()` | `InboundRoutesClient` | Recipient-domain routing to MTA listeners |
| `Workspace` | `smtpRelays()` | `SmtpRelaysClient` | Tenant SMTP relay integrations |
| `Workspace` | `storageIntegrations()` | `StorageIntegrationsClient` | Tenant object-storage integrations |
| `Workspace` | `webhookEndpoints()` | `WebhookEndpointsClient` | Webhook endpoints, secrets and deliveries |
| `Workspace` | `identityProviders()` | `IdentityProvidersClient` | Tenant external identity providers |
| `Workspace` | `quotas()` | `QuotasClient` | Effective tenant quotas and usage |
| `Workspace` | `mtaRateLimits()` | `MtaRateLimitsClient` | Tenant MTA rate-limit override |
| `Workspace` | `governance()` | `GovernanceClient` | Audit log, dedicated IP pools, resource search |
| `Workspace` | `mailAnalytics()` | `MailAnalyticsClient` | Mail activity and delivery aggregates |
| `AdminClient` | `auth()` | `AuthClient` | Public tenant login context |

`Workspace` is a lightweight binding. It holds the slug and a client reference,
so creating one per request is cheap. Use `admin.workspace("a")` and
`admin.workspace("b")` to work across tenants from a single `AdminClient`.

```java
Workspace ws = admin.workspace("my-workspace");
ws.slug();   // "my-workspace"
```

---

## Core concepts

### Hydrated entities

Reads and creations return an **entity**, not a bare record. An entity wraps the
wire record with the client and path needed to act on the resource, so you can
read it and operate on it directly.

```java
Domain d = ws.domains().create("example.com");

d.domainName();          // data accessor (delegated)
d.data();                // raw wire record (DomainData)
d.reload();              // refresh the snapshot
d.replace("dmarc@example.com");
d.listenerGrants();
d.delete();
d.isDeleted();           // local bookkeeping
```

Every entity exposes three things:

- **data** - every field of the underlying record, delegated (`domainName()`,
  `status()`, ...). The raw record is available through `data()`.
- **operations** - `reload()`, mutations (`replace`, `update`, `setActive`,
  `rotateSecret`, ...), and `delete()`.
- **children** - strictly-owned collections and singletons:
  `listener.routingRules()`, `listener.apiKeys()`,
  `listener.sendingDomainPolicy()`, `listener.mtaRateLimit()`,
  `set.members()`, `endpoint.deliveries()`, `domain.listenerGrants()`.

`Entity<D>` (base class) provides:

| Method | Description |
| --- | --- |
| `D data()` | The raw wire record |
| `boolean isDeleted()` | Local delete state for this instance |
| `boolean delete()` | Delete the resource; idempotent per instance |

Reads are snapshots. `isDeleted()` only reflects deletes made through that
instance, not deletes elsewhere.

### Pagination

List operations return `Paged<T>`, which implements `Iterable<T>` and fetches
pages lazily as you consume the iterator.

```java
for (Domain domain : ws.domains().list()) { /* ... */ }
List<Domain> all = ws.domains().list().toList();
ws.domains().list().stream().map(Domain::domainName).forEach(System.out::println);
```

`Paged<T>`:

| Method | Description |
| --- | --- |
| `Page<T> firstPage()` | The eagerly fetched first page |
| `boolean isEmpty()` | Cheap emptiness check |
| `Iterator<T> iterator()` | Lazy iterator across all pages |
| `Stream<T> stream()` | Lazy stream across all pages |
| `List<T> toList()` | Collect every page |
| `<R> Paged<R> map(Function<T,R> mapper)` | Map each element, keeping paging |

`Page<T>`:

| Method | Description |
| --- | --- |
| `List<T> items()` | Items on this page |
| `String nextPageToken()` / `previousPageToken()` / `lastPageToken()` | Opaque cursors |
| `boolean hasNext()` / `hasPrevious()` | Page availability |
| `Page<T> nextPage()` / `previousPage()` | Re-issue the same request with a cursor |
| `<R> Page<R> map(Function<T,R> mapper)` | Map the items |

Cursors are opaque. Never parse or persist them. Manual paging:

```java
Page<Domain> page = ws.domains().list().firstPage();
while (page != null) {
    for (Domain domain : page.items()) {
        System.out.println(domain.domainName());
    }
    page = page.hasNext() ? page.nextPage() : null;
}
```

`nextPage()` and `previousPage()` preserve the filters from the original call and
throw `IllegalStateException` when no such page exists. A transport failure while
fetching a later page surfaces as `UncheckedIOException` (the iterator cannot
declare checked exceptions).

`ListenersClient.listMtaListeners()` and `listSubmissionListeners()` return a
plain `List<Listener>` instead of a `Paged<T>`.

### Typed filters

Filterable list endpoints expose a fluent query builder with enums for every
closed value set, so you never need the wire parameter names.

```java
Paged<Domain> verified = ws.domains().query()
        .search("example.com")
        .status(DomainStatus.VERIFIED)
        .dkimVerified(true)
        .pageSize(50)
        .list();
```

| Query | Filters |
| --- | --- |
| `ws.domains().query()` | `search`, `status(DomainStatus)`, `spfVerified`, `dkimVerified`, `dmarcVerified`, `sendingEnabled` |
| `ws.listeners().query()` | `search`, `listenerType(ListenerType)`, `streamType(StreamType)`, `defaultTerminalActionType(TerminalActionType)`, `rspamdScanningEnabled` |
| `listener.routingRules().query()` | `search`, `actionType(RoutingRuleActionKind)`, `active` |
| `ws.suppressions().query()` | `search`, `reason(SuppressionReason)` |
| `ws.recipientSets().query()` | `search` |
| `set.members().query()` | `search`, `addedFrom`, `addedTo` |
| `ws.inboundRoutes().query()` | `search`, `mtaListenerId`, `domainName`, `verified` |
| `ws.governance().auditQuery()` | `search`, `actorKind(AuditActorKind)`, `actorId`, `action`, `resourceType`, `status(AuditStatus)`, `createdFrom`, `createdTo` |
| `ws.governance().searchQuery()` | `query` (required), `types(TenantSearchResourceType...)` |

Every builder provides `pageSize(int)` and `pageToken(String)` and terminates in
`list()` returning `Paged<T>`. Unset filters are omitted. The raw `QueryParams`
type remains available as an escape hatch.

`search(...)` requires at least 3 characters — the control plane rejects shorter
queries with an opaque `400`. The builder omits a blank value and rejects a
1–2 character value locally with a clear message. `QueryParams.q(...)` enforces
the same rule.

```java
QueryParams.create()
        .put("some_future_filter", "value")
        .pageSize(25)
        .pageToken(token)
        .q("search term");
```

### Errors

Non-success responses are decoded from the RFC 9457 `application/problem+json`
body and thrown as `ApiException`.

```java
import com.mxraven.admin.exception.ConflictException;
import com.mxraven.admin.exception.NotFoundException;
import com.mxraven.admin.exception.ValidationException;

try {
    ws.domains().create("example.com");
} catch (NotFoundException e) {          // 404
    // not found or not visible
} catch (ConflictException e) {          // 409
    // conflicts with current state
} catch (ValidationException e) {        // 422
    e.errors().forEach(err -> System.err.println(err.pointer() + " -> " + err.code()));
}
```

| Exception | Status | Notes |
| --- | --- | --- |
| `BadRequestException` | `400` | Malformed request |
| `AuthenticationException` | `401` | Missing, invalid or expired token |
| `PermissionDeniedException` | `403` | Missing scope or tenant access |
| `NotFoundException` | `404` | Not found or not visible |
| `ConflictException` | `409` | Conflicts with current state |
| `ValidationException` | `422` | Semantic rejection; see `errors()` |
| `RateLimitException` | `429` | Retried per `RateLimitConfig`, then thrown |
| `ServerException` | `5xx` | Control-plane failure |
| `ApiException` | any | Base class for all API errors |

Every exception carries `status()`, `code()` (stable, branch on this), `title()`,
`detail()`, `traceId()`, `errors()`, and `problem()`. The base class
`MxRavenException` (runtime) is extended by every admin exception.

### Serialization

- Wire fields are `snake_case`; model records are `lowerCamelCase` and map
  automatically.
- Contract enums (statuses, kinds, scopes, analytics metrics/dimensions, ...) are
  typed enums in `com.mxraven.admin.model`. Each exposes `wire()` and
  `fromWire(String)` for the raw value.
- Unset optional request fields are omitted from the JSON body. Do not send
  `null` for an optional non-nullable field.
- Some request fields are required but nullable (for example
  `ReplaceDomainRequest.dmarcReportAddress`, suppression reasons, and
  `UpdateAutoReplyTemplateRequest.textBody`/`htmlBody`). Passing `null` sends an
  explicit `null` to clear the stored value.
- Create-only secrets are returned once and never on reads:
  `IssuedAPIKey.secret()` and `IssuedWebhookEndpoint.signingSecret()`. Store them
  immediately.
- Every `*Request` record exposes a fluent builder, and request-taking operations
  also accept a `Consumer<Builder>` configurator. `build()` fails fast on missing
  or invalid required fields.

### Terminal action payloads

`defaultTerminalActionPayload` (create) and `actionPayload` (update) are typed as
`TerminalActionPayload`, and reads return the same type. Each factory matches the
control plane shape for the corresponding `TerminalActionType`.

| Action | Factory |
| --- | --- |
| `DELIVER` | `TerminalActionPayload.deliver()` |
| `DELIVER_DEDICATED` | `TerminalActionPayload.deliverDedicated(poolId)` |
| `SMARTHOST_RELAY` | `TerminalActionPayload.smartHostRelay(relayRef)` |
| `RELAY` | `TerminalActionPayload.relay(relayRef)` |
| `AUTO_REPLY` | `TerminalActionPayload.autoReply(templateRef)` |
| `DROP` | `TerminalActionPayload.drop()` / `drop(auditReason)` |
| `REJECT` | `TerminalActionPayload.reject(smtpStatusCode, enhancedStatusCode, message[, auditReason])` |

Typed getters: `isEmpty()`, `poolId()`, `relayRef()`, `templateRef()`,
`auditReason()`, `smtpStatusCode()`, `enhancedStatusCode()`, `message()`,
`values()`. `TerminalActionPayload.of(Map)` is the escape hatch.

```java
ws.listeners().create(listener -> listener
        .displayName("Inbound")
        .listenerType(ListenerType.MTA)
        .streamType(StreamType.TRANSACTIONAL)
        .defaultTerminalAction(TerminalActionType.RELAY, TerminalActionPayload.relay("primary")));
```

### Routing rule actions

`CreateRoutingRuleRequest` / `ReplaceRoutingRuleRequest` take a `RoutingRuleAction`
built from a factory or `builder()`. Payload shapes are validated locally.

| Action | Factory | Listener |
| --- | --- | --- |
| `DELIVER_DEDICATED` | `RoutingRuleAction.deliverDedicated(poolId)` | submission |
| `DELIVER` | `deliver()` | submission |
| `SMARTHOST_RELAY` | `smartHostRelay(relayRef)` | submission |
| `DROP` | `drop()` / `drop(auditReason)` | both |
| `REJECT` | `reject(smtpStatusCode, enhancedStatusCode, message[, auditReason])` | both |
| `MODIFY_HEADER` | `modifyHeader(List<ModifyHeaderOperation>)` | submission |
| `ADD_RECIPIENT` | `addRecipient(List<String>)` | submission |
| `NOTIFY_WEBHOOK` | `notifyWebhook(webhookRef)` | both |
| `RELAY` | `relay(relayRef)` | mta |
| `AUTO_REPLY` | `autoReply(templateRef)` | mta |
| `DELIVER_WEBHOOK` | `deliverWebhook(webhookRef)` | mta |
| `SMTP_FORWARD` | `smtpForward(destinationRef)` | mta |
| `S3_STORE` | `s3Store(storageRef, objectKeyPrefix, objectKeyTemplate)` | mta |

`ModifyHeaderOperation.append(header[, value])`, `set(header, value)` and
`remove(header)` build header operations. For generic construction use
`RoutingRuleAction.builder().payload(...)` or
`RoutingRuleAction.of(actionType, payload)`. `build()` fails fast on
`priority < 1`, a missing or oversized `expression_text`, or a missing action.

```java
listener.routingRules().create(rule -> rule
        .priority(1)
        .expressionText("from == 'billing@example.com'")
        .action(RoutingRuleAction.smtpForward("billing-dest")));
```

---

## API reference

Every method throws `java.io.IOException` for transport failures and
`ApiException` for API error responses. Collection methods live on
`ws.<family>()`; per-resource operations live on the **entity** returned by
`get(...)`, `list()` or `create(...)`.

### Workspace

| Method | Endpoint |
| --- | --- |
| `String slug()` | - |
| `Tenant tenant()` | `GET /v2/tenants/{slug}` |

### Domains - `ws.domains()`

| Method | Endpoint |
| --- | --- |
| `Paged<Domain> list()` | `GET /v2/tenants/{slug}/domains` |
| `DomainQuery query()` | builder for the above |
| `Domain get(String domainId)` | `GET /v2/tenants/{slug}/domains/{id}` |
| `Domain create(String domainName)` | `POST /v2/tenants/{slug}/domains` |
| `Domain create(String domainName, String dmarcReportAddress)` | `POST /v2/tenants/{slug}/domains` |

`DomainQuery`: `search(String)`, `status(DomainStatus)`, `spfVerified(boolean)`,
`dkimVerified(boolean)`, `dmarcVerified(boolean)`, `sendingEnabled(boolean)`,
`pageSize(int)`, `pageToken(String)`, `list()`.

`Domain` data: `id()`, `tenantId()`, `domainName()`, `verificationToken()`,
`sendingEnabled()`, `dkimActiveSelector()`, `spfVerified()`, `dkimVerified()`,
`dmarcVerified()`, `dmarcReportAddress()`, `dnsLastCheckedAt()`, `status()`,
`requiredCustomerRecords()`.
Operations: `reload()`, `replace(String dmarcReportAddress)`,
`listenerGrants()` (returns `Paged<DomainListenerGrant>`), `delete()`.

```java
Domain d = ws.domains().create("example.com", "dmarc@example.com");
for (DnsInstructionRecord rec : d.requiredCustomerRecords()) {
    System.out.println(rec);
}
for (DomainListenerGrant grant : d.listenerGrants()) {
    System.out.println(grant);
}
```

### Listeners - `ws.listeners()`

| Method | Endpoint |
| --- | --- |
| `Paged<Listener> list()` | `GET /v2/tenants/{slug}/listeners` |
| `ListenerQuery query()` | builder for the above |
| `Listener get(String listenerId)` | `GET /v2/tenants/{slug}/listeners/{id}` |
| `Listener create(Consumer<CreateListenerRequest.Builder>)` | `POST /v2/tenants/{slug}/listeners` |
| `Listener create(CreateListenerRequest)` | `POST /v2/tenants/{slug}/listeners` |
| `List<Listener> listMtaListeners()` | all pages, filtered to MTA |
| `List<Listener> listSubmissionListeners()` | all pages, filtered to submission |

`ListenerQuery`: `search(String)`, `listenerType(ListenerType)`,
`streamType(StreamType)`, `defaultTerminalActionType(TerminalActionType)`,
`rspamdScanningEnabled(boolean)`, `pageSize(int)`, `pageToken(String)`, `list()`.

`Listener` data: `id()`, `tenantId()`, `displayName()`, `listenerType()`,
`streamType()`, `defaultTerminalActionType()`, `defaultTerminalActionPayload()`,
`rspamdScanningEnabled()`.
Operations: `reload()`, `rename(String)`,
`update(Consumer<UpdateListenerRequest.Builder>)`,
`update(UpdateListenerRequest)`,
`updateDefaultTerminalAction(TerminalActionType)`,
`updateDefaultTerminalAction(TerminalActionType, TerminalActionPayload)`,
`updateDefaultTerminalAction(Consumer<UpdateListenerDefaultTerminalActionRequest.Builder>)`,
`updateDefaultTerminalAction(UpdateListenerDefaultTerminalActionRequest)`,
`updateRspamdScanning(boolean)`, `updateRspamdScanning(Consumer<...>)`,
`updateRspamdScanning(UpdateListenerRspamdScanningRequest)`, `delete()`.
Children: `routingRules()`, `apiKeys()`, `sendingDomainPolicy()`, `mtaRateLimit()`.

```java
Listener listener = ws.listeners().create(l -> l
        .displayName("Outbound transactional")
        .listenerType(ListenerType.SUBMISSION)
        .streamType(StreamType.TRANSACTIONAL)
        .defaultTerminalAction(TerminalActionType.DELIVER));

listener.rename("Outbound primary");
listener.updateRspamdScanning(true);
```

### Routing rules - `listener.routingRules()`

| Method | Endpoint |
| --- | --- |
| `Paged<RoutingRule> list()` | `GET .../{listener}/rules` |
| `RoutingRuleQuery query()` | builder for the above |
| `RoutingRule get(String ruleId)` | `GET .../{listener}/rules/{id}` |
| `RoutingRule create(Consumer<CreateRoutingRuleRequest.Builder>)` | `POST .../{listener}/rules` |
| `RoutingRule create(CreateRoutingRuleRequest)` | `POST .../{listener}/rules` |

`RoutingRuleQuery`: `search(String)`, `actionType(RoutingRuleActionKind)`,
`active(boolean)`, `pageSize(int)`, `pageToken(String)`, `list()`.

`RoutingRule` data: `id()`, `listenerId()`, `priority()`, `expressionText()`,
`action()`, `isActive()`.
Operations: `reload()`, `replace(String expressionText, RoutingRuleAction)`,
`replace(Consumer<ReplaceRoutingRuleRequest.Builder>)`,
`replace(ReplaceRoutingRuleRequest)`, `setActive(boolean)`,
`setActive(Consumer<SetRoutingRuleActiveRequest.Builder>)`,
`setActive(SetRoutingRuleActiveRequest)`, `reorder(int)`,
`reorder(Consumer<ReorderRoutingRuleRequest.Builder>)`,
`reorder(ReorderRoutingRuleRequest)`, `delete()`.

```java
listener.routingRules().create(rule -> rule
        .priority(10)
        .expressionText("to == 'support@example.com'")
        .action(RoutingRuleAction.notifyWebhook("support-hook")));

for (RoutingRule rule : listener.routingRules().list()) {
    System.out.println(rule.priority() + " " + rule.expressionText() + " " + rule.isActive());
}
```

### API keys - `listener.apiKeys()`

| Method | Endpoint |
| --- | --- |
| `Paged<APIKey> list()` | `GET .../{listener}/api-keys` |
| `IssuedAPIKey create()` | `POST .../{listener}/api-keys` |

`APIKey` data: `id()`, `tenantId()`, `listenerId()`, `username()`.
`IssuedAPIKey` additionally carries the one-time `secret()`.
`APIKey` operations: `delete()`. There is no single-key read.

```java
IssuedAPIKey key = listener.apiKeys().create();
System.out.println(key.username() + " " + key.secret());   // store the secret now

for (APIKey k : listener.apiKeys().list()) {
    k.delete();
}
```

### Sending-domain policy - `listener.sendingDomainPolicy()`

| Method | Endpoint |
| --- | --- |
| `SendingDomainPolicy get()` | `GET .../{listener}/sending-domain-policy` |
| `SendingDomainPolicy update(List<SendingDomainPolicyGrantRequest>)` | `PUT .../{listener}/sending-domain-policy` |
| `SendingDomainPolicy update(Consumer<ReplaceSendingDomainPolicyRequest.Builder>)` | `PUT .../{listener}/sending-domain-policy` |
| `SendingDomainPolicy update(ReplaceSendingDomainPolicyRequest)` | `PUT .../{listener}/sending-domain-policy` |

### Listener MTA rate limit - `listener.mtaRateLimit()`

| Method | Endpoint |
| --- | --- |
| `MTARateLimitOverride get()` | `GET .../{listener}/mta-rate-limit-override` |
| `MTARateLimitOverride put(MTARateLimitPolicy)` | `PUT .../{listener}/mta-rate-limit-override` |
| `void delete()` | `DELETE .../{listener}/mta-rate-limit-override` |

`listener.mtaRateLimit()` is only valid for MTA listeners; calling it on a submission
listener throws `IllegalStateException` locally instead of a `422`.

### Suppressions - `ws.suppressions()`

| Method | Endpoint |
| --- | --- |
| `Paged<TenantSuppression> list()` | `GET /v2/tenants/{slug}/suppressions` |
| `SuppressionQuery query()` | builder for the above |
| `TenantSuppression get(String emailAddress)` | `GET .../suppressions/{email}` |
| `TenantSuppression create(Consumer<CreateTenantSuppressionRequest.Builder>)` | `POST /v2/tenants/{slug}/suppressions` |
| `TenantSuppression create(CreateTenantSuppressionRequest)` | `POST /v2/tenants/{slug}/suppressions` |

`SuppressionQuery`: `search(String)`, `reason(SuppressionReason)`,
`pageSize(int)`, `pageToken(String)`, `list()`.

`TenantSuppression` data: `tenantId()`, `emailAddress()`, `reason()`.
Operations: `reload()`, `update(SuppressionReason)`,
`update(Consumer<UpdateTenantSuppressionRequest.Builder>)`,
`update(UpdateTenantSuppressionRequest)`, `delete()`.

```java
ws.suppressions().create(s -> s
        .emailAddress("bounce@example.com")
        .reason(SuppressionReason.BOUNCE));
```

### Recipient sets - `ws.recipientSets()`

| Method | Endpoint |
| --- | --- |
| `Paged<RecipientSet> list()` | `GET /v2/tenants/{slug}/recipient-sets` |
| `RecipientSetQuery query()` | builder for the above |
| `RecipientSet create(Consumer<CreateRecipientSetRequest.Builder>)` | `POST /v2/tenants/{slug}/recipient-sets` |
| `RecipientSet create(CreateRecipientSetRequest)` | `POST /v2/tenants/{slug}/recipient-sets` |
| `RecipientSet getByRef(String setRef)` | `GET .../recipient-sets/{set_ref}` |

`RecipientSetQuery`: `search(String)`, `pageSize(int)`, `pageToken(String)`,
`list()`.

`RecipientSet` data: `id()`, `tenantId()`, `setRef()`, `displayName()`,
`description()`, `createdAt()`, `updatedAt()`.
Operations: `reload()`, `update(Consumer<UpdateRecipientSetRequest.Builder>)`,
`update(UpdateRecipientSetRequest)`, `delete()`, `members()`.

#### Recipient set members - `set.members()`

| Method | Endpoint |
| --- | --- |
| `Paged<RecipientSetMember> list()` | `GET .../recipient-sets/{set_ref}/members` |
| `RecipientSetMemberQuery query()` | builder for the above |
| `RecipientSetMember add(String emailAddress)` | `POST .../members` |
| `RecipientSetMember add(Consumer<RecipientSetMemberRequest.Builder>)` | `POST .../members` |
| `RecipientSetMember add(RecipientSetMemberRequest)` | `POST .../members` |
| `void delete(String emailAddress)` | `DELETE .../members/{email}` |
| `RecipientSetBatchResult batchAdd(List<String>)` | `POST .../members/batch` |
| `RecipientSetBatchResult batchAdd(Consumer<RecipientSetMembersBatchRequest.Builder>)` | `POST .../members/batch` |
| `RecipientSetBatchResult batchAdd(RecipientSetMembersBatchRequest)` | `POST .../members/batch` |
| `RecipientSetBatchResult batchDelete(List<String>)` | `POST .../members/batch-delete` |
| `RecipientSetBatchResult batchDelete(Consumer<RecipientSetMembersBatchRequest.Builder>)` | `POST .../members/batch-delete` |
| `RecipientSetBatchResult batchDelete(RecipientSetMembersBatchRequest)` | `POST .../members/batch-delete` |

`RecipientSetMemberQuery`: `search(String)`, `addedFrom(String)`,
`addedTo(String)`, `pageSize(int)`, `pageToken(String)`, `list()`.
`RecipientSetMember` data: `emailAddress()`, `addedAt()`; operation `delete()`.

```java
RecipientSet set = ws.recipientSets().create(s -> s.displayName("VIP customers"));
set.members().add("vip@example.com");
set.members().batchAdd(List.of("a@example.com", "b@example.com"));

for (RecipientSetMember member : set.members().list()) {
    System.out.println(member.emailAddress() + " " + member.addedAt());
}
```

### Auto-reply templates - `ws.autoReplyTemplates()`

| Method | Endpoint |
| --- | --- |
| `Paged<AutoReplyTemplate> list()` | `GET /v2/tenants/{slug}/auto-reply-templates` |
| `AutoReplyTemplate create(Consumer<CreateAutoReplyTemplateRequest.Builder>)` | `POST .../auto-reply-templates` |
| `AutoReplyTemplate create(CreateAutoReplyTemplateRequest)` | `POST .../auto-reply-templates` |
| `AutoReplyTemplate get(String templateId)` | `GET .../auto-reply-templates/{id}` |
| `AutoReplyTemplate getByRef(String templateRef)` | `GET .../auto-reply-templates/ref/{template_ref}` |

`AutoReplyTemplate` data: `id()`, `tenantId()`, `templateRef()`, `displayName()`,
`fromAddress()`, `isActive()`, `content()`, `senderReadiness()`.
Operations: `reload()`, `update(Consumer<UpdateAutoReplyTemplateRequest.Builder>)`,
`update(UpdateAutoReplyTemplateRequest)`, `setActive(boolean)`,
`setActive(Consumer<SetAutoReplyTemplateActiveRequest.Builder>)`,
`setActive(SetAutoReplyTemplateActiveRequest)`, `delete()`.

```java
AutoReplyTemplate tpl = ws.autoReplyTemplates().create(t -> t
        .displayName("Out of office")
        .fromAddress("noreply@example.com")
        .textBody("We will get back to you shortly."));
tpl.setActive(true);
```

### SMTP forward destinations - `ws.smtpForwardDestinations()`

| Method | Endpoint |
| --- | --- |
| `Paged<SmtpForwardDestination> list()` | `GET /v2/tenants/{slug}/smtp-forward-destinations` |
| `SmtpForwardDestination create(Consumer<CreateSmtpForwardDestinationRequest.Builder>)` | `POST .../smtp-forward-destinations` |
| `SmtpForwardDestination create(CreateSmtpForwardDestinationRequest)` | `POST .../smtp-forward-destinations` |
| `SmtpForwardDestinationConfirmation confirm(String token)` | `POST /v2/smtp-forward-destination-verifications/confirm` (public) |
| `SmtpForwardDestinationConfirmation confirm(Consumer<ConfirmSmtpForwardDestinationRequest.Builder>)` | public confirm |
| `SmtpForwardDestinationConfirmation confirm(ConfirmSmtpForwardDestinationRequest)` | public confirm |
| `SmtpForwardDestination get(String destinationId)` | `GET .../{id}` |
| `SmtpForwardDestination getByRef(String destinationRef)` | `GET .../ref/{destination_ref}` |

`SmtpForwardDestination` data: `id()`, `tenantId()`, `destinationRef()`,
`displayName()`, `emailAddress()`, `verificationStatus()`, `verificationMethod()`,
`deliveryStatus()`, `verifiedAt()`, `deliveryRequestedAt()`,
`verificationEmailSentAt()`, `createdAt()`, `updatedAt()`.
Operations: `reload()`, `sendVerification()`, `delete()`.

### Inbound routes - `ws.inboundRoutes()`

| Method | Endpoint |
| --- | --- |
| `Paged<InboundRoute> list()` | `GET /v2/tenants/{slug}/inbound-routes` |
| `InboundRouteQuery query()` | builder for the above |
| `InboundRoute create(Consumer<CreateInboundRouteRequest.Builder>)` | `POST .../inbound-routes` |
| `InboundRoute create(CreateInboundRouteRequest)` | `POST .../inbound-routes` |
| `InboundRoute get(String routeId)` | `GET .../inbound-routes/{id}` |

`InboundRouteQuery`: `search(String)`, `mtaListenerId(String)`,
`domainName(String)`, `verified(boolean)`, `pageSize(int)`, `pageToken(String)`,
`list()`.

`InboundRoute` data: `id()`, `tenantId()`, `mtaListenerId()`, `domainName()`,
`isVerified()`, `verificationStatus()`, `txtVerified()`, `mxVerified()`,
`dnsLastCheckedAt()`, `verificationToken()`, `requiredCustomerRecords()`.
Operations: `reload()`, `update(String mtaListenerId, String domainName)`,
`update(Consumer<UpdateInboundRouteRequest.Builder>)`,
`update(UpdateInboundRouteRequest)`, `delete()`.

### SMTP relays - `ws.smtpRelays()`

| Method | Endpoint |
| --- | --- |
| `Paged<SmtpRelay> list()` | `GET /v2/tenants/{slug}/smtp-relays` |
| `SmtpRelay create(Consumer<CreateSmtpRelayRequest.Builder>)` | `POST .../smtp-relays` |
| `SmtpRelay create(CreateSmtpRelayRequest)` | `POST .../smtp-relays` |
| `SmtpRelay get(String relayId)` | `GET .../smtp-relays/{id}` |
| `SmtpRelay getByRef(String relayRef)` | `GET .../smtp-relays/ref/{relay_ref}` |

`SmtpRelay` data: `id()`, `tenantId()`, `relayRef()`, `displayName()`,
`isActive()`, `credentialsPresent()`.
Operations: `reload()`, `update(Consumer<UpdateSmtpRelayRequest.Builder>)`,
`update(UpdateSmtpRelayRequest)`, `setActive(boolean)`,
`setActive(Consumer<SetIntegrationActiveRequest.Builder>)`,
`setActive(SetIntegrationActiveRequest)`, `delete()`.

### Storage integrations - `ws.storageIntegrations()`

| Method | Endpoint |
| --- | --- |
| `Paged<StorageIntegration> list()` | `GET /v2/tenants/{slug}/storage-integrations` |
| `StorageIntegration create(Consumer<CreateStorageIntegrationRequest.Builder>)` | `POST .../storage-integrations` |
| `StorageIntegration create(CreateStorageIntegrationRequest)` | `POST .../storage-integrations` |
| `StorageIntegration get(String integrationId)` | `GET .../{id}` |
| `StorageIntegration getByRef(String storageRef)` | `GET .../ref/{storage_ref}` |

`StorageIntegration` data: `id()`, `tenantId()`, `storageRef()`, `displayName()`,
`isActive()`, `credentialsPresent()`.
Operations: `reload()`,
`update(Consumer<UpdateStorageIntegrationRequest.Builder>)`,
`update(UpdateStorageIntegrationRequest)`, `setActive(boolean)`,
`setActive(Consumer<SetIntegrationActiveRequest.Builder>)`,
`setActive(SetIntegrationActiveRequest)`, `delete()`.

### Webhook endpoints - `ws.webhookEndpoints()`

| Method | Endpoint |
| --- | --- |
| `Paged<WebhookEndpoint> list()` | `GET /v2/tenants/{slug}/webhook-endpoints` |
| `IssuedWebhookEndpoint create(Consumer<CreateWebhookEndpointRequest.Builder>)` | `POST .../webhook-endpoints` |
| `IssuedWebhookEndpoint create(CreateWebhookEndpointRequest)` | `POST .../webhook-endpoints` |
| `WebhookEndpoint get(String endpointId)` | `GET .../webhook-endpoints/{id}` |
| `WebhookEndpoint getByRef(String webhookRef)` | `GET .../webhook-endpoints/ref/{webhook_ref}` |

`WebhookEndpoint` data: `id()`, `tenantId()`, `webhookRef()`, `displayName()`,
`targetUrl()`, `signingKid()`, `hasSigningSecret()`, `isActive()`, `createdAt()`,
`updatedAt()`.
Operations: `reload()`, `update(Consumer<UpdateWebhookEndpointRequest.Builder>)`,
`update(UpdateWebhookEndpointRequest)`, `setActive(boolean)`,
`setActive(Consumer<SetIntegrationActiveRequest.Builder>)`,
`setActive(SetIntegrationActiveRequest)`, `rotateSecret()`, `delete()`,
`deliveries()`.
`IssuedWebhookEndpoint` carries the one-time `signingSecret()`.

#### Webhook deliveries - `endpoint.deliveries()`

| Method | Endpoint |
| --- | --- |
| `Paged<WebhookDelivery> list()` | `GET .../webhook-endpoints/{id}/deliveries` |

```java
IssuedWebhookEndpoint endpoint = ws.webhookEndpoints().create(w -> w
        .displayName("Billing events")
        .targetUrl("https://example.com/hooks"));
System.out.println(endpoint.signingSecret());   // store now

for (WebhookDelivery delivery : endpoint.deliveries().list()) {
    System.out.println(delivery);
}
```

### Identity providers - `ws.identityProviders()`

| Method | Endpoint |
| --- | --- |
| `Paged<TenantIdentityProvider> list()` | `GET /v2/tenants/{slug}/identity-providers` |
| `TenantIdentityProvider get(String idpId)` | `GET .../identity-providers/{idp_id}` |
| `TenantIdentityProvider create(Consumer<CreateTenantIdentityProviderRequest.Builder>)` | `POST .../identity-providers` (OIDC, `202`) |
| `TenantIdentityProvider create(CreateTenantIdentityProviderRequest)` | `POST .../identity-providers` |
| `TenantIdentityProvider createOAuth(CreateTenantOAuthIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/oauth` |
| `TenantIdentityProvider createJwt(CreateTenantJwtIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/jwt` |
| `TenantIdentityProvider createSaml(CreateTenantSamlIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/saml` |
| `TenantIdentityProvider createLdap(CreateTenantLdapIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/ldap` |
| `TenantIdentityProvider createGoogle(CreateTenantGoogleIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/google` |
| `TenantIdentityProvider createAzureAd(CreateTenantAzureAdIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/azure-ad` |
| `TenantIdentityProvider createGitHub(CreateTenantGitHubIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/github` |
| `TenantIdentityProvider createGitHubEnterpriseServer(CreateTenantGitHubEnterpriseServerIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/github-enterprise-server` |
| `TenantIdentityProvider createGitLab(CreateTenantGitLabIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/gitlab` |
| `TenantIdentityProvider createGitLabSelfHosted(CreateTenantGitLabSelfHostedIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/gitlab-self-hosted` |
| `TenantIdentityProvider createApple(CreateTenantAppleIdentityProviderRequest)` / `(Consumer<...>)` | `POST .../identity-providers/apple` |
| `IdentityProvisioning getProvisioning()` | `GET .../identity-provisioning` |
| `IdentityProvisioning updateProvisioning(List<String> roles)` | `PUT .../identity-provisioning` |
| `IdentityProvisioning updateProvisioning(UpdateIdentityProvisioningRequest)` | `PUT .../identity-provisioning` |
| `IdentityProvisioning updateProvisioning(Consumer<UpdateIdentityProvisioningRequest.Builder>)` | `PUT .../identity-provisioning` |
| `IdentityAccessClaim claimAccess()` | `POST .../identity/claim-access` |
| `TenantIdentityProvider setAutoGrant(String idpId, boolean enabled)` | `PUT .../identity-providers/{idp_id}/auto-grant` |

`TenantIdentityProvider` data: `id()`, `tenantId()`, `idpRef()`, `displayName()`,
`providerType()`, `zitadelProviderId()`, `issuer()`, `clientId()`, `scopes()`,
`providerConfig()`, `autoGrantRoles()`, `isActive()`, `lifecycleStatus()`.
Operations: `reload()`, `setAutoGrant(boolean)`, `delete()` (`202`).

```java
TenantIdentityProvider idp = ws.identityProviders().create(i -> i
        .displayName("Corporate SSO")
        .providerType(IdentityProviderType.OIDC)
        .issuer("https://idp.example.com")
        .clientId("client-id")
        .clientSecret("client-secret"));
idp.setAutoGrant(true);
```

### Quotas - `ws.quotas()`

| Method | Endpoint |
| --- | --- |
| `TenantQuota get()` | `GET /v2/tenants/{slug}/quotas` |

### MTA rate limits - `ws.mtaRateLimits()`

| Method | Endpoint |
| --- | --- |
| `MTARateLimitOverride get()` | `GET /v2/tenants/{slug}/mta-rate-limit-override` |
| `MTARateLimitOverride put(MTARateLimitPolicy)` | `PUT .../mta-rate-limit-override` |
| `void delete()` | `DELETE .../mta-rate-limit-override` |

Listener-level overrides live on `listener.mtaRateLimit()`.

### Governance - `ws.governance()`

| Method | Endpoint |
| --- | --- |
| `Paged<AuditLog> listAuditLog()` | `GET /v2/tenants/{slug}/audit-log` |
| `AuditLogQuery auditQuery()` | builder for the above |
| `Paged<TenantDedicatedIPPool> listDedicatedIPPools()` | `GET .../dedicated-ip-pools` |
| `Paged<TenantDedicatedIPPool> listDedicatedIPPools(QueryParams)` | `GET .../dedicated-ip-pools` |
| `Paged<TenantResourceSearchResult> search(String query)` | `GET .../search` |
| `ResourceSearchQuery searchQuery()` | builder for the above |

`AuditLogQuery`: `search(String)`, `actorKind(AuditActorKind)`, `actorId(String)`,
`action(String)`, `resourceType(String)`, `status(AuditStatus)`,
`createdFrom(String)`, `createdTo(String)`, `pageSize(int)`, `pageToken(String)`,
`list()`.

`ResourceSearchQuery`: `query(String)` (required by the control plane),
`types(TenantSearchResourceType...)`, `pageSize(int)`, `pageToken(String)`,
`list()`.

```java
for (AuditLog entry : ws.governance().auditQuery()
        .actorKind(AuditActorKind.HUMAN)
        .status(AuditStatus.SUCCESS)
        .pageSize(100)
        .list()) {
    System.out.println(entry);
}

for (TenantResourceSearchResult hit : ws.governance().searchQuery()
        .query("example.com")
        .types(TenantSearchResourceType.DOMAIN, TenantSearchResourceType.LISTENER)
        .list()) {
    System.out.println(hit);
}
```

### Mail analytics - `ws.mailAnalytics()`

All operations are `GET /v2/tenants/{slug}/mail-analytics/{operation}` with a
required `start_at`/`end_at` interval. Metric and dimension arguments are the
`MailAnalyticsMetric` / `MailAnalyticsDimension` enums.

Intervals are validated locally before the request: both timestamps must use UTC
(zero offset), `start_at < end_at`, span at most 31 days, and align to UTC hour
boundaries for intervals up to 72 hours or day boundaries for longer ones.
Lifecycle operations additionally require `observation_end_at` in UTC, not before
`end_at`, with a cohort of at most 7 days and an observation horizon of at most 8
days. `limit` must be 1-20. Violations throw `IllegalArgumentException`.

| Method | Operation |
| --- | --- |
| `MailAnalyticsOverview getMailAnalyticsOverview(String startAt, String endAt)` | `overview` |
| `MailAnalyticsOverview getMailAnalyticsOverview(QueryParams params)` | `overview` |
| `MailAnalyticsLifecycle getMailAnalyticsLifecycle(String startAt, String endAt, String observationEndAt)` | `lifecycle` |
| `MailAnalyticsLifecycle getMailAnalyticsLifecycle(QueryParams params)` | `lifecycle` |
| `MailAnalyticsBreakdown getMailAnalyticsBreakdown(String startAt, String endAt, MailAnalyticsMetric, MailAnalyticsDimension)` | `breakdown` |
| `MailAnalyticsBreakdown getMailAnalyticsBreakdown(String startAt, String endAt, MailAnalyticsMetric, MailAnalyticsDimension, Integer limit)` | `breakdown` |
| `MailAnalyticsComparison getMailAnalyticsComparison(String startAt, String endAt)` | `comparison` |
| `MailAnalyticsComparison getMailAnalyticsComparison(QueryParams params)` | `comparison` |
| `MailAnalyticsActivityHeatmap getMailAnalyticsActivityHeatmap(String startAt, String endAt)` | `activity-heatmap` |
| `MailAnalyticsActivityHeatmap getMailAnalyticsActivityHeatmap(QueryParams params)` | `activity-heatmap` |
| `MailAnalyticsSeries getMailAnalyticsSeries(String startAt, String endAt, MailAnalyticsMetric, MailAnalyticsDimension)` | `series` |
| `MailAnalyticsSeries getMailAnalyticsSeries(String startAt, String endAt, MailAnalyticsMetric, MailAnalyticsDimension, Integer limit)` | `series` |
| `MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(String startAt, String endAt, String observationEndAt)` | `domain-lifecycle` |
| `MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(String startAt, String endAt, String observationEndAt, String recipientDomain, Integer limit)` | `domain-lifecycle` |
| `MailAnalyticsDomainLifecycle getMailAnalyticsDomainLifecycle(QueryParams params)` | `domain-lifecycle` |
| `MailAnalyticsTaskSizeStatistics getMailAnalyticsTaskSizeStatistics(String startAt, String endAt)` | `task-size-statistics` |
| `MailAnalyticsTaskSizeStatistics getMailAnalyticsTaskSizeStatistics(QueryParams params)` | `task-size-statistics` |

```java
MailAnalyticsSeries series = ws.mailAnalytics().getMailAnalyticsSeries(
        "2026-09-01T00:00:00Z",
        "2026-09-02T00:00:00Z",
        MailAnalyticsMetric.DATA_BYTES,
        MailAnalyticsDimension.STREAM);
```

### Auth - `admin.auth()`

| Method | Endpoint |
| --- | --- |
| `TenantLoginContext loginContext(String tenantSlug)` | `GET /v2/auth/tenants/{slug}/login-context` (public) |

```java
TenantLoginContext context = admin.auth().loginContext("my-workspace");
System.out.println(context.workspaceRef() + " " + context.displayName());
```

---

## Low-level request API

For endpoints not yet wrapped by a typed client, `AdminClient` exposes the raw
request methods. Paths are client-relative and get the API version prefix
automatically.

| Method | Description |
| --- | --- |
| `Response get(String path)` | GET |
| `Response get(String path, Map<String,String> query)` | GET with query |
| `Response post(String path, Object body)` | POST |
| `Response post(String path, Map<String,String> query, Object body)` | POST with query |
| `Response put(String path, Object body)` | PUT |
| `Response delete(String path)` | DELETE |
| `<T> Paged<T> paged(String path, Map<String,String> query, Class<T> elementType)` | Lazily paginated GET |
| `Response request(String method, String path, Map<String,String> query, Object body)` | Full control |

`Response`:

| Method | Description |
| --- | --- |
| `int status()` | HTTP status |
| `HttpHeaders headers()` | All response headers |
| `Optional<String> header(String name)` | First header value |
| `JsonNode body()` | Decoded JSON body, or `null` |
| `<T> T as(Class<T> type)` | Decode body to a single object |
| `<T> List<T> listOf(Class<T> elementType)` | Decode body to a list |
| `<T> Page<T> pageOf(Class<T> elementType)` | Decode body to a page |

```java
Response response = admin.get("/tenants/my-workspace/domains");
JsonNode body = response.body();
```

---

## Models

Typed request and response models live in `com.mxraven.admin.model`. Notable
records include:

- Tenancy: `Tenant`, `TenantStatus`, `TenantProvisioningState`, `TenantQuota`,
  `TenantQuotaUsage`, `EffectiveTenantQuotas`, `TenantDedicatedIPPool`.
- Domains: `Domain`, `DomainData`, `DomainStatus`, `DomainListenerGrant`,
  `DnsInstructionRecord`, `DnsRecordType`, `CreateDomainRequest`,
  `ReplaceDomainRequest`.
- Listeners: `Listener`, `ListenerData`, `ListenerType`, `StreamType`,
  `TerminalActionType`, `TerminalActionPayload`, `CreateListenerRequest`,
  `UpdateListenerRequest`, `UpdateListenerDefaultTerminalActionRequest`,
  `UpdateListenerRspamdScanningRequest`.
- Routing: `RoutingRule`, `RoutingRuleData`, `RoutingRuleAction`,
  `RoutingRuleActionKind`, `RoutingRulePayload`, `ModifyHeaderOperation`,
  `ModifyHeaderOp`, `CreateRoutingRuleRequest`, `ReplaceRoutingRuleRequest`,
  `SetRoutingRuleActiveRequest`, `ReorderRoutingRuleRequest`.
- API keys: `APIKey`, `APIKeyData`, `IssuedAPIKey`.
- Sending-domain policy: `SendingDomainPolicy`, `SendingDomainPolicyGrant`,
  `SendingDomainPolicyGrantRequest`, `SendingDomainSubdomainScope`,
  `ReplaceSendingDomainPolicyRequest`.
- Suppressions: `TenantSuppression`, `TenantSuppressionData`,
  `SuppressionReason`, `CreateTenantSuppressionRequest`,
  `UpdateTenantSuppressionRequest`.
- Recipient sets: `RecipientSet`, `RecipientSetData`, `RecipientSetMember`,
  `RecipientSetMemberData`, `RecipientSetMemberRequest`,
  `RecipientSetMembersBatchRequest`, `RecipientSetBatchResult`,
  `CreateRecipientSetRequest`, `UpdateRecipientSetRequest`.
- Auto-reply: `AutoReplyTemplate`, `AutoReplyTemplateData`,
  `AutoReplyTemplateContentSummary`, `AutoReplyTemplateHeader`,
  `AutoReplySenderReadiness`, `CreateAutoReplyTemplateRequest`,
  `UpdateAutoReplyTemplateRequest`, `SetAutoReplyTemplateActiveRequest`,
  `MaskedTemplateField`.
- SMTP forwarding: `SmtpForwardDestination`, `SmtpForwardDestinationData`,
  `SmtpForwardDestinationConfirmation`,
  `SmtpForwardDestinationVerificationMethod`,
  `SmtpForwardDestinationVerificationStatus`,
  `SmtpForwardVerificationDeliveryStatus`,
  `CreateSmtpForwardDestinationRequest`,
  `ConfirmSmtpForwardDestinationRequest`.
- Inbound routes: `InboundRoute`, `InboundRouteData`,
  `InboundRouteVerificationStatus`, `CreateInboundRouteRequest`,
  `UpdateInboundRouteRequest`.
- Relays and storage: `SmtpRelay`, `SmtpRelayData`, `CreateSmtpRelayRequest`,
  `UpdateSmtpRelayRequest`; `StorageIntegration`, `StorageIntegrationData`,
  `CreateStorageIntegrationRequest`, `UpdateStorageIntegrationRequest`,
  `SetIntegrationActiveRequest`.
- Webhooks: `WebhookEndpoint`, `WebhookEndpointData`, `WebhookDelivery`,
  `WebhookDeliveryKind`, `IssuedWebhookEndpoint`, `CreateWebhookEndpointRequest`,
  `UpdateWebhookEndpointRequest`.
- Identity: `TenantIdentityProvider`, `TenantIdentityProviderData`,
  `IdentityProviderType`, `IdentityProviderLifecycleStatus`, `IdentityProvisioning`,
  `IdentityAccessClaim`, `IdpAutoLinking`, `LdapAttributes`, `ProviderOptions`,
  `CreateTenantIdentityProviderRequest`, `CreateTenantOAuthIdentityProviderRequest`,
  `CreateTenantJwtIdentityProviderRequest`, `CreateTenantSamlIdentityProviderRequest`,
  `CreateTenantLdapIdentityProviderRequest`, `CreateTenantGoogleIdentityProviderRequest`,
  `CreateTenantAzureAdIdentityProviderRequest`,
  `CreateTenantGitHubIdentityProviderRequest`,
  `CreateTenantGitHubEnterpriseServerIdentityProviderRequest`,
  `CreateTenantGitLabIdentityProviderRequest`,
  `CreateTenantGitLabSelfHostedIdentityProviderRequest`,
  `CreateTenantAppleIdentityProviderRequest`,
  `UpdateIdentityProvisioningRequest`, `UpdateIdentityProviderAutoGrantRequest`,
  `SamlBinding`, `SamlNameIdFormat`, `SamlSignatureAlgorithm`, `AzureAdTenantType`.
- MTA rate limits: `MTARateLimitPolicy`, `MTARateLimitOverride`,
  `MtaRateLimitScope`, `MtaRateLimitSource`.
- Governance: `AuditLog`, `AuditActorKind`, `AuditStatus`,
  `TenantResourceSearchResult`, `TenantSearchResourceType`.
- Mail analytics: the `MailAnalytics*` family, including metrics, dimensions,
  buckets, breakdowns, series, lifecycle, latency and task-size statistics.
- Auth: `TenantLoginContext`.
- Shared: `Problem`, `ProblemError`, `RequestSupport`.

Timestamps are ISO-8601 strings. Contract enums expose `wire()` and
`fromWire(String)`.

---

## Building and testing

```sh
./gradlew :admin:test      # unit tests
./gradlew :admin:build     # compile, test and package
./gradlew build            # all modules
```

## License

See the repository root for license information. Contributions are welcome: see
[`CONTRIBUTING.md`](../CONTRIBUTING.md).
