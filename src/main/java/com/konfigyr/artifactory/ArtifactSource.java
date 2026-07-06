package com.konfigyr.artifactory;

/**
 * Describes where a {@link ManifestEntry}'s {@link ArtifactMetadata} originated from.
 * <p>
 * This is modeled as a named enum rather than a boolean because the distinction is a real,
 * potentially user-facing property of a settled {@link Manifest} entry. For example, a future
 * Konfigyr UI could surface where a service's configuration documentation actually comes from, not
 * just an internal implementation flag.
 *
 * @author Vladimir Spasic
 * @see ManifestEntry
 * @since 1.0.0
 */
public enum ArtifactSource {

	/**
	 * The {@link ArtifactMetadata} was uploaded directly by the build plugin as part of the service's
	 * own build, i.e. it is first-party metadata belonging to the service itself.
	 */
	LOCAL,

	/**
	 * The {@link ArtifactMetadata} was resolved from an {@link Artifact} that is already indexed in the
	 * Konfigyr {@code Artifactory}, i.e. it is metadata for a dependency the service uses rather than
	 * metadata the service itself produced.
	 */
	ARTIFACTORY

}
