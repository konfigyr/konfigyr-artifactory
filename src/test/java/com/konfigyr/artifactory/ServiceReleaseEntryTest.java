package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ServiceReleaseEntryTest {

	Artifact artifact = Artifact.of("com.konfigyr", "konfigyr-artifactory", "1.0.0");

	@Test
	@DisplayName("should create default service release entry using the fluent builder")
	void createDefaultServiceReleaseEntry() {
		final var entry = ServiceReleaseEntry.builder()
				.artifact(artifact)
				.status(ArtifactUploadStatus.UPLOAD_REQUIRED)
				.build();

		assertThat(entry)
				.isNotNull()
				.returns(artifact.groupId(), DefaultServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), DefaultServiceReleaseEntry::artifactId)
				.returns(artifact.version(), DefaultServiceReleaseEntry::version)
				.returns(artifact.name(), DefaultServiceReleaseEntry::name)
				.returns(artifact.description(), DefaultServiceReleaseEntry::description)
				.returns(artifact.website(), DefaultServiceReleaseEntry::website)
				.returns(artifact.repository(), DefaultServiceReleaseEntry::repository)
				.returns(ArtifactUploadStatus.UPLOAD_REQUIRED, DefaultServiceReleaseEntry::status);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = ServiceReleaseEntry.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Artifact groupId can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.artifact(artifact)::build)
				.withMessage("Artifact upload status can not be null");

		assertThat(builder.status(ArtifactUploadStatus.SKIP).build())
				.isNotNull()
				.returns(artifact.groupId(), DefaultServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), DefaultServiceReleaseEntry::artifactId)
				.returns(artifact.version(), DefaultServiceReleaseEntry::version)
				.returns(ArtifactUploadStatus.SKIP, DefaultServiceReleaseEntry::status);
	}

}
