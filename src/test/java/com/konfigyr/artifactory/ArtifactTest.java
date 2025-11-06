package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ArtifactTest {

	@Test
	@DisplayName("should create default artifact using the fluent builder")
	void createDefaultArtifact() {
		final var artifact = Artifact.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.build();

		assertThat(artifact)
				.isNotNull()
				.returns("com.konfigyr", Artifact::groupId)
				.returns("konfigyr-artifactory", Artifact::artifactId)
				.returns("1.0.0", Artifact::version)
				.returns("Konfigyr Artifactory", Artifact::name)
				.returns("Konfigyr Artifactory library for Java.", Artifact::description)
				.returns(URI.create("https://konfigyr.com"), Artifact::website)
				.returns(URI.create("https://github.com/konfigyr/konfigyr-artifactory"), Artifact::repository);
	}

	@Test
	@DisplayName("should create artifact metadata")
	void createArtifactMetadata() {
		final var artifact = Artifact.builder()
				.groupId("com.konfigyr")
				.artifactId("konfigyr-artifactory")
				.version("1.0.0")
				.name("Konfigyr Artifactory")
				.description("Konfigyr Artifactory library for Java.")
				.website("https://konfigyr.com")
				.repository("https://github.com/konfigyr/konfigyr-artifactory")
				.build();

		final var descriptors = List.of(
				PropertyDescriptor.builder()
						.name("spring.application.group")
						.typeName("java.lang.String")
						.schema("{\"type\":\"string\"}")
						.build(),
				PropertyDescriptor.builder()
						.name("spring.application.name")
						.typeName("java.lang.String")
						.schema("{\"type\":\"string\"}")
						.build()
		);

		assertThatObject(artifact.toMetadata(descriptors))
				.isNotNull()
				.returns("com.konfigyr", Artifact::groupId)
				.returns("konfigyr-artifactory", Artifact::artifactId)
				.returns("1.0.0", Artifact::version)
				.returns("Konfigyr Artifactory", Artifact::name)
				.returns("Konfigyr Artifactory library for Java.", Artifact::description)
				.returns(URI.create("https://konfigyr.com"), Artifact::website)
				.returns(URI.create("https://github.com/konfigyr/konfigyr-artifactory"), Artifact::repository)
				.returns(descriptors, ArtifactMetadata::properties);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = Artifact.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.groupId("com.konfigyr")::build)
				.withMessage("Artifact artifactId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifactId("konfigyr-artifactory")::build)
				.withMessage("Artifact version can not be blank");

		assertThat(builder.version("1.0.0").build())
				.isNotNull()
				.returns("com.konfigyr", Artifact::groupId)
				.returns("konfigyr-artifactory", Artifact::artifactId)
				.returns("1.0.0", Artifact::version)
				.returns(null, Artifact::name)
				.returns(null, DefaultArtifact::description)
				.returns(null, DefaultArtifact::website)
				.returns(null, DefaultArtifact::repository);
	}

	@Test
	@DisplayName("should sort artifacts based on Maven coordinates")
	void sortedArtifacts() {
		final var artifacts = Stream.of(
				Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.1"),
				Artifact.of("com.konfigyr", "konfigyr-gradle-plugin", "1.0.0"),
				Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0"),
				Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.1.0"),
				Artifact.of("com.konfigyr", "konfigyr-gradle-plugin", "2.0.0")
		);

		assertThat(artifacts.sorted())
				.containsExactly(
						Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0"),
						Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.1"),
						Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.1.0"),
						Artifact.of("com.konfigyr", "konfigyr-gradle-plugin", "1.0.0"),
						Artifact.of("com.konfigyr", "konfigyr-gradle-plugin", "2.0.0")
				);
	}

}
