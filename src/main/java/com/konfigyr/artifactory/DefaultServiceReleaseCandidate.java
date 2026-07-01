package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;

/**
 * Default implementation of the {@link ServiceReleaseCandidate} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param checksum    checksum of the {@link ArtifactMetadata} resolved locally for this candidate,
 *                    can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultServiceReleaseCandidate(
		@NonNull String groupId,
		@NonNull String artifactId,
		@NonNull String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		@NonNull String checksum
) implements ServiceReleaseCandidate {

	@Serial
	private static final long serialVersionUID = 3921604587123049110L;

	/**
	 * Builder class used to create new instances of the {@link DefaultServiceReleaseCandidate}.
	 */
	public static final class Builder extends ServiceReleaseCandidateBuilder<DefaultServiceReleaseCandidate, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultServiceReleaseCandidate} as a result of this builder.
		 *
		 * @return service release candidate, never {@literal null}.
		 */
		@NonNull
		@Override
		public DefaultServiceReleaseCandidate build() {
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
				throw new IllegalArgumentException("Service release candidate checksum can not be blank");
			}

			return new DefaultServiceReleaseCandidate(groupId, artifactId, version, name, description, website,
					repository, checksum);
		}

	}

}
