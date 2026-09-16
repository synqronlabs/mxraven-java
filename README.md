# mxraven-java

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

## Other

List all available tasks:

```bash
./gradlew tasks
```

## License

Apache License 2.0. See [`LICENSE`](./LICENSE) and [`NOTICE`](./NOTICE).
