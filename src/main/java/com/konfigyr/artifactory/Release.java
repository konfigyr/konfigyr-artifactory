package com.konfigyr.artifactory;

import com.github.zafarkhaja.semver.Version;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Represents a version change that occurred on the {@link Artifact}.
 *
 * @author : Vladimir Spasic
 * @since : 22.09.23, Fri
 **/
public interface Release extends Comparable<Release>, Serializable {

	/**
	 * The unique {@link Artifact} identifier to which this {@link Release} belongs to.
	 * @return artifact identifier, never {@literal null}.
	 * @see Artifact#getIdentifier()
	 */
	@Nonnull
	String getArtifact();

	/**
	 * Version that identifies this {@link Release}.
	 * @return release version, never {@literal null}.
	 */
	@Nonnull
	Version getVersion();

	/**
	 * As one {@link Artifact} can have multiple {@link Release releases} it is useful to
	 * specify which of the versions should be considered as latest.
	 * <p>
	 * Konfigyr Artifactory uses this attribute to retrieve the {@link PropertyDescriptor
	 * property metadata} of the {@link Artifact} when specific release version is not
	 * specified.
	 * @return {@code true} if this release is considered as latest, never
	 * {@literal null}.
	 */
	boolean isLatest();

	/**
	 * The state of the {@link Release} that indicates if it is production ready.
	 * @return release state, never {@literal null}.
	 */
	@Nonnull
	ReleaseState getState();

	/**
	 * List containing a list of error messages that caused this release to fail. The
	 * maintainers of this {@link Artifact} should address these issues and perform the
	 * release again.
	 * <p>
	 * Should not be empty when the {@link Release} is in the {@link ReleaseState#FAILED}
	 * state.
	 * @return error messages, never {@literal null}.
	 */
	@Nonnull
	List<String> getErrors();

	/**
	 * Checksum of the {@link PropertyDescriptor property metadata} that was part of the
	 * {@link Component} when this {@link Release} was created.
	 * <p>
	 * It is advised to use {@code SHA-256} or stronger checksum algorithm to generate the
	 * hash that would be used to verify the integrity of this release.
	 * @return release checksum of the property metadata, never {@literal null}.
	 */
	@Nonnull
	String getChecksum();

	/**
	 * Timestamp when this release was created.
	 * @return release date, never {@literal null}.
	 */
	@Nonnull
	Instant getReleaseDate();

	@Override
	default int compareTo(@Nonnull Release o) {
		return getVersion().compareTo(o.getVersion());
	}

}
