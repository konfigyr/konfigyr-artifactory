package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceReleaseCandidateTest {

	Artifact artifact = Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0");

	@Test
	@DisplayName("should create default service release candidate using the fluent builder")
	void createDefaultServiceReleaseCandidate() {
		final var candidate = ServiceReleaseCandidate.builder()
				.artifact(artifact)
				.checksum("checksum")
				.build();

		assertThat(candidate)
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), ServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), ServiceReleaseCandidate::version)
				.returns(artifact.name(), ServiceReleaseCandidate::name)
				.returns(artifact.description(), ServiceReleaseCandidate::description)
				.returns(artifact.website(), ServiceReleaseCandidate::website)
				.returns(artifact.repository(), ServiceReleaseCandidate::repository)
				.returns("checksum", ServiceReleaseCandidate::checksum);
	}

	@Test
	@DisplayName("should create service release candidate using Maven coordinates and checksum")
	void createServiceReleaseCandidateFromCoordinates() {
		final var candidate = ServiceReleaseCandidate.of(
				artifact.groupId(), artifact.artifactId(), artifact.version(), "checksum");

		assertThat(candidate)
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), ServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), ServiceReleaseCandidate::version)
				.returns("checksum", ServiceReleaseCandidate::checksum);
	}

	@Test
	@DisplayName("should create service release candidate using an artifact and checksum")
	void createServiceReleaseCandidateFromArtifact() {
		final var candidate = ServiceReleaseCandidate.of(artifact, "checksum");

		assertThat(candidate)
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), ServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), ServiceReleaseCandidate::version)
				.returns(artifact.name(), ServiceReleaseCandidate::name)
				.returns(artifact.description(), ServiceReleaseCandidate::description)
				.returns(artifact.website(), ServiceReleaseCandidate::website)
				.returns(artifact.repository(), ServiceReleaseCandidate::repository)
				.returns("checksum", ServiceReleaseCandidate::checksum);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = ServiceReleaseCandidate.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifact(artifact)::build)
				.withMessage("Service release candidate checksum can not be blank");

		assertThat(builder.checksum("checksum").build())
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), ServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), ServiceReleaseCandidate::version)
				.returns("checksum", ServiceReleaseCandidate::checksum);
	}

}
