package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Reports what happened the last time a service attempted to publish its {@link Manifest}: a process
 * report for a single service publish build.
 * <p>
 * {@code ServiceRelease} is deliberately kept separate from {@link Manifest} rather than folded into
 * it as extra fields. {@link Manifest} answers "what does this service currently publish", a content
 * snapshot. {@code ServiceRelease} answers "what happened the last time someone tried to publish it",
 * a transient process report:
 * <ul>
 *     <li>{@link ReleaseState#PENDING} while artifacts are being resolved/uploaded</li>
 *     <li>{@link ReleaseState#RELEASED} once committed</li>
 *     <li>{@link ReleaseState#FAILED} if something declared was never uploaded</li>
 * </ul>
 * <p>
 * Bolting the {@code state()}/{@code publishedAt()}/{@code errors()} directly onto {@link Manifest}
 * would make it behave like a process-tracking type it isn't. Keeping the two separate means {@link Manifest}
 * stays a pure content view and {@code ServiceRelease} stays a pure process report, and neither has to lie
 * about what it represents.
 *
 * @author Vladimir Spasic
 * @see ReleaseState
 * @see ServiceReleaseEntry
 * @see Manifest
 * @since 1.0.0
 */
public interface ServiceRelease extends Serializable {

	/**
	 * Creates a new instance of the {@link ServiceReleaseBuilder} used to create a new instance
	 * of the default {@link ServiceRelease} implementation using the fluent builder API.
	 *
	 * @return default service release builder, never {@literal null}
	 */
	static ServiceReleaseBuilder<? extends ServiceRelease, ?> builder() {
		return new DefaultServiceRelease.Builder();
	}

	/**
	 * The unique identifier of this release attempt.
	 *
	 * @return service release identifier, never {@literal null}.
	 */
	@NonNull
	String id();

	/**
	 * The state of this release attempt.
	 *
	 * @return service release state, never {@literal null}.
	 */
	@NonNull
	ReleaseState state();

	/**
	 * Per-artifact resolution results captured for this release. Typically populated while resolving
	 * the release and empty once the release has been published, since the build plugin already knows
	 * what it uploaded.
	 *
	 * @return service release entries, never {@literal null} but may be empty.
	 */
	@NonNull
	List<ServiceReleaseEntry> artifacts();

	/**
	 * Timestamp when this release was committed as the service's current {@link Manifest}.
	 *
	 * @return service release publish timestamp, {@literal null} while {@link #state()} is not
	 * {@link ReleaseState#RELEASED}.
	 */
	@Nullable
	Instant publishedAt();

	/**
	 * List containing a list of error messages that caused this release to fail.
	 * <p>
	 * Should not be empty when this {@link ServiceRelease} is in the {@link ReleaseState#FAILED}
	 * state.
	 *
	 * @return error messages, never {@literal null}.
	 */
	@NonNull
	List<String> errors();

}
