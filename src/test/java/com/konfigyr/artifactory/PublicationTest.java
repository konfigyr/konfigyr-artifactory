package com.konfigyr.artifactory;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PublicationTest {

	Artifact artifact = Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0");

	@Test
	@DisplayName("should create default publication using the fluent builder")
	void createDefaultPublication() {
		final var publication = Publication.builder()
				.artifact(artifact)
				.state(PublicationState.FAILED)
				.checksum("checksum")
				.error("some publication error")
				.error("and another publication error")
				.errors(List.of("yet another publication error", " "))
				.publishedAt(Instant.now())
				.build();

		assertThat(publication)
				.isNotNull()
				.returns(artifact.groupId(), Publication::groupId)
				.returns(artifact.artifactId(), Publication::artifactId)
				.returns(artifact.version(), Publication::version)
				.returns(artifact.name(), Publication::name)
				.returns(artifact.description(), Publication::description)
				.returns(artifact.website(), Publication::website)
				.returns(artifact.repository(), Publication::repository)
				.returns(PublicationState.FAILED, Publication::state)
				.returns("checksum", Publication::checksum)
				.returns(List.of("some publication error", "and another publication error", "yet another publication error"), Publication::errors)
				.returns(publication.publishedAt(), Publication::publishedAt);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = Publication.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifact(artifact)::build)
				.withMessage("Publication property metadata checksum can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.checksum("checksum")::build)
				.withMessage("Publication date can not be null");

		assertThat(builder.publishedAt(Instant.now()).build())
				.isNotNull()
				.returns(artifact.groupId(), Publication::groupId)
				.returns(artifact.artifactId(), Publication::artifactId)
				.returns(artifact.version(), Publication::version)
				.returns(artifact.name(), Publication::name)
				.returns(artifact.description(), Publication::description)
				.returns(artifact.website(), Publication::website)
				.returns(artifact.repository(), Publication::repository)
				.returns(PublicationState.PENDING, Publication::state)
				.returns("checksum", Publication::checksum)
				.returns(List.of(), Publication::errors)
				.extracting(Publication::publishedAt, InstanceOfAssertFactories.INSTANT)
				.isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
	}

	@Test
	@DisplayName("errors should treat a null iterable as a no-op")
	void errorsIgnoresNullIterable() {
		final var publication = Publication.builder()
				.artifact(artifact)
				.checksum("checksum")
				.error("some publication error")
				.errors(null)
				.publishedAt(Instant.now())
				.build();

		assertThat(publication.errors()).containsExactly("some publication error");
	}

}
