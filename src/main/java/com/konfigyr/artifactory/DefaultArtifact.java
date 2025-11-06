package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
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
		@NonNull String groupId,
		@NonNull String artifactId,
		@NonNull String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository
) implements Artifact {

	@Serial
	private static final long serialVersionUID = 8548427370636592022L;

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
		@NonNull
		@Override
		public DefaultArtifact build() {
			if (groupId == null || groupId.isBlank()) {
				throw new IllegalArgumentException("Artifact groupId can not be blank");
			}
			if (artifactId == null || artifactId.isBlank()) {
				throw new IllegalArgumentException("Artifact artifactId can not be blank");
			}
			if (version == null || version.isBlank()) {
				throw new IllegalArgumentException("Artifact version can not be blank");
			}

			return new DefaultArtifact(groupId, artifactId, version, name, description, website, repository);
		}

	}

}
