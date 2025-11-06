package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serial;
import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * The default implementation of the {@link ArtifactMetadata} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param checksum    checksum identifying this specific metadata, may be {@literal null}.
 * @param properties  property definitions for the artifact, can't {@literal null}
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultArtifactMetadata(
		String groupId,
		String artifactId,
		String version,
		String name,
		String description,
		URI website,
		URI repository,
		String checksum,
		List<PropertyDescriptor> properties
) implements ArtifactMetadata {

	@Serial
	private static final long serialVersionUID = 5969189079506443729L;

	/**
	 * Builder class used to create new instances of the {@link DefaultArtifactMetadata}.
	 */
	public static final class Builder extends ArtifactMetadataBuilder<DefaultArtifactMetadata, Builder> {

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
		public DefaultArtifactMetadata build() {
			if (groupId == null || groupId.isBlank()) {
				throw new IllegalArgumentException("Artifact groupId can not be blank");
			}
			if (artifactId == null || artifactId.isBlank()) {
				throw new IllegalArgumentException("Artifact artifactId can not be blank");
			}
			if (version == null || version.isBlank()) {
				throw new IllegalArgumentException("Artifact version can not be blank");
			}
			if (properties.isEmpty()) {
				throw new IllegalArgumentException("Artifact metadata must contain at least one property descriptor");
			}

			properties.sort(PropertyDescriptor::compareTo);

			return new DefaultArtifactMetadata(groupId, artifactId, version, name, description,
					website, repository, checksum, Collections.unmodifiableList(properties));
		}

	}

}
