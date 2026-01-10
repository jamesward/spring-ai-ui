rootProject.name = "spring-ai-ui-demo"

// Use local library if running from within the spring-ai-ui project
val parentSettingsFile = file("../settings.gradle.kts")
if (parentSettingsFile.exists()) {
    includeBuild("..")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
