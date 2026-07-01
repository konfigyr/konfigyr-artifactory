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
