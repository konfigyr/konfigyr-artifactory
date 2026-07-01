package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
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
		@NonNull String groupId,
		@NonNull String artifactId,
		@NonNull String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		@NonNull String checksum,
		@NonNull ArtifactSource source,
		@NonNull Instant resolvedAt
) implements ManifestEntry {

	@Serial
	private static final long serialVersionUID = 8548427370636592022L;

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
		@NonNull
		@Override
		public DefaultManifestEntry build() {
			if (groupId == null || groupId.isBlank()) {
				throw new IllegalArgumentException("Artifact groupId can not be blank");
			}
			if (artifactId == null || artifactId.isBlank()) {
				throw new IllegalArgumentException("Artifact artifactId can not be blank");
			}
			if (version == null || version.isBlank()) {
				throw new IllegalArgumentException("Artifact version can not be blank");
			}
			if (checksum == null || checksum.isBlank()) {
				throw new IllegalArgumentException("Manifest entry checksum can not be blank");
			}
			if (source == null) {
				throw new IllegalArgumentException("Manifest entry source can not be null");
			}
			if (resolvedAt == null) {
				throw new IllegalArgumentException("Manifest entry resolution date can not be null");
			}

			return new DefaultManifestEntry(groupId, artifactId, version, name, description, website, repository,
					checksum, source, resolvedAt);
		}

	}

}
