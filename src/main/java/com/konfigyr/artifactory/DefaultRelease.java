package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.List;


/**
 * Default implementation of the {@link Release} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param state       the state of this release, can't be {@literal null}.
 * @param errors      error messages that caused this release to fail, if any. May be {@code empty} but not {@literal null}.
 * @param checksum    checksum of the uploaded {@link ArtifactMetadata artifact metadata}, can't be {@literal null}.
 * @param releaseDate timestamp when this release was crated, can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultRelease(
		@NonNull String groupId,
		@NonNull String artifactId,
		@NonNull String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		@NonNull ReleaseState state,
		@NonNull List<String> errors,
		@NonNull String checksum,
		@NonNull Instant releaseDate
) implements Release {

	@Serial
	private static final long serialVersionUID = 8548427370636592022L;

	/**
	 * Builder class used to create new instances of the {@link DefaultRelease}.
	 */
	public static final class Builder extends ReleaseBuilder<DefaultRelease, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultRelease} as a result of this builder.
		 *
		 * @return artifact release, never {@literal null}.
		 */
		@NonNull
		@Override
		public DefaultRelease build() {
			if (groupId == null || groupId.isBlank()) {
				throw new IllegalArgumentException("Artifact groupId can not be blank");
			}
			if (artifactId == null || artifactId.isBlank()) {
				throw new IllegalArgumentException("Artifact artifactId can not be blank");
			}
			if (version == null || version.isBlank()) {
				throw new IllegalArgumentException("Artifact version can not be blank");
			}
			if (state == null) {
				state = ReleaseState.PENDING;
			}
			if (checksum == null || checksum.isBlank()) {
				throw new IllegalArgumentException("Release property metadata checksum can not be blank");
			}
			if (releaseDate == null) {
				throw new IllegalArgumentException("Release date can not be null");
			}

			return new DefaultRelease(groupId, artifactId, version, name, description, website, repository,
					state, Collections.unmodifiableList(errors), checksum, releaseDate);
		}

	}

}
