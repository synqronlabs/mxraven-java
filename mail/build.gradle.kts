// The root build.gradle.kts applies the Java toolchain, JUnit 5, and test wiring
// to every subproject. The mail module needs Jackson to decode webhook and
// feedback payloads and the official NATS client for the notification consumer;
// SMTP submission itself is pure JDK.
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
    implementation("io.nats:jnats:2.26.4")
}
