package com.konfigyr.artifactory;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

class ManifestEntryTest {

	Artifact artifact = Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0");

	@Test
	@DisplayName("should create default manifest entry using the fluent builder")
	void createDefaultManifestEntry() {
		final var entry = ManifestEntry.builder()
				.artifact(artifact)
				.checksum("checksum")
				.source(ArtifactSource.ARTIFACTORY)
				.resolvedAt(Instant.now())
				.build();

		assertThat(entry)
				.isNotNull()
				.returns(artifact.groupId(), ManifestEntry::groupId)
				.returns(artifact.artifactId(), ManifestEntry::artifactId)
				.returns(artifact.version(), ManifestEntry::version)
				.returns(artifact.name(), ManifestEntry::name)
				.returns(artifact.description(), ManifestEntry::description)
				.returns(artifact.website(), ManifestEntry::website)
				.returns(artifact.repository(), ManifestEntry::repository)
				.returns("checksum", ManifestEntry::checksum)
				.returns(ArtifactSource.ARTIFACTORY, ManifestEntry::source)
				.returns(entry.resolvedAt(), ManifestEntry::resolvedAt);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = ManifestEntry.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifact(artifact)::build)
				.withMessage("Manifest entry checksum can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.checksum("checksum")::build)
				.withMessage("Manifest entry source can not be null");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.source(ArtifactSource.LOCAL)::build)
				.withMessage("Manifest entry resolution date can not be null");

		assertThat(builder.resolvedAt(Instant.now()).build())
				.isNotNull()
				.returns(artifact.groupId(), ManifestEntry::groupId)
				.returns(artifact.artifactId(), ManifestEntry::artifactId)
				.returns(artifact.version(), ManifestEntry::version)
				.returns("checksum", ManifestEntry::checksum)
				.returns(ArtifactSource.LOCAL, ManifestEntry::source)
				.extracting(ManifestEntry::resolvedAt, InstanceOfAssertFactories.INSTANT)
				.isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
	}

}
