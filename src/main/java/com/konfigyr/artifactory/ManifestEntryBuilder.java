package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Abstract builder class used to create new instances of the {@link ManifestEntry} that can be reused
 * for different implementations.
 * <p>
 * Follows the same "an {@link Artifact} plus more" pattern established by {@link PublicationBuilder} in
 * this SDK, extending {@link ArtifactBuilder} rather than {@link PublicationBuilder} itself, since a
 * {@link ManifestEntry} is not a {@link Publication}, see {@link ManifestEntry}'s Javadoc for why.
 *
 * @param <T> the manifest entry implementation that this builder should build
 * @param <B> the builder generic type
 * @author Vladimir Spasic
 * @see ManifestEntry
 * @since 1.0.0
 */
public abstract class ManifestEntryBuilder<T extends ManifestEntry, B extends ManifestEntryBuilder<T, B>> extends ArtifactBuilder<T, B> {

	/**
	 * Checksum of the {@link ArtifactMetadata property metadata} captured for this manifest entry.
	 */
	protected @Nullable String checksum;

	/**
	 * Where this entry's metadata originated from.
	 */
	protected @Nullable ArtifactSource source;

	/**
	 * Timestamp when this entry's metadata was captured.
	 */
	protected @Nullable Instant resolvedAt;

	/**
	 * Creates a new {@link ManifestEntryBuilder} instance.
	 */
	protected ManifestEntryBuilder() {
	}

	/**
	 * Specify the {@link Artifact} that this manifest entry describes.
	 *
	 * @param artifact the artifact
	 * @return manifest entry builder
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
	 * Specify the checksum of the {@link ArtifactMetadata property metadata} captured for this entry.
	 *
	 * @param checksum the metadata checksum
	 * @return manifest entry builder
	 */
	public B checksum(String checksum) {
		this.checksum = checksum;
		return myself();
	}

	/**
	 * Specify where this entry's metadata originated from.
	 *
	 * @param source the manifest entry source
	 * @return manifest entry builder
	 */
	public B source(ArtifactSource source) {
		this.source = source;
		return myself();
	}

	/**
	 * Specify the timestamp when this entry's metadata was captured.
	 *
	 * @param resolvedAt the manifest entry resolution timestamp
	 * @return manifest entry builder
	 */
	public B resolvedAt(Instant resolvedAt) {
		this.resolvedAt = resolvedAt;
		return myself();
	}

	@Override
	protected void validate() {
		super.validate();

		if (checksum == null || checksum.isBlank()) {
			throw new IllegalArgumentException("Manifest entry checksum can not be blank");
		}
		if (source == null) {
			throw new IllegalArgumentException("Manifest entry source can not be null");
		}
		if (resolvedAt == null) {
			throw new IllegalArgumentException("Manifest entry resolution date can not be null");
		}
	}

}
