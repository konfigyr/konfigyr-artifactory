package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ArtifactMetadataTest {

	@Test
	@DisplayName("should create default artifact metadata using the fluent builder")
	void createDefaultArtifactMetadata() {
		final var first = PropertyDescriptor.builder()
				.name("spring.application.group")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.build();

		final var second = PropertyDescriptor.builder()
				.name("spring.application.name")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.build();

		final var metadata = ArtifactMetadata.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.checksum("checksum")
				.property(second)
				.property(first)
				.properties(null)
				.build();

		assertThatObject(metadata)
				.isNotNull()
				.returns("com.konfigyr", Artifact::groupId)
				.returns("konfigyr-artifactory", Artifact::artifactId)
				.returns("1.0.0", Artifact::version)
				.returns("Konfigyr Artifactory", Artifact::name)
				.returns("Konfigyr Artifactory library for Java.", Artifact::description)
				.returns(URI.create("https://konfigyr.com"), Artifact::website)
				.returns(URI.create("https://github.com/konfigyr/konfigyr-artifactory"), Artifact::repository)
				.returns("checksum", ArtifactMetadata::checksum);

		assertThatIterable(metadata)
				.hasSize(2)
				.containsExactly(first, second);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = ArtifactMetadata.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.groupId("com.konfigyr")::build)
				.withMessage("Artifact artifactId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifactId("konfigyr-artifactory")::build)
				.withMessage("Artifact version can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.version("1.0.0")::build)
				.withMessage("Artifact metadata must contain at least one property descriptor");

		final var property = PropertyDescriptor.builder()
				.name("spring.application.group")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.build();

		assertThatObject(builder.property(property).build())
				.isNotNull()
				.returns("com.konfigyr", Artifact::groupId)
				.returns("konfigyr-artifactory", Artifact::artifactId)
				.returns("1.0.0", Artifact::version)
				.returns(null, Artifact::name)
				.returns(null, Artifact::description)
				.returns(null, Artifact::website)
				.returns(null, Artifact::repository)
				.returns(null, ArtifactMetadata::checksum)
				.returns(List.of(property), ArtifactMetadata::properties);
	}

	@Test
	@DisplayName("should fail to create metadata when creating without property descriptors")
	void createMetadataWithoutPropertyDescriptors() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> ArtifactMetadata.of("com.konfigyr", "konfigyr-artifactory", "1.0.1"))
				.withMessage("Artifact metadata must contain at least one property descriptor");
	}

}
