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
				.returns(artifact.groupId(), DefaultServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), DefaultServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), DefaultServiceReleaseCandidate::version)
				.returns(artifact.name(), DefaultServiceReleaseCandidate::name)
				.returns(artifact.description(), DefaultServiceReleaseCandidate::description)
				.returns(artifact.website(), DefaultServiceReleaseCandidate::website)
				.returns(artifact.repository(), DefaultServiceReleaseCandidate::repository)
				.returns("checksum", DefaultServiceReleaseCandidate::checksum);
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
				.returns(artifact.groupId(), DefaultServiceReleaseCandidate::groupId)
				.returns(artifact.artifactId(), DefaultServiceReleaseCandidate::artifactId)
				.returns(artifact.version(), DefaultServiceReleaseCandidate::version)
				.returns("checksum", DefaultServiceReleaseCandidate::checksum);
	}

}
