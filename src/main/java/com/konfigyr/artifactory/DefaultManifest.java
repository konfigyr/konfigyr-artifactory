package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link Manifest} interface.
 *
 * @param id        the unique identifier of the {@code Service} this manifest belongs to, can't be {@literal null}.
 * @param name      the name of the {@code Service} this manifest belongs to, can't be {@literal null}.
 * @param artifacts list of artifacts that belong to this manifest, can't be {@literal null}.
 * @param createdAt timestamp when this manifest was created, can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultManifest(
		@NonNull String id,
		@NonNull String name,
		@NonNull List<Artifact> artifacts,
		@NonNull Instant createdAt
) implements Manifest {

	/**
	 * Builder class used to create new instances of the {@link DefaultManifest}.
	 */
	public static final class Builder extends ManifestBuilder<DefaultManifest, DefaultManifest.Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultManifest} as a result of this builder.
		 *
		 * @return service artifact manifest, never {@literal null}.
		 */
		@NonNull
		@Override
		public DefaultManifest build() {
			if (id == null || id.isBlank()) {
				throw new IllegalArgumentException("Service identifier can not be blank");
			}
			if (name == null || name.isBlank()) {
				throw new IllegalArgumentException("Service name can not be blank");
			}
			if (createdAt == null) {
				createdAt = Instant.now();
			}

			artifacts.sort(Artifact::compareTo);

			return new DefaultManifest(id, name, Collections.unmodifiableList(artifacts), createdAt);
		}

	}

}
