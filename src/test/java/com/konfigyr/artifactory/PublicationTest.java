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
				.returns(artifact.groupId(), DefaultPublication::groupId)
				.returns(artifact.artifactId(), DefaultPublication::artifactId)
				.returns(artifact.version(), DefaultPublication::version)
				.returns(artifact.name(), DefaultPublication::name)
				.returns(artifact.description(), DefaultPublication::description)
				.returns(artifact.website(), DefaultPublication::website)
				.returns(artifact.repository(), DefaultPublication::repository)
				.returns(PublicationState.FAILED, DefaultPublication::state)
				.returns("checksum", DefaultPublication::checksum)
				.returns(List.of("some publication error", "and another publication error", "yet another publication error"), DefaultPublication::errors)
				.returns(publication.publishedAt(), DefaultPublication::publishedAt);
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
				.returns(artifact.groupId(), DefaultPublication::groupId)
				.returns(artifact.artifactId(), DefaultPublication::artifactId)
				.returns(artifact.version(), DefaultPublication::version)
				.returns(artifact.name(), DefaultPublication::name)
				.returns(artifact.description(), DefaultPublication::description)
				.returns(artifact.website(), DefaultPublication::website)
				.returns(artifact.repository(), DefaultPublication::repository)
				.returns(PublicationState.PENDING, DefaultPublication::state)
				.returns("checksum", DefaultPublication::checksum)
				.returns(List.of(), DefaultPublication::errors)
				.extracting(DefaultPublication::publishedAt, InstanceOfAssertFactories.INSTANT)
				.isCloseTo(Instant.now(), within(500, ChronoUnit.MILLIS));
	}

}
