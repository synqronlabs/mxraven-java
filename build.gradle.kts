import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

allprojects {
    group = "com.mxraven"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

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

    tasks.register<Jar>("sourcesJar") {
        group = "build"
        description = "Builds a jar containing the main source files."
        archiveClassifier.set("sources")
        from(project.extensions.getByType(JavaPluginExtension::class).sourceSets.getByName("main").allSource)
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
                artifact(project.tasks.named("sourcesJar"))
                pom {
                    name.set(project.name)
                    description.set("mxRaven ${project.name} SDK")
                }
            }
        }
    }
}
