package com.konfigyr.artifactory;

import java.io.Serial;
import java.time.Instant;
import java.util.List;

/**
 * Default implementation of the {@link Manifest} interface.
 *
 * @param id        the unique identifier of the {@code Service} this manifest belongs to, can't be {@literal null}.
 * @param name      the name of the {@code Service} this manifest belongs to, can't be {@literal null}.
 * @param artifacts list of manifest entries that belong to this manifest, can't be {@literal null}.
 * @param createdAt timestamp when this manifest was created, can't be {@literal null}.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultManifest(
		String id,
		String name,
		List<ManifestEntry> artifacts,
		Instant createdAt
) implements Manifest {

	@Serial
	private static final long serialVersionUID = 9038022062672153532L;

	/**
	 * Validates this {@link Manifest}, mirroring the checks performed by {@link ManifestBuilder#validate()},
	 * so that the invariant holds regardless of whether this record is constructed via the {@link Builder}
	 * or directly.
	 */
	public DefaultManifest {
		id = Asserts.notBlank(id, "Service identifier can not be blank");
		name = Asserts.notBlank(name, "Service name can not be blank");
	}

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
		@Override
		protected DefaultManifest instantiate() {
			artifacts.sort(Artifact::compareTo);

			return new DefaultManifest(id, name, List.copyOf(artifacts), createdAt);
		}

	}

}
