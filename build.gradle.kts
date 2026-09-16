import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
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
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    // Publish under <module>-jdk8 (for example com.mxraven:mail-jdk8) and name
    // the jars accordingly.
    extensions.configure<BasePluginExtension> {
        archivesName.set("${project.name}-jdk8")
    }

    dependencies {
        add("testImplementation", platform("org.junit:junit-bom:5.11.4"))
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<JavaCompile>().configureEach {
        // The build runs on a JDK 8 toolchain, so the source/target level is 8.
        // (`--release` is not supported by the JDK 8 compiler.)
        options.encoding = "UTF-8"
        // Keep constructor parameter names for Jackson's ParameterNamesModule.
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    tasks.withType<Javadoc>().configureEach {
        // Fail on broken references and malformed HTML. The `missing` group is
        // excluded so undocumented members are tolerated.
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:all,-missing", true)
    }

    // --- Examples -------------------------------------------------------------
    // A separate `examples` source set, kept out of `main`: never published,
    // never bundled in the jars or fatJar, and not compiled by `build`. Compile
    // with `./gradlew :<module>:compileExamplesJava` and run a single example
    // with `./gradlew :<module>:runExample -Pexample=<fully.qualified.Main>`.
    val sourceSets = extensions.getByType<SourceSetContainer>()
    val mainSourceSet = sourceSets.getByName("main")
    val examplesSourceSet = sourceSets.create("examples") {
        java.srcDir("src/examples/java")
        compileClasspath += mainSourceSet.output
        runtimeClasspath += mainSourceSet.output
    }
    configurations.named(examplesSourceSet.implementationConfigurationName) {
        extendsFrom(configurations.getByName("implementation"))
    }
    configurations.named(examplesSourceSet.runtimeOnlyConfigurationName) {
        extendsFrom(configurations.getByName("runtimeOnly"))
    }

    tasks.register<JavaExec>("runExample") {
        group = "application"
        description = "Runs one example. Usage: ./gradlew :<module>:runExample -Pexample=<fqcn>"
        dependsOn(examplesSourceSet.classesTaskName)
        classpath = examplesSourceSet.runtimeClasspath
        mainClass.set(providers.gradleProperty("example"))
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
                from(components["java"])
                artifactId = "${project.name}-jdk8"
                artifact(project.tasks.named("sourcesJar"))
                artifact(project.tasks.named("javadocJar"))
                pom {
                    name.set("mxRaven ${project.name} SDK (Java 8)")
                    description.set("mxRaven ${project.name} Java SDK for Java 8+")
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
