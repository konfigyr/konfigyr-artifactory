package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;

/**
 * The outcome of resolving a single {@link Artifact} against the Konfigyr {@code Artifactory} as part
 * of a {@link ServiceRelease}: whether the build plugin still needs to upload this artifact's
 * {@link ArtifactMetadata}.
 *
 * @author Vladimir Spasic
 * @see ArtifactUploadStatus
 * @see ServiceRelease
 * @since 1.0.0
 */
public interface ServiceReleaseEntry extends Artifact, Serializable {

	/**
	 * Creates a new instance of the {@link ServiceReleaseEntry} using the Maven coordinates
	 * and the {@link ArtifactUploadStatus}.
	 * <p>
	 * This is a convenience factory method that constructs a {@link ServiceReleaseEntry} with
	 * the given Maven coordinates and status, equivalent to invoking:
	 *
	 * <pre>{@code
	 * ServiceReleaseEntry artifact = ServiceReleaseEntry.builder()
	 *     .groupId(groupId)
	 *     .artifactId(artifactId)
	 *     .version(version)
	 *     .status(status)
	 *     .build();
	 * }</pre>
	 *
	 * @param groupId    the Maven {@code groupId}, e.g. {@code org.springframework.boot}.
	 * @param artifactId the Maven {@code artifactId}, e.g. {@code spring-boot-starter-web}.
	 * @param version    the Maven {@code version}, e.g. {@code 3.3.1}.
	 * @param status     the upload status for the entry, can't be {@literal null}
	 * @return a new immutable {@link ServiceReleaseEntry} instance, never {@literal null}.
	 */
	@NonNull
	static ServiceReleaseEntry of(String groupId, String artifactId, String version, ArtifactUploadStatus status) {
		return builder()
				.groupId(groupId)
				.artifactId(artifactId)
				.version(version)
				.status(status)
				.build();
	}

	/**
	 * Creates a new instance of the {@link ServiceReleaseEntry} using the given {@link Artifact}
	 * and upload status.
	 *
	 * @param artifact the artifact to be added to the service release, can't be {@literal null}
	 * @param status   the upload status for the entry, can't be {@literal null}
	 * @return a new immutable {@link ServiceReleaseEntry} instance, never {@literal null}.
	 */
	static ServiceReleaseEntry of(Artifact artifact, ArtifactUploadStatus status) {
		return builder().artifact(artifact).status(status).build();
	}

	/**
	 * Creates a new instance of the {@link DefaultServiceReleaseEntry.Builder} used to create a new
	 * instance of the {@link DefaultServiceReleaseEntry} using the fluent builder API.
	 *
	 * @return default service release entry builder, never {@literal null}
	 */
	static DefaultServiceReleaseEntry.Builder builder() {
		return new DefaultServiceReleaseEntry.Builder();
	}

	/**
	 * Whether the build plugin still needs to upload this artifact's {@link ArtifactMetadata}.
	 *
	 * @return artifact upload status, never {@literal null}.
	 */
	@NonNull
	ArtifactUploadStatus status();

}
