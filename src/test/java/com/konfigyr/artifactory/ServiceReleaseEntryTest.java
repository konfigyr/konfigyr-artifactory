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
				.returns(artifact.groupId(), ServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), ServiceReleaseEntry::artifactId)
				.returns(artifact.version(), ServiceReleaseEntry::version)
				.returns(artifact.name(), ServiceReleaseEntry::name)
				.returns(artifact.description(), ServiceReleaseEntry::description)
				.returns(artifact.website(), ServiceReleaseEntry::website)
				.returns(artifact.repository(), ServiceReleaseEntry::repository)
				.returns(ArtifactUploadStatus.UPLOAD_REQUIRED, ServiceReleaseEntry::status);
	}

	@Test
	@DisplayName("should create service release entry using Maven coordinates and status")
	void createServiceReleaseEntryFromCoordinates() {
		final var entry = ServiceReleaseEntry.of(artifact.groupId(), artifact.artifactId(), artifact.version(),
				ArtifactUploadStatus.UPLOAD_REQUIRED);

		assertThat(entry)
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), ServiceReleaseEntry::artifactId)
				.returns(artifact.version(), ServiceReleaseEntry::version)
				.returns(ArtifactUploadStatus.UPLOAD_REQUIRED, ServiceReleaseEntry::status);
	}

	@Test
	@DisplayName("should create service release entry using an artifact and status")
	void createServiceReleaseEntryFromArtifact() {
		final var entry = ServiceReleaseEntry.of(artifact, ArtifactUploadStatus.UPLOAD_REQUIRED);

		assertThat(entry)
				.isNotNull()
				.returns(artifact.groupId(), ServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), ServiceReleaseEntry::artifactId)
				.returns(artifact.version(), ServiceReleaseEntry::version)
				.returns(artifact.name(), ServiceReleaseEntry::name)
				.returns(artifact.description(), ServiceReleaseEntry::description)
				.returns(artifact.website(), ServiceReleaseEntry::website)
				.returns(artifact.repository(), ServiceReleaseEntry::repository)
				.returns(ArtifactUploadStatus.UPLOAD_REQUIRED, ServiceReleaseEntry::status);
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
				.returns(artifact.groupId(), ServiceReleaseEntry::groupId)
				.returns(artifact.artifactId(), ServiceReleaseEntry::artifactId)
				.returns(artifact.version(), ServiceReleaseEntry::version)
				.returns(ArtifactUploadStatus.SKIP, ServiceReleaseEntry::status);
	}

}
