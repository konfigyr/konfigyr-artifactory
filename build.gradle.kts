plugins {
    id("idea")
    id("checkstyle")
    id("java-library")
    id("maven-publish")
    id("com.konfigyr.sonatype")
    id("com.konfigyr.deploy")
}

group = "com.konfigyr"
version = "1.0.0-RC2"
description = "Library that defines the main building blocks of the Konfigyr Artifactory."

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")

    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
}

publishing {
    publications {
        create<MavenPublication>("artifactory") {
            groupId = "com.konfigyr"
            artifactId = "konfigyr-artifactory"
            version = "1.0.0"
            from(components.getByName("java"))
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
}

tasks.test {
    useJUnitPlatform()
}
