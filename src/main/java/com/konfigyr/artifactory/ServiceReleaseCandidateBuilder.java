package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

/**
 * Abstract builder class used to create new instances of the {@link ServiceReleaseCandidate} that can
 * be reused for different implementations.
 * <p>
 * Follows the same "an {@link Artifact} plus more" pattern established by {@link PublicationBuilder},
 * {@link ManifestEntryBuilder}, and {@link ServiceReleaseEntryBuilder} in this SDK, extending
 * {@link ArtifactBuilder} and adding only the {@code checksum} field this type needs.
 *
 * @param <T> the service release candidate implementation that this builder should build
 * @param <B> the builder generic type
 * @author Vladimir Spasic
 * @see ServiceReleaseCandidate
 * @since 1.0.0
 */
public abstract class ServiceReleaseCandidateBuilder<T extends ServiceReleaseCandidate, B extends ServiceReleaseCandidateBuilder<T, B>> extends ArtifactBuilder<T, B> {

	/**
	 * Checksum of the {@link ArtifactMetadata} resolved locally for this candidate.
	 */
	protected @Nullable String checksum;

	/**
	 * Creates a new {@link ServiceReleaseCandidateBuilder} instance.
	 */
	protected ServiceReleaseCandidateBuilder() {
	}

	/**
	 * Specify the {@link Artifact} that this service release candidate describes.
	 *
	 * @param artifact the artifact
	 * @return service release candidate builder
	 */
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
	 * Specify the checksum of the {@link ArtifactMetadata} resolved locally for this candidate.
	 *
	 * @param checksum the metadata checksum
	 * @return service release candidate builder
	 */
	public B checksum(String checksum) {
		this.checksum = checksum;
		return myself();
	}

	@Override
	protected void validate() {
		super.validate();

		if (checksum == null || checksum.isBlank()) {
			throw new IllegalArgumentException("Service release candidate checksum can not be blank");
		}
	}

}
