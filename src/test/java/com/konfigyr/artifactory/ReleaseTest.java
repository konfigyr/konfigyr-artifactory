package com.konfigyr.artifactory;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReleaseTest {

	Artifact artifact = Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0");

	@Test
	@DisplayName("should create default release using the fluent builder")
	void createDefaultRelease() {
		final var release = Release.builder()
				.artifact(artifact)
				.state(ReleaseState.FAILED)
				.checksum("checksum")
				.error("some release error")
				.error("and another release error")
				.errors(List.of("yet another release error", " "))
				.releaseDate(Instant.now())
				.build();

		assertThat(release)
				.isNotNull()
				.returns(artifact.groupId(), DefaultRelease::groupId)
				.returns(artifact.artifactId(), DefaultRelease::artifactId)
				.returns(artifact.version(), DefaultRelease::version)
				.returns(artifact.name(), DefaultRelease::name)
				.returns(artifact.description(), DefaultRelease::description)
				.returns(artifact.website(), DefaultRelease::website)
				.returns(artifact.repository(), DefaultRelease::repository)
				.returns(ReleaseState.FAILED, DefaultRelease::state)
				.returns("checksum", DefaultRelease::checksum)
				.returns(List.of("some release error", "and another release error", "yet another release error"), DefaultRelease::errors)
				.returns(release.releaseDate(), DefaultRelease::releaseDate);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = Release.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifact(artifact)::build)
				.withMessage("Release property metadata checksum can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.checksum("checksum")::build)
				.withMessage("Release date can not be null");

		assertThat(builder.releaseDate(Instant.now()).build())
				.isNotNull()
				.returns(artifact.groupId(), DefaultRelease::groupId)
				.returns(artifact.artifactId(), DefaultRelease::artifactId)
				.returns(artifact.version(), DefaultRelease::version)
				.returns(artifact.name(), DefaultRelease::name)
				.returns(artifact.description(), DefaultRelease::description)
				.returns(artifact.website(), DefaultRelease::website)
				.returns(artifact.repository(), DefaultRelease::repository)
				.returns(ReleaseState.PENDING, DefaultRelease::state)
				.returns("checksum", DefaultRelease::checksum)
				.returns(List.of(), DefaultRelease::errors)
				.extracting(DefaultRelease::releaseDate, InstanceOfAssertFactories.INSTANT)
				.isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
	}

}
