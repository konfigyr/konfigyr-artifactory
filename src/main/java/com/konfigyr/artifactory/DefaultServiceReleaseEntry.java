package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.net.URI;

/**
 * Default implementation of the {@link ServiceReleaseEntry} interface.
 *
 * @param groupId     Maven coordinate {@code groupId} of the artifact, can't be {@literal null}.
 * @param artifactId  Maven coordinate {@code artifactId} of the artifact, can't be {@literal null}.
 * @param version     Maven coordinate {@code version} of the artifact, can't be {@literal null}.
 * @param name        human-readable name of the artifact, may be {@literal null}.
 * @param description textual description of the artifact, may be {@literal null}.
 * @param website     external URL for documentation or homepage, may be {@literal null}.
 * @param repository  source control repository reference (SCM URL), may be {@literal null}.
 * @param status      whether the build plugin still needs to upload this artifact's metadata, can't be
 *                    {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultServiceReleaseEntry(
		String groupId,
		String artifactId,
		String version,
		@Nullable String name,
		@Nullable String description,
		@Nullable URI website,
		@Nullable URI repository,
		ArtifactUploadStatus status
) implements ServiceReleaseEntry {

	@Serial
	private static final long serialVersionUID = 5502981736441827334L;

	/**
	 * Validates this {@link ServiceReleaseEntry}, mirroring the checks performed by
	 * {@link ArtifactBuilder#validate()} and {@link ServiceReleaseEntryBuilder#validate()}, so that
	 * the invariant holds regardless of whether this record is constructed via the {@link Builder} or
	 * directly.
	 */
	public DefaultServiceReleaseEntry {
		if (groupId == null || groupId.isBlank()) {
			throw new IllegalArgumentException("Artifact groupId can not be blank");
		}
		if (artifactId == null || artifactId.isBlank()) {
			throw new IllegalArgumentException("Artifact artifactId can not be blank");
		}
		if (version == null || version.isBlank()) {
			throw new IllegalArgumentException("Artifact version can not be blank");
		}
		if (status == null) {
			throw new IllegalArgumentException("Artifact upload status can not be null");
		}
	}

	/**
	 * Builder class used to create new instances of the {@link DefaultServiceReleaseEntry}.
	 */
	public static final class Builder extends ServiceReleaseEntryBuilder<DefaultServiceReleaseEntry, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultServiceReleaseEntry} as a result of this builder.
		 *
		 * @return service release entry, never {@literal null}.
		 */
		@Override
		protected DefaultServiceReleaseEntry instantiate() {
			return new DefaultServiceReleaseEntry(groupId, artifactId, version, name, description, website,
					repository, status);
		}

	}

}
