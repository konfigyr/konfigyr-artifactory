plugins {
    id("idea")
    id("checkstyle")
    id("java-library")
    id("maven-publish")
    id("com.konfigyr.sonatype")
    id("com.konfigyr.deploy")
}

group = "com.konfigyr"
version = "1.0.0-RC4"
description = "Library that defines the main building blocks of the Konfigyr Artifactory."

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    api(libs.jspecify)
    compileOnly(libs.jackson.databind)

    testImplementation(libs.assert4j)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

checkstyle {
    toolVersion = "13.4.0"
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

tasks.test {
    useJUnitPlatform()
}
