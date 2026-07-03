package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Default implementation of the {@link Publication} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param state       the state of this publication, can't be {@literal null}.
 * @param errors      error messages that caused this publication to fail. May be {@code empty} but not {@literal null}.
 * @param checksum    checksum of the uploaded {@link ArtifactMetadata artifact metadata}, can't be {@literal null}.
 * @param publishedAt timestamp when this publication was crated, can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultPublication(
		String groupId,
		String artifactId,
		String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		PublicationState state,
		List<String> errors,
		String checksum,
		Instant publishedAt
) implements Publication {

	@Serial
	private static final long serialVersionUID = 8548427370636592022L;

	/**
	 * Validates this {@link Publication}, mirroring the checks performed by
	 * {@link ArtifactBuilder#validate()} and {@link PublicationBuilder#validate()}, so that the
	 * invariant holds regardless of whether this record is constructed via the {@link Builder} or
	 * directly.
	 */
	public DefaultPublication {
		Asserts.notBlank(groupId, "Artifact groupId can not be blank");
		Asserts.notBlank(artifactId, "Artifact artifactId can not be blank");
		Asserts.notBlank(version, "Artifact version can not be blank");
		Asserts.nonNull(state, "Publication state can not be null");
		Asserts.nonNull(errors, "Publication errors can not be null");
		Asserts.notBlank(checksum, "Publication property metadata checksum can not be blank");
		Asserts.nonNull(publishedAt, "Publication date can not be null");
	}

	/**
	 * Builder class used to create new instances of the {@link DefaultPublication}.
	 */
	public static final class Builder extends PublicationBuilder<DefaultPublication, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultPublication} as a result of this builder.
		 *
		 * @return artifact publication, never {@literal null}.
		 */
		@Override
		protected DefaultPublication instantiate() {
			return new DefaultPublication(groupId, artifactId, version, name, description, website, repository,
					state, List.copyOf(errors), checksum, publishedAt);
		}

	}

}
