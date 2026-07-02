package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;

/**
 * A single {@link Artifact} coordinate paired with the checksum of the {@link ArtifactMetadata} the
 * build plugin resolved locally for it, submitted by the build plugin as part of the request to create
 * a new {@link ServiceRelease}.
 *
 * @author Vladimir Spasic
 * @see ServiceRelease
 * @see ServiceReleaseEntry
 * @since 1.0.0
 */
public interface ServiceReleaseCandidate extends Artifact, Serializable {

	/**
	 * Creates a new instance of the {@link ServiceReleaseCandidate} using the Maven coordinates
	 * and the Spring Configuration Metadata checksum.
	 * <p>
	 * This is a convenience factory method that constructs a {@link ServiceReleaseCandidate} with
	 * the given Maven coordinates and checksum, equivalent to invoking:
	 *
	 * <pre>{@code
	 * ServiceReleaseCandidate artifact = ServiceReleaseCandidate.builder()
	 *     .groupId(groupId)
	 *     .artifactId(artifactId)
	 *     .version(version)
	 *     .checksum(checksum)
	 *     .build();
	 * }</pre>
	 *
	 * @param groupId    the Maven {@code groupId}, e.g. {@code org.springframework.boot}.
	 * @param artifactId the Maven {@code artifactId}, e.g. {@code spring-boot-starter-web}.
	 * @param version    the Maven {@code version}, e.g. {@code 3.3.1}.
	 * @param checksum   the Spring Configuration Metadata checksum, can't be {@literal null}
	 * @return a new immutable {@link ServiceReleaseCandidate} instance, never {@literal null}.
	 */
	@NonNull
	static ServiceReleaseCandidate of(String groupId, String artifactId, String version, String checksum) {
		return builder()
				.groupId(groupId)
				.artifactId(artifactId)
				.version(version)
				.checksum(checksum)
				.build();
	}

	/**
	 * Creates a new instance of the {@link ServiceReleaseCandidate} using the given {@link Artifact}
	 * and Spring Configuration Metadata checksum.
	 *
	 * @param artifact the artifact to be considered as a release candidate, can't be {@literal null}
	 * @param checksum the Spring Configuration Metadata checksum, can't be {@literal null}
	 * @return a new immutable {@link ServiceReleaseCandidate} instance, never {@literal null}.
	 */
	static ServiceReleaseCandidate of(Artifact artifact, String checksum) {
		return builder().artifact(artifact).checksum(checksum).build();
	}

	/**
	 * Creates a new instance of the {@link ServiceReleaseCandidate} using the given {@link ArtifactMetadata}.
	 *
	 * @param metadata the artifact metadata to be considered as a release candidate, can't be {@literal null}
	 * @return a new immutable {@link ServiceReleaseCandidate} instance, never {@literal null}.
	 */
	static ServiceReleaseCandidate of(ArtifactMetadata metadata) {
		return of(metadata, metadata.checksum());
	}

	/**
	 * Creates a new instance of the {@link DefaultServiceReleaseCandidate.Builder} used to create a
	 * new instance of the {@link DefaultServiceReleaseCandidate} using the fluent builder API.
	 *
	 * @return default service release candidate builder, never {@literal null}
	 */
	static DefaultServiceReleaseCandidate.Builder builder() {
		return new DefaultServiceReleaseCandidate.Builder();
	}

	/**
	 * Checksum of the {@link ArtifactMetadata} the build plugin resolved for this artifact locally.
	 *
	 * @return service release candidate checksum, never {@literal null}.
	 */
	@NonNull
	String checksum();

}
