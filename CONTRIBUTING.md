# Contributing to mxRaven Java SDK

Thanks for taking the time to contribute. This guide covers how to report
issues, propose changes, and get a pull request merged.

## Ways to contribute

- **Report a bug** by opening an issue.
- **Request a feature** or describe a use case that does not work well today.
- **Improve documentation** in the READMEs or the Javadoc.
- **Send a pull request** for a fix, feature, or test.

Please be respectful and constructive in issues, reviews, and discussions.

## Reporting bugs

Open an issue with enough detail to reproduce it:

- What you expected to happen and what happened instead.
- The exact version (`com.mxraven:mail-jdk8` / `com.mxraven:admin-jdk8`) and
  your Java version.
- A minimal code snippet or steps to reproduce.
- The full exception or response output, including the `traceId` from any
  `ApiException` if the control plane returned an error.

## Suggesting features

Open an issue describing the problem you are trying to solve, the API you would
like to use, and any alternatives you considered. Small, focused proposals are
easier to review than broad ones.

## Development setup

1. Install **JDK 17** or newer so Gradle can run. Compilation, tests, and
   Javadoc use a **JDK 8** Gradle toolchain, which Gradle provisions
   automatically if it is missing, so the published artifacts target Java 8.
2. Fork the repository and clone your fork.
3. Create a branch with a descriptive name, for example:
   `git checkout -b feat/attachments-streaming`.

### Repository layout

| Path | Contents |
| --- | --- |
| `mail/` | The `mail` module: SMTP, MIME, webhooks, feedback. |
| `admin/` | The `admin` module: control-plane REST client. |
| `*/src/main/java` | Main sources (what gets published). |
| `*/src/test/java` | Unit and integration tests. |
| `*/src/examples/java` | Runnable examples (not published). |

## Build and test

```bash
./gradlew build              # compile, test, and package every module
./gradlew test               # run the test suite
./gradlew :mail:test         # tests for one module
./gradlew :admin:test
./gradlew javadoc            # must build without warnings
```

To run an example:

```bash
./gradlew :mail:runExample -Pexample=com.mxraven.mail.examples.SendEmailExample
./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.ManageListenersExample
```

## Coding guidelines

- **Keep changes focused.** One concern per pull request, and avoid unrelated
  reformatting so the diff stays reviewable.
- **Add tests.** Every behavior change or bug fix should come with a test in the
  affected module's `src/test/java`.
- **Document public API.** Classes, methods, fields, and record components are
  expected to have Javadoc. Match the existing style; the build runs Javadoc
  with `-Xdoclint:all,-missing`.
- **Follow the existing code style.** 4-space indentation, no wildcard imports,
  and the conventions already used in the file you are editing.
- **Add an example** under the relevant `src/examples/java` when you introduce a
  new user-facing capability.
- **Never commit secrets.** No tokens, keys, credentials, or personal data.

## Commit messages

Use a type prefix so the history stays readable:

| Prefix | Use for |
| --- | --- |
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation only |
| `test` | Tests |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `build` | Build system or dependencies |
| `ci` | CI configuration |
| `chore` | Maintenance tasks |

Where it helps, add a scope, for example `feat(mail): ...` or
`fix(admin): ...`.

## Pull requests

Before opening a pull request, confirm:

- [ ] `./gradlew build` passes.
- [ ] `./gradlew javadoc` builds without warnings.
- [ ] New behavior is covered by tests.
- [ ] Public API changes are documented with Javadoc.
- [ ] The change is focused on a single concern.

In the description, explain what the change does and why, and link the related
issue. A maintainer will review and may request changes; please be responsive to
feedback.

## Releasing

Releases are published automatically by
[`.github/workflows/release.yml`](.github/workflows/release.yml).

1. Merge the change into the `jdk8` branch.
2. Create a GitHub tag and release from that branch (for example `v2.1.0`).
3. The **Release** workflow checks out the tag, sets the version from the tag,
   runs `./gradlew test`, and runs `./gradlew publishAggregationToCentralPortal`.

The workflow uses the Gradle toolchain, so the runner only needs a JDK 17 to run
Gradle; the JDK 8 compiler and Javadoc are provisioned automatically by the
foojay resolver. Required repository secrets: `CENTRAL_PORTAL_USERNAME`,
`CENTRAL_PORTAL_PASSWORD`, `SIGNING_KEY`, and `SIGNING_PASSWORD`.

On this branch the artifacts are published as `com.mxraven:mail-jdk8` and
`com.mxraven:admin-jdk8`. A release created from `main` continues to publish the
Java 17 artifacts (`com.mxraven:mail` / `com.mxraven:admin`), so both lines can
coexist.

## License

By contributing, you agree that your contributions are licensed under the
[Apache License 2.0](./LICENSE), and you confirm you have the right to submit
them.
