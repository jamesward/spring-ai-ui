plugins {
    kotlin("jvm") version "2.3.0"
    signing
    id("com.palantir.git-version") version "4.2.0"
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "com.jamesward"

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.springframework.ai:spring-ai-model:2.0.0-M1")
    implementation("io.github.wakingrufus:khtmx-spring:0.4.0")
    implementation("io.github.wakingrufus:khtmx-dsl:0.4.0")
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "Spring AI UI"
        description = "UI for Spring AI"
        inceptionYear = "2026"
        url = "https://github.com/jamesward/spring-ai-ui"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "jamesward"
                name = "James Ward"
                email = "james@jamesward.com"
                url = "https://github.com/jamesward"
            }
        }
        scm {
            url = "https://github.com/jamesward/spring-ai-ui.git"
            connection = "scm:git:git://github.com/jamesward/spring-ai-ui.git"
            developerConnection = "scm:git:ssh://git@github.com/jamesward/spring-ai-ui.git"
        }
    }
}
