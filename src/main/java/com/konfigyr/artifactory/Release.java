package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Represents a version change that occurred on the {@link Artifact}.
 *
 * @author : Vladimir Spasic
 * @since : 22.09.23, Fri
 **/
public interface Release extends Artifact, Serializable {

	/**
	 * Creates a new instance of the {@link DefaultRelease.Builder} used to create a
	 * new instance of the {@link DefaultRelease} using the fluent builder API.
	 *
	 * @return default release builder, never {@literal null}
	 */
	static DefaultRelease.Builder builder() {
		return new DefaultRelease.Builder();
	}

	/**
	 * The state of the {@link Release} that indicates if it is production ready.
	 *
	 * @return release state, never {@literal null}.
	 */
	@NonNull
	ReleaseState state();

	/**
	 * List containing a list of error messages that caused this release to fail. The
	 * maintainers of this {@link Artifact} should address these issues and perform the
	 * release again.
	 * <p>
	 * Should not be empty when the {@link Release} is in the {@link ReleaseState#FAILED}
	 * state.
	 *
	 * @return error messages, never {@literal null}.
	 */
	@NonNull
	List<String> errors();

	/**
	 * Checksum of the {@link ArtifactMetadata artifact metadata} that was calculated by the
	 * build plugin on the {@code Artifactory} when this {@link Release} was created.
	 * <p>
	 * It is advised to use {@code SHA-256} or stronger checksum algorithm to generate the
	 * hash that would be used to verify the integrity of this release.
	 *
	 * @return release checksum of the property metadata, never {@literal null}.
	 */
	@NonNull
	String checksum();

	/**
	 * Timestamp when this release was created.
	 *
	 * @return release date, never {@literal null}.
	 */
	@NonNull
	Instant releaseDate();

}
