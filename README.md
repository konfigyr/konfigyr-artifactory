![CI Build](https://github.com/konfigyr/konfigyr-artifactory/actions/workflows/continuous-integration.yml/badge.svg)
[![codecov](https://codecov.io/gh/konfigyr/konfigyr-artifactory/graph/badge.svg?token=8F51325ZV5)](https://codecov.io/gh/konfigyr/konfigyr-artifactory)
[![Latest Release](https://img.shields.io/maven-central/v/com.konfigyr/konfigyr-artifactory?style=flat)](https://central.sonatype.com/search?q=g%3Acom.konfigyr)
![Java 21+](https://img.shields.io/badge/java-21+-lightgray.svg)
[![Javadoc](https://javadoc.io/badge2/com.konfigyr/konfigyr-artifactory/javadoc.svg)](https://javadoc.io/doc/com.konfigyr/konfigyr-artifactory)

# Konfigyr Artifactory SDK

> #### Configuration done right
> A lightweight Java library that models the Konfigyr Artifactory API,
providing consistent abstractions for backend services, Gradle or Maven plugins, and third-party integrations.

### Overview

The Konfigyr Artifactory SDK defines the core interfaces and types that describe the relationship between:

- **Artifacts**: Spring Boot or JVM components that define configuration metadata.
- **Property descriptors**: Configuration properties exposed by artifacts.
- **Artifact metadata**: Aggregates all configuration metadata for a specific artifact.
- **Publications**: Versioned upload states managed by the Artifactory backend.
- **Manifests**: Lists of manifest entries currently used by a service within a namespace, each
  carrying the checksum and provenance of its captured metadata.
- **Service releases**: Process reports of a service's publish build, tracking per-artifact upload
  status separately from the service's published manifest content.
- **JsonSchema**: JSON Schema used by the Konfigyr UI for type-safe validation.

This SDK provides a shared, stable contract between:

- The Konfigyr Artifactory backend.
- The Gradle and Maven plugins that upload metadata.
- External systems integrating with Konfigyr’s configuration repository.

### Requirements

- Java 21 or later.
- No required runtime dependency on Spring Boot. The `Artifact`, `PropertyDescriptor`, and related types are
  plain Java abstractions; Spring Boot's `spring-boot-configuration-processor` is only needed at build time
  if you want to auto-generate `ArtifactMetadata` from `@ConfigurationProperties`.

### Getting Started

```xml
<dependency>
  <groupId>com.konfigyr</groupId>
  <artifactId>konfigyr-artifactory</artifactId>
  <version>1.0.0-RC6</version>
</dependency>
```

Or with Gradle:

```kotlin
implementation("com.konfigyr:konfigyr-artifactory:1.0.0-RC6")
```

### Related projects

- **[Konfigyr Maven Plugin](https://github.com/konfigyr/konfigyr-plugin#maven-plugin)**: Uploads artifact metadata during Maven builds.
- **[Konfigyr Gradle Plugin](https://github.com/konfigyr/konfigyr-plugin#gradle-plugin)**: Automates metadata extraction for Gradle projects.

### Core Interfaces

##### Artifact

Represents a unique component identified by Maven coordinates: `groupId`, `artifactId` and `version`.

Artifacts can be dependencies of the Spring Boot application that have `META-INF/spring-configuration-metadata.json` in
their JARs or the Spring Boot application itself that is using `org.springframework.boot:spring-boot-configuration-processor`
annotation processor.

Each artifact may additionally have:
- A human-readable name and description
- References to website and repository
- Comparison semantics via `Comparable<Artifact>`

```java
Artifact artifact = Artifact.of("com.example", "demo-app", "1.2.3");
```

##### PropertyDescriptor

Describes a single configuration property exposed by an artifact that is derived from
`org.springframework.boot.configurationmetadata.ConfigurationMetadataProperty` defined in the
`org.springframework.boot:spring-boot-configuration-processor` annotation processor library.

Each descriptor includes the property name, type information, description, default value, and 
a JSON Schema used by the Konfigyr UI for type-safe validation.

```java
PropertyDescriptor descriptor = PropertyDescriptor.builder()
    .name("server.port")
    .typeName("java.lang.Integer")
    .description("Port on which the HTTP server listens.")
    .defaultValue("8080")
    .schema(NumberSchema.builder().minimum(0.0).maximum(65535.0).build())
    .build();
```

##### ArtifactMetadata

Aggregates all property definitions for a single artifact version. This object is uploaded to the Artifactory
backend via REST API, where it creates a new `Publication` and triggers batch ingestion.

This object is typically produced automatically by the Konfigyr Gradle or Maven plugin during the build process.

```java
ArtifactMetadata metadata = ArtifactMetadata.builder()
    .groupId("com.example")
    .artifactId("demo-app")
    .version("1.2.3")
    .properties(propertyDescriptors)
    .build();
```

##### Publication

Represents a version change event for a specific Konfigyr Artifact. Publications are created when new artifact
metadata is uploaded to the Konfigyr Artifactory. Each publication transitions through the following lifecycle
states: `PENDING` → `PUBLISHED` → `FAILED`

##### Manifest

Represents the current state of a Konfigyr service. Manifests allow Konfigyr to detect differences between
environments and ensure configuration consistency across releases. A `Manifest` is a content snapshot: it
holds the list of `ManifestEntry` instances currently in use by the service.

```java
Manifest manifest = Manifest.builder()
    .id("service-id")
    .name("payments-service")
    .artifact(ManifestEntry.builder()
        .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
        .checksum("checksum")
        .source(ArtifactSource.ARTIFACTORY)
        .resolvedAt(Instant.now())
        .build())
    .build();
```

##### ManifestEntry

Represents a single, settled entry within a `Manifest`: an `Artifact` together with the checksum and
`ArtifactSource` of the metadata that was captured for it. Unlike `Publication`, a `ManifestEntry` carries
no processing state — once a `Manifest` is published, every entry in it is equally present.

```java
ManifestEntry entry = ManifestEntry.builder()
    .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
    .checksum("checksum")
    .source(ArtifactSource.LOCAL)
    .resolvedAt(Instant.now())
    .build();
```

##### ServiceRelease

Reports what happened the last time a service attempted to publish its `Manifest`: a transient process
report, kept separate from `Manifest` itself so that content (what the service currently publishes) and
process (what happened the last time someone tried to publish it) never get mixed together. Each
`ServiceRelease` transitions through the following lifecycle states: `PENDING` → `RELEASED` → `FAILED`

```java
ServiceRelease release = ServiceRelease.builder()
    .id("release-id")
    .state(ReleaseState.PENDING)
    .artifact(ServiceReleaseEntry.builder()
        .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
        .status(ArtifactUploadStatus.UPLOAD_REQUIRED)
        .build())
    .build();
```

##### ServiceReleaseEntry

Reports whether the build plugin still needs to upload a single artifact's metadata as part of a
`ServiceRelease`. Like `ManifestEntry`, it is an `Artifact` plus one extra field — here, an
`ArtifactUploadStatus`.

```java
ServiceReleaseEntry entry = ServiceReleaseEntry.builder()
    .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
    .status(ArtifactUploadStatus.SKIP)
    .build();
```

##### ServiceReleaseCandidate

A single artifact coordinate paired with the checksum of its locally resolved metadata, submitted by
the build plugin as part of the request to create a new `ServiceRelease`. It is the request-side
counterpart of `ServiceReleaseEntry`: an `Artifact` plus one extra field — here, a `checksum` instead
of an `ArtifactUploadStatus`.

```java
ServiceReleaseCandidate candidate = ServiceReleaseCandidate.builder()
    .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
    .checksum("checksum")
    .build();
```

### Jackson support

This library provides Jackson support for serializing and deserializing objects to and from JSON. To use it,
you would need to add `tools.jackson.core:jackson-databind` dependency to your project and configure the
`JsonMapper` as follows:

```java
final JsonMapper mapper = JsonMapper.builder()
        .addModule(new ArtifactoryJacksonModule())
        .build();
```

This Jackson module will register all the necessary converters for all the types defined in this library.

### Contributing

Pull requests are more than welcome — see [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines on how to get started.
Found a bug or have a feature request? Please [open an issue](https://github.com/konfigyr/konfigyr-artifactory/issues).

### Licence

This library is available either under the terms of the [Apache License 2.0](./LICENSE).
