// The root build.gradle.kts applies the Java toolchain, JUnit 5, and test wiring
// to every subproject. The mail module needs Jackson to decode webhook and
// feedback payloads, OkHttp for the feedback and raw-message HTTP calls, and
// the official NATS client for the notification consumer; SMTP submission
// itself is pure JDK.
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.22.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.nats:jnats:2.26.4")
}
