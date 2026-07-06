package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ServiceReleaseTest {

	@Test
	@DisplayName("should create default service release using the fluent builder")
	void createDefaultServiceRelease() {
		final var entry = ServiceReleaseEntry.builder()
				.artifact(Artifact.of("com.konfigyr", "konfigyr-crypto-api", "1.0.0"))
				.status(ArtifactUploadStatus.SKIP)
				.build();

		final var publishedAt = Instant.now();

		final var release = ServiceRelease.builder()
				.id("konfigyr-service-release")
				.state(ReleaseState.RELEASED)
				.artifact(entry)
				.publishedAt(publishedAt)
				.error("some release error")
				.error("and another release error")
				.errors(List.of("yet another release error", " "))
				.build();

		assertThat(release)
				.isNotNull()
				.returns("konfigyr-service-release", ServiceRelease::id)
				.returns(ReleaseState.RELEASED, ServiceRelease::state)
				.returns(List.of(entry), ServiceRelease::artifacts)
				.returns(publishedAt, ServiceRelease::publishedAt)
				.returns(List.of("some release error", "and another release error", "yet another release error"), ServiceRelease::errors);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = ServiceRelease.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Service release identifier can not be blank");

		assertThat(builder.id("konfigyr-service-release").build())
				.isNotNull()
				.returns("konfigyr-service-release", ServiceRelease::id)
				.returns(ReleaseState.PENDING, ServiceRelease::state)
				.returns(List.of(), ServiceRelease::artifacts)
				.returns(null, ServiceRelease::publishedAt)
				.returns(List.of(), ServiceRelease::errors);
	}

}
