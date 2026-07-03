package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;
import java.time.Instant;

/**
 * Default implementation of the {@link ManifestEntry} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param checksum    checksum of the {@link ArtifactMetadata artifact metadata} captured for this entry, can't be {@literal null}.
 * @param source      where this entry's metadata originated from, can't be {@literal null}.
 * @param resolvedAt  timestamp when this entry's metadata was captured, can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultManifestEntry(
		String groupId,
		String artifactId,
		String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		String checksum,
		ArtifactSource source,
		Instant resolvedAt
) implements ManifestEntry {

	@Serial
	private static final long serialVersionUID = 8731625110585043324L;

	/**
	 * Validates this {@link ManifestEntry}, mirroring the checks performed by
	 * {@link ArtifactBuilder#validate()} and {@link ManifestEntryBuilder#validate()}, so that the
	 * invariant holds regardless of whether this record is constructed via the {@link Builder} or
	 * directly.
	 */
	public DefaultManifestEntry {
		Asserts.notBlank(groupId, "Artifact groupId can not be blank");
		Asserts.notBlank(artifactId, "Artifact artifactId can not be blank");
		Asserts.notBlank(version, "Artifact version can not be blank");
		Asserts.notBlank(checksum, "Manifest entry checksum can not be blank");
		Asserts.nonNull(source, "Manifest entry source can not be null");
		Asserts.nonNull(resolvedAt, "Manifest entry resolution date can not be null");
	}

	/**
	 * Builder class used to create new instances of the {@link DefaultManifestEntry}.
	 */
	public static final class Builder extends ManifestEntryBuilder<DefaultManifestEntry, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultManifestEntry} as a result of this builder.
		 *
		 * @return manifest entry, never {@literal null}.
		 */
		@Override
		protected DefaultManifestEntry instantiate() {
			return new DefaultManifestEntry(groupId, artifactId, version, name, description, website, repository,
					checksum, source, resolvedAt);
		}

	}

}
