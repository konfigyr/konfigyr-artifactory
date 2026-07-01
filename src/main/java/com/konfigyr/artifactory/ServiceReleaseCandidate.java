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
