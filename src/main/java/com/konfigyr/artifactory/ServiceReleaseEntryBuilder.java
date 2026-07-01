package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

/**
 * Abstract builder class used to create new instances of the {@link ServiceReleaseEntry} that can be
 * reused for different implementations.
 * <p>
 * Follows the same "an {@link Artifact} plus more" pattern established by {@link PublicationBuilder}
 * and {@link ManifestEntryBuilder} in this SDK, extending {@link ArtifactBuilder} and adding only the
 * {@link ArtifactUploadStatus} field this type needs.
 *
 * @param <T> the service release entry implementation that this builder should build
 * @param <B> the builder generic type
 * @author Vladimir Spasic
 * @see ServiceReleaseEntry
 * @since 1.0.0
 */
public abstract class ServiceReleaseEntryBuilder<T extends ServiceReleaseEntry, B extends ServiceReleaseEntryBuilder<T, B>> extends ArtifactBuilder<T, B> {

	/**
	 * Whether the build plugin still needs to upload this artifact's metadata.
	 */
	protected ArtifactUploadStatus status;

	/**
	 * Creates a new {@link ServiceReleaseEntryBuilder} instance.
	 */
	protected ServiceReleaseEntryBuilder() {
	}

	/**
	 * Specify the {@link Artifact} that this service release entry describes.
	 *
	 * @param artifact the artifact
	 * @return service release entry builder
	 */
	@NonNull
	public B artifact(Artifact artifact) {
		return groupId(artifact.groupId())
				.artifactId(artifact.artifactId())
				.version(artifact.version())
				.name(artifact.name())
				.description(artifact.description())
				.website(artifact.website())
				.repository(artifact.repository());
	}

	/**
	 * Specify whether the build plugin still needs to upload this artifact's metadata.
	 *
	 * @param status the artifact upload status
	 * @return service release entry builder
	 */
	@NonNull
	public B status(ArtifactUploadStatus status) {
		this.status = status;
		return myself();
	}

	/**
	 * Creates the {@link ServiceReleaseEntry} as a result of this builder.
	 *
	 * @return the service release entry instance, never {@literal null}.
	 */
	@NonNull
	public abstract T build();

}
