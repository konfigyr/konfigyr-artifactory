package com.konfigyr.artifactory;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ManifestTest {

	static final Instant RESOLVED_AT = Instant.now();

	static ManifestEntry entry(String groupId, String artifactId, String version) {
		return ManifestEntry.builder()
				.groupId(groupId)
				.artifactId(artifactId)
				.version(version)
				.checksum("checksum")
				.source(ArtifactSource.LOCAL)
				.resolvedAt(RESOLVED_AT)
				.build();
	}

	@Test
	@DisplayName("should create default service manifest using the fluent builder")
	void createDefaultManifest() {
		final var manifest = Manifest.builder()
				.id("konfigyr-service")
				.name("Konfigyr example service")
				.artifacts(List.of(
						entry("com.konfigyr", "konfigyr-crypto-api", "1.0.0"),
						entry("com.konfigyr", "konfigyr-crypto-tink", "1.0.0")
				))
				.artifact(entry("com.konfigyr", "konfigyr-artifactory", "1.0.0"))
				.createdAt(Instant.now().minusSeconds(6000))
				.build();

		assertThatObject(manifest)
				.isNotNull()
				.returns("konfigyr-service", Manifest::id)
				.returns("Konfigyr example service", Manifest::name)
				.returns(manifest.createdAt(), Manifest::createdAt);

		assertThat(manifest)
				.hasSize(3)
				.containsExactly(
						entry("com.konfigyr", "konfigyr-artifactory", "1.0.0"),
						entry("com.konfigyr", "konfigyr-crypto-api", "1.0.0"),
						entry("com.konfigyr", "konfigyr-crypto-tink", "1.0.0")
				);
	}

	@Test
	@DisplayName("should find artifact by groupId and artifactId")
	void findArtifact() {
		final var entry = entry("com.konfigyr", "konfigyr-crypto-api", "1.0.0");

		final var manifest = Manifest.builder()
				.id("konfigyr-service")
				.name("Konfigyr example service")
				.artifact(entry)
				.build();

		assertThat(manifest.find("com.konfigyr", "konfigyr-crypto-api"))
				.isNotEmpty()
				.get()
				.isSameAs(entry);

		assertThat(manifest.find("com.konfigyr", "konfigyr-crypto-jdbc"))
				.isEmpty();

		assertThat(manifest.find("com.other", "konfigyr-crypto-api"))
				.isEmpty();
	}

	@Test
	@DisplayName("should check if artifact is in the manifest")
	void containsArtifact() {
		final var entry = entry("com.konfigyr", "konfigyr-crypto-api", "1.0.0");

		final var manifest = Manifest.builder()
				.id("konfigyr-service")
				.name("Konfigyr example service")
				.artifact(entry)
				.build();

		assertThat(manifest.contains(entry))
				.isTrue();

		assertThat(manifest.contains(Artifact.of("com.konfigyr", "konfigyr-crypto-api", "1.0.0")))
				.isTrue();

		assertThat(manifest.contains(Artifact.of("com.konfigyr", "konfigyr-crypto-api", "1.0.1")))
				.isFalse();

		assertThat(manifest.contains(Artifact.of("com.konfigyr", "konfigyr-crypto-jdbc", "1.0.0")))
				.isFalse();

		assertThat(manifest.contains(Artifact.of("com.other", "konfigyr-crypto-api", "1.0.0")))
				.isFalse();
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = Manifest.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Service identifier can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.id("id")::build)
				.withMessage("Service name can not be blank");

		assertThatObject(builder.name("name").build())
				.isNotNull()
				.returns("id", Manifest::id)
				.returns("name", Manifest::name)
				.returns(List.of(), Manifest::artifacts)
				.extracting(Manifest::createdAt, InstanceOfAssertFactories.INSTANT)
				.isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
	}

}
