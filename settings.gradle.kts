plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradleup.nmcp.settings") version "1.6.2"
}

rootProject.name = "mxraven-java"

include("mail")
include("admin")

val centralPortalUsername = providers.gradleProperty("centralPortalUsername").orNull
val centralPortalPassword = providers.gradleProperty("centralPortalPassword").orNull

nmcpSettings {
    // Only configured when credentials are present so local builds and JitPack
    // (which run publishToMavenLocal) work without Central credentials.
    if (centralPortalUsername != null && centralPortalPassword != null) {
        centralPortal {
            username = centralPortalUsername
            password = centralPortalPassword
            publishingType = "AUTOMATIC"
            publicationName = "mxraven-java"
        }
    }
}
