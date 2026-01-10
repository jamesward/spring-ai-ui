plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.spring") version "2.3.0"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.0-M1"))
    implementation("org.springframework.ai:spring-ai-starter-model-openai-sdk")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // for testing the spring-ai-ui library
    if (file("../settings.gradle.kts").exists()) {
        implementation("com.jamesward:spring-ai-ui")
    } else {
        implementation("com.jamesward:spring-ai-ui:0.0.1")
    }

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
