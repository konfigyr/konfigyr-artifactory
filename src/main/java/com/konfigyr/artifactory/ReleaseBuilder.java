package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder class used to create new instances of the {@link Release} that can be reused
 * for different implementation.
 *
 * @param <T> the release implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see Release
 * @since : 23.10.25, Thu
 */
public abstract class ReleaseBuilder<T extends Release, B extends ReleaseBuilder<T, B>> extends ArtifactBuilder<T, B> {

	protected ReleaseState state;
	protected String checksum;
	protected Instant releaseDate;
	protected final List<String> errors;

	protected ReleaseBuilder() {
		this.errors = new ArrayList<>();
	}

	@NonNull
	@SuppressWarnings("unchecked")
	protected B myself() {
		return (B) this;
	}

	/**
	 * Specify the {@link Artifact} for which this release is created.
	 *
	 * @param artifact the released artifact
	 * @return release builder
	 */
	@NonNull
	public B artifact(Artifact artifact) {
		return groupId(artifact.groupId())
				.artifactId(artifact.artifactId())
				.version(artifact.version())
				.name(artifact.name())
				.description(artifact.description())
				.website(artifact.website())
				.repository(artifact.repository());
	}

	/**
	 * Specify the state of this release.
	 *
	 * @param state the release state
	 * @return release builder
	 */
	@NonNull
	public B state(ReleaseState state) {
		this.state = state;
		return myself();
	}

	/**
	 * Adds a single release error message that caused this release to fail.
	 *
	 * @param error the release error message
	 * @return release builder
	 */
	@NonNull
	public B error(String error) {
		if (error != null && !error.isBlank()) {
			this.errors.add(error);
		}
		return myself();
	}

	/**
	 * Adds the release error message that caused this release to fail.
	 *
	 * @param errors the release error messages
	 * @return release builder
	 */
	@NonNull
	public B errors(Iterable<String> errors) {
		for (String error : errors) {
			error(error);
		}

		return myself();
	}

	/**
	 * Adds a checksum of the {@link PropertyDescriptor property metadata} that was generated
	 * when this release was created.
	 *
	 * @param checksum the metadata checksum
	 * @return release builder
	 */
	@NonNull
	public B checksum(String checksum) {
		this.checksum = checksum;
		return myself();
	}

	/**
	 * Specify the timestamp when this release was created.
	 *
	 * @param releaseDate the release date
	 * @return release builder
	 */
	@NonNull
	public B releaseDate(Instant releaseDate) {
		this.releaseDate = releaseDate;
		return myself();
	}

	/**
	 * Creates the {@link Release} as a result of this builder.
	 *
	 * @return the release instance, never {@literal null}.
	 */
	@NonNull
	public abstract T build();

}
