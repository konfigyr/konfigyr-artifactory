package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link ServiceRelease} interface.
 *
 * @param id          the unique identifier of this release attempt, can't be {@literal null}.
 * @param state       the state of this release attempt, can't be {@literal null}.
 * @param artifacts   per-artifact resolution results captured for this release, can't be
 *                    {@literal null} but may be empty.
 * @param publishedAt timestamp when this release was committed as the service's current manifest, may
 *                    be {@literal null}.
 * @param errors      error messages that caused this release to fail, can't be {@literal null} but may
 *                    be empty.
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public record DefaultServiceRelease(
		@NonNull String id,
		@NonNull ReleaseState state,
		@NonNull List<ServiceReleaseEntry> artifacts,
		@Nullable Instant publishedAt,
		@NonNull List<String> errors
) implements ServiceRelease {

	@Serial
	private static final long serialVersionUID = 4267158390215730881L;

	/**
	 * Builder class used to create new instances of the {@link DefaultServiceRelease}.
	 */
	public static final class Builder extends ServiceReleaseBuilder<DefaultServiceRelease, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultServiceRelease} as a result of this builder.
		 *
		 * @return service release, never {@literal null}.
		 */
		@NonNull
		@Override
		public DefaultServiceRelease build() {
			if (id == null || id.isBlank()) {
				throw new IllegalArgumentException("Service release identifier can not be blank");
			}
			if (state == null) {
				state = ReleaseState.PENDING;
			}

			return new DefaultServiceRelease(id, state, Collections.unmodifiableList(artifacts), publishedAt,
					Collections.unmodifiableList(errors));
		}

	}

}
