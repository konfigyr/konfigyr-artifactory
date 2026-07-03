package com.konfigyr.artifactory;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Represents a version change that occurred on the {@link Artifact}.
 *
 * @author : Vladimir Spasic
 * @since 1.0.0
 **/
public interface Publication extends Artifact, Serializable {

	/**
	 * Creates a new instance of the {@link PublicationBuilder} used to create a
	 * new instance of the default {@link Publication} implementation using the fluent builder API.
	 *
	 * @return default publication builder, never {@literal null}
	 */
	static PublicationBuilder<? extends Publication, ?> builder() {
		return new DefaultPublication.Builder();
	}

	/**
	 * The state of the {@link Publication} that indicates if it is production ready.
	 *
	 * @return publication state, never {@literal null}.
	 */
	PublicationState state();

	/**
	 * List containing a list of error messages that caused this publication to fail. The
	 * maintainers of this {@link Artifact} should address these issues and perform the
	 * publication again.
	 * <p>
	 * Should not be empty when the {@link Publication} is in the {@link PublicationState#FAILED}
	 * state.
	 *
	 * @return error messages, never {@literal null}.
	 */
	List<String> errors();

	/**
	 * Checksum of the {@link ArtifactMetadata artifact metadata} that was calculated by the
	 * build plugin on the {@code Artifactory} when this {@link Publication} was created.
	 * <p>
	 * It is advised to use {@code SHA-256} or stronger checksum algorithm to generate the
	 * hash that would be used to verify the integrity of this publication.
	 *
	 * @return publication checksum of the property metadata, never {@literal null}.
	 */
	String checksum();

	/**
	 * Timestamp when this publication was created.
	 *
	 * @return publication date, never {@literal null}.
	 */
	Instant publishedAt();

}
