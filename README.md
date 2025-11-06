# Konfigyr Artifactory SDK

> #### Configuration Done Right
> A lightweight Java library that models the Konfigyr Artifactory API,
providing consistent abstractions for backend services, Gradle or Maven plugins, and third-party integrations.

### Overview

The Konfigyr Artifactory SDK defines the core interfaces and types that describe the relationship between:

- **Artifacts**: Spring Boot or JVM components that define configuration metadata.
- **Property descriptors**: Configuration properties exposed by artifacts.
- **Artifact metadata**: Aggregates all configuration metadata for a specific artifact.
- **Releases**: Versioned upload states managed by the Artifactory backend.
- **Manifests**: Lists of artifacts currently used by a service within a namespace.

This SDK provides a shared, stable contract between:

- The Konfigyr Artifactory backend.
- The Gradle and Maven plugins that upload metadata.
- External systems integrating with Konfigyr’s configuration repository.

### Getting Started

```xml
<dependency>
  <groupId>com.konfigyr</groupId>
  <artifactId>konfigyr-artifactory</artifactId>
  <version>1.0.0</version>
</dependency>
```

Or with Gradle:

```kotlin
implementation("com.konfigyr:konfigyr-artifactory:1.0.0")
```

### Related projects

- **Konfigyr Maven Plugin**: Uploads artifact metadata during Maven builds.
- **Konfigyr Gradle Plugin**: Automates metadata extraction for Gradle projects.

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
    .schema("{\"type\": \"number\", \"minimum\": 0, \"maximum\": 65535}")
    .build();
```

##### ArtifactMetadata

Aggregates all property definitions for a single artifact version. This object is uploaded to the Artifactory
backend via REST API, where it creates a new `Release` and triggers batch ingestion.

This object is typically produced automatically by the Konfigyr Gradle or Maven plugin during the build process.

```java
ArtifactMetadata metadata = ArtifactMetadata.builder()
    .groupId("com.example")
    .artifactId("demo-app")
    .version("1.2.3")
    .properties(propertyDescriptors)
    .build();
```

##### Release

Represents a version change event for a specific Konfigyr Artifact. Releases are created when new artifact metadata
is uploaded to the Konfigyr Artifactory. Each release transitions through the following lifecycle states: `PENDING` → `RELEASED` → `FAILED`

##### Manifest

Represents the current state of a Konfigyr service. Manifests allow Konfigyr to detect differences between
environments and ensure configuration consistency across releases.

```java
Manifest manifest = Manifest.builder()
    .id("service-id")
    .name("payments-service")
    .artifact(Artifact.of("com.example", "auth-lib", "1.4.2"))
    .artifact(Artifact.of("com.example", "core-utils", "2.0.0"))
    .build();
```

### Licence

This library is available either under the terms of the [Apache License 2.0](./LICENSE).
