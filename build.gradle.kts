import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.plugins.signing.SigningExtension

allprojects {
    group = "com.mxraven"
    version = providers.gradleProperty("releaseVersion").getOrElse("2.0.1")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    dependencies {
        add("testImplementation", platform("org.junit:junit-bom:5.11.4"))
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    tasks.withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.register<Jar>("sourcesJar") {
        group = "build"
        description = "Builds a jar containing the main source files."
        archiveClassifier.set("sources")
        from(project.extensions.getByType(JavaPluginExtension::class).sourceSets.getByName("main").allSource)
    }

    tasks.register<Jar>("javadocJar") {
        group = "build"
        description = "Builds a jar containing the generated javadoc."
        archiveClassifier.set("javadoc")
        from(tasks.named("javadoc"))
    }

    tasks.register<Jar>("fatJar") {
        group = "build"
        description = "Builds a jar bundling all runtime dependencies."
        archiveClassifier.set("all")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(project.extensions.getByType(JavaPluginExtension::class).sourceSets.getByName("main").output)
        val runtimeClasspath = project.configurations.getByName("runtimeClasspath")
        dependsOn(runtimeClasspath)
        from(runtimeClasspath.map { if (it.isDirectory) it else project.zipTree(it) }) {
            exclude("module-info.class")
            exclude("META-INF/versions/**")
        }
    }

    extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "mxraven-${project.name}"
                from(components["java"])
                artifact(project.tasks.named("sourcesJar"))
                artifact(project.tasks.named("javadocJar"))
                pom {
                    name.set("mxRaven ${project.name} SDK")
                    description.set("mxRaven ${project.name} Java SDK")
                    url.set("https://github.com/synqronlabs/mxraven-java")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("shivam1608")
                            name.set("Shivam")
                            email.set("notshivzee@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/synqronlabs/mxraven-java.git")
                        developerConnection.set("scm:git:ssh://git@github.com/synqronlabs/mxraven-java.git")
                        url.set("https://github.com/synqronlabs/mxraven-java")
                    }
                }
            }
        }
    }

    // Signing is required by Maven Central but should not break local builds or
    // JitPack, which run publishToMavenLocal without a key.
    val publications = extensions.getByType(PublishingExtension::class).publications
    val signingKey = providers.gradleProperty("signingKey").orNull
    val signingPassword = providers.gradleProperty("signingPassword").orNull
    extensions.configure<SigningExtension> {
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publications)
        }
    }
}
