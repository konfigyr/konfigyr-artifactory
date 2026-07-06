package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;

/**
 * The default implementation of the {@link Artifact} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultArtifact(
		String groupId,
		String artifactId,
		String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository
) implements Artifact {

	@Serial
	private static final long serialVersionUID = 7475803035172028135L;

	/**
	 * Validates the Maven coordinates of this {@link Artifact}, mirroring the checks performed
	 * by {@link ArtifactBuilder#validate()}, so that the invariant holds regardless of whether
	 * this record is constructed via the {@link Builder} or directly.
	 */
	public DefaultArtifact {
		Asserts.notBlank(groupId, "Artifact groupId can not be blank");
		Asserts.notBlank(artifactId, "Artifact artifactId can not be blank");
		Asserts.notBlank(version, "Artifact version can not be blank");
	}

	/**
	 * Builder class used to create new instances of the {@link DefaultArtifact}.
	 */
	public static final class Builder extends ArtifactBuilder<DefaultArtifact, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultArtifact} as a result of this builder.
		 *
		 * @return artifact, never {@literal null}.
		 */
		@Override
		protected DefaultArtifact instantiate() {
			return new DefaultArtifact(groupId, artifactId, version, name, description, website, repository);
		}

	}

}
