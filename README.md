# mxraven-java

Java SDK for the mxRaven email service: `mxraven-mail` for SMTP submission,
MIME, webhooks, and feedback, and `mxraven-admin` for the control-plane REST API.

API documentation: https://synqronlabs.github.io/mxraven-java/

## Installation

Both modules are published to **Maven Central** and can also be consumed through
**JitPack** (useful for building against `main` or a specific commit).

### Maven Central

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mxraven:mxraven-mail:2.0.1")
    implementation("com.mxraven:mxraven-admin:2.0.1")
}
```

### JitPack

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.synqronlabs.mxraven-java:mxraven-mail:2.0.1")
    implementation("com.github.synqronlabs.mxraven-java:mxraven-admin:2.0.1")
    // Latest `main` build:
    // implementation("com.github.synqronlabs.mxraven-java:mxraven-mail:main-SNAPSHOT")
    // Or pin a commit:
    // implementation("com.github.synqronlabs.mxraven-java:mxraven-mail:<commit-sha>")
}
```

Every release is tagged (`2.0.1`) and published from CI; see
[Releasing](#releasing).

## Build

Compile all modules:

```bash
./gradlew build
```

Compile without running tests:

```bash
./gradlew assemble
```

Build a fat jar (bundles all runtime dependencies) for a module:

```bash
./gradlew :mail:fatJar
./gradlew :admin:fatJar
```

Build a sources jar for a module:

```bash
./gradlew :mail:sourcesJar
./gradlew :admin:sourcesJar
```

## Test

Run all tests:

```bash
./gradlew test
```

Run tests for a specific module:

```bash
./gradlew :mail:test
./gradlew :admin:test
```

## Clean

Remove build outputs:

```bash
./gradlew clean
```

## Publish

Publish artifacts to the local Maven repository:

```bash
./gradlew publishToMavenLocal
```

### Releasing

Releases are automated: publishing a GitHub Release triggers the
[`Release`](.github/workflows/release.yml) workflow, which tests the project and
uploads the signed artifacts to Maven Central through the Central Portal
(`./gradlew publishAggregationToCentralPortal`). The version is taken from the
release tag (a leading `v` is stripped), so tag `2.0.1` publishes version
`2.0.1`.

Configure these repository secrets before the first release:

| Secret | Description |
| --- | --- |
| `CENTRAL_PORTAL_USERNAME` | Central Portal user-token username |
| `CENTRAL_PORTAL_PASSWORD` | Central Portal user-token password |
| `SIGNING_KEY` | ASCII-armored PGP private key |
| `SIGNING_PASSWORD` | Passphrase for the PGP key |

To publish from a workstation instead, pass the same values as Gradle
properties (for example in `~/.gradle/gradle.properties`):

```properties
centralPortalUsername=...
centralPortalPassword=...
signingKey=...
signingPassword=...
```

Then run:

```bash
./gradlew publishAggregationToCentralPortal
```

`publishingType = "AUTOMATIC"` releases the deployment as soon as validation
passes. Switch it to `USER_MANAGED` in `settings.gradle.kts` if you want to
approve each deployment from the Central Portal UI first.


## Other

List all available tasks:

```bash
./gradlew tasks
```

## License

Apache License 2.0. See [`LICENSE`](./LICENSE) and [`NOTICE`](./NOTICE).
