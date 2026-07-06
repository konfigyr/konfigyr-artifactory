package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import tools.jackson.databind.exc.ValueInstantiationException;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.StringNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatObject;

class ArtifactoryJacksonModuleTest {

	final JsonMapper mapper = JsonMapper.builder()
			.addModule(new ArtifactoryJacksonModule())
			.build();

	@EnumSource(JsonSchemaType.class)
	@ParameterizedTest(name = "assert JSON schema type: {0}")
	@DisplayName("should serialize and deserialize JSON schema type")
	void serializeAndDeserializeJsonType(JsonSchemaType type) {
		final var serialized = mapper.writeValueAsString(type);

		assertThat(serialized)
				.as("Serialized JSON schema type should not be empty")
				.isEqualTo("\"%s\"", type.name().toLowerCase());

		assertThat(mapper.readValue(serialized, JsonSchemaType.class))
				.as("Deserialized JSON schema type should be equal to the original")
				.isEqualTo(type);
	}

	@Test
	@DisplayName("should serialize and deserialize JSON schema using defined Mixin annotations")
	void assertJsonSchemaMixins() {
		final var schema = ObjectSchema.builder()
				.property("status", StringSchema.builder()
						.enumeration("ACTIVE")
						.enumeration("DISABLED")
						.enumeration("INACTIVE")
						.build()
				)
				.required("status")
				.build();

		final var json = mapper.valueToTree(schema);

		assertThat(json)
				.as("Serialized JSON object schema should only contain 3 fields")
				.hasSize(3);

		assertThat(json.get("type"))
				.as("Serialized JSON schema type should be object")
				.isEqualTo(StringNode.valueOf("object"));

		assertThat(json.get("required"))
				.as("Serialized JSON schema required field should contain only 'status'")
				.containsExactlyInAnyOrder(StringNode.valueOf("status"));

		assertThatObject(json.get("properties"))
				.as("Serialized JSON schema properties field contain the status schema")
				.extracting(it -> it.get("status"))
				.isEqualTo(mapper.createObjectNode()
						.put("type", "string")
						.set("enum", mapper.createArrayNode()
								.add("ACTIVE")
								.add("DISABLED")
								.add("INACTIVE")
						)
				);

		assertThat(mapper.convertValue(json, JsonSchema.class))
				.as("Deserialized JSON schema should be equal to the original")
				.isEqualTo(schema);
	}

	@Test
	@DisplayName("should serialize and deserialize JSON schema objects")
	void serializeAndDeserializeJsonSchema() {
		final var schema = ObjectSchema.builder()
				.title("Test object")
				.description("Test object description")
				.required("username", "age", "active", "status")
				.additionalProperties(StringSchema.instance())
				.propertyNames(StringSchema.instance())
				.property("username", StringSchema.builder()
						.format("email")
						.minLength(12)
						.maxLength(255)
						.example("john.doe@konfigyr.com")
						.example("jane.doe@konfigyr.com")
						.build()
				)
				.property("active", BooleanSchema.builder()
						.description("Is the user active?")
						.defaultValue(true)
						.build()
				)
				.property("age", IntegerSchema.builder()
						.minimum(18L)
						.format("int32")
						.build()
				)
				.property("roles", ArraySchema.builder()
						.items(ObjectSchema.builder()
								.property("name", StringSchema.builder()
										.example("ADMIN")
										.example("USER")
										.example("VISITOR")
										.build()
								)
								.build()
						)
						.build()
				)
				.property("status", StringSchema.builder()
						.enumeration("ACTIVE")
						.enumeration("DISABLED")
						.enumeration("INACTIVE")
						.build()
				)
				.property("height", NumberSchema.builder()
						.minimum(160.00)
						.multipleOf(10.0)
						.deprecated(true)
						.build()
				)
				.property("createdAt", StringSchema.builder()
						.format("date-time")
						.build()
				)
				.property("expiresIn", StringSchema.builder()
						.format("duration")
						.build()
				)
				.build();

		final var json = mapper.writeValueAsString(schema);

		assertThat(json)
				.as("Serialized JSON schema should not be empty")
				.isNotBlank();

		assertThat(mapper.readValue(json, JsonSchema.class))
				.as("Deserialized JSON schema should be equal to the original")
				.isEqualTo(schema);
	}

	@Test
	@DisplayName("should serialize and deserialize default artifact implementation")
	void serializeAndDeserializeArtifact() {
		final var artifact = Artifact.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.build();

		final var json = mapper.writeValueAsString(artifact);

		assertThat(json)
				.as("Serialized Artifact should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, Artifact.class))
				.as("Deserialized Artifact should be equal to the original")
				.isInstanceOf(DefaultArtifact.class)
				.isEqualTo(artifact);
	}

	@Test
	@DisplayName("should serialize and deserialize default artifact metadata implementation")
	void serializeAndDeserializeMetadata() {
		final var metadata = ArtifactMetadata.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.property(PropertyDescriptor.builder()
						.name("konfigyr.artifactory.enabled")
						.typeName("java.lang.Boolean")
						.schema(BooleanSchema.instance())
						.defaultValue("true")
						.build()
				).build();

		final var json = mapper.writeValueAsString(metadata);

		assertThat(json)
				.as("Serialized Artifact metadata should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, ArtifactMetadata.class))
				.as("Deserialized Artifact metadata should be equal to the original")
				.isInstanceOf(DefaultArtifactMetadata.class)
				.isEqualTo(metadata);
	}

	@Test
	@DisplayName("should serialize and deserialize default publication implementation")
	void serializeAndDeserializePublication() {
		final var publication = Publication.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.state(PublicationState.PUBLISHED)
				.checksum("publication-checksum")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.publishedAt(Instant.now().minus(7, ChronoUnit.DAYS))
				.build();

		final var json = mapper.writeValueAsString(publication);

		assertThat(json)
				.as("Serialized Publication should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, Publication.class))
				.as("Deserialized Publication should be equal to the original")
				.isInstanceOf(DefaultPublication.class)
				.isEqualTo(publication);
	}

	@Test
	@DisplayName("should serialize and deserialize default manifest entry implementation")
	void serializeAndDeserializeManifestEntry() {
		final var entry = ManifestEntry.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.checksum("entry-checksum")
				.source(ArtifactSource.LOCAL)
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.resolvedAt(Instant.now().minus(7, ChronoUnit.DAYS))
				.build();

		final var json = mapper.writeValueAsString(entry);

		assertThat(json)
				.as("Serialized ManifestEntry should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, ManifestEntry.class))
				.as("Deserialized ManifestEntry should be equal to the original")
				.isInstanceOf(DefaultManifestEntry.class)
				.isEqualTo(entry);
	}

	@Test
	@DisplayName("should serialize and deserialize default service release entry implementation")
	void serializeAndDeserializeServiceReleaseEntry() {
		final var entry = ServiceReleaseEntry.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-crypto-api")
				.version("1.0.0")
				.status(ArtifactUploadStatus.UPLOAD_REQUIRED)
				.build();

		final var json = mapper.writeValueAsString(entry);

		assertThat(json)
				.as("Serialized ServiceReleaseEntry should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, ServiceReleaseEntry.class))
				.as("Deserialized ServiceReleaseEntry should be equal to the original")
				.isInstanceOf(DefaultServiceReleaseEntry.class)
				.isEqualTo(entry);
	}

	@Test
	@DisplayName("should serialize and deserialize default service release implementation")
	void serializeAndDeserializeServiceRelease() {
		final var release = ServiceRelease.builder()
				.id("konfigyr-service-release")
				.state(ReleaseState.FAILED)
				.artifact(ServiceReleaseEntry.builder()
						.groupId("com.konfigyr")
						.artifactId("konfigyr-crypto-api")
						.version("1.0.0")
						.status(ArtifactUploadStatus.UPLOAD_REQUIRED)
						.build())
				.error("artifact upload failed")
				.build();

		final var json = mapper.writeValueAsString(release);

		assertThat(json)
				.as("Serialized ServiceRelease should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, ServiceRelease.class))
				.as("Deserialized ServiceRelease should be equal to the original")
				.isInstanceOf(DefaultServiceRelease.class)
				.isEqualTo(release);
	}

	@Test
	@DisplayName("should serialize and deserialize default service release candidate implementation")
	void serializeAndDeserializeServiceReleaseCandidate() {
		final var candidate = ServiceReleaseCandidate.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-crypto-api")
				.version("1.0.0")
				.checksum("candidate-checksum")
				.build();

		final var json = mapper.writeValueAsString(candidate);

		assertThat(json)
				.as("Serialized ServiceReleaseCandidate should not be empty")
				.isNotBlank();

		assertThatObject(mapper.readValue(json, ServiceReleaseCandidate.class))
				.as("Deserialized ServiceReleaseCandidate should be equal to the original")
				.isInstanceOf(DefaultServiceReleaseCandidate.class)
				.isEqualTo(candidate);
	}

	@Test
	@DisplayName("should reject invalid artifact metadata payload")
	void deserializeInvalidMetadata() {
		final var json = mapper.getNodeFactory().objectNode()
				.put("name", "Konfigyr API")
				.set("properties", mapper.getNodeFactory().arrayNode()
						.add(mapper.getNodeFactory().objectNode())
				).toPrettyString();

		assertThatExceptionOfType(ValueInstantiationException.class)
				.as("Deserializing Artifact metadata missing required fields should fail")
				.isThrownBy(() -> mapper.readValue(json, ArtifactMetadata.class))
				.havingCause()
				.isInstanceOf(IllegalArgumentException.class)
				.withMessage("Property name can not be blank");
	}

}
