package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder class used to create new instances of the {@link ServiceRelease} that can be reused
 * for different implementations.
 *
 * @param <T> the service release implementation that this builder should build
 * @param <B> the builder generic type
 * @author Vladimir Spasic
 * @see ServiceRelease
 * @since 1.0.0
 */
public abstract class ServiceReleaseBuilder<T extends ServiceRelease, B extends ServiceReleaseBuilder<T, B>> {

	/**
	 * The unique identifier of this release attempt.
	 */
	protected @Nullable String id;

	/**
	 * The state of this release attempt. Optional: defaults to {@link ReleaseState#PENDING} when
	 * left unset.
	 */
	protected @Nullable ReleaseState state;

	/**
	 * Collection of per-artifact resolution results captured for this release.
	 */
	protected final List<ServiceReleaseEntry> artifacts;

	/**
	 * Timestamp when this release was committed as the service's current manifest.
	 */
	protected @Nullable Instant publishedAt;

	/**
	 * Collection of release errors that caused this release to fail.
	 */
	protected final List<String> errors;

	/**
	 * Creates a new {@link ServiceReleaseBuilder} instance.
	 */
	protected ServiceReleaseBuilder() {
		this.artifacts = new ArrayList<>();
		this.errors = new ArrayList<>();
	}

	/**
	 * Utility method that returns the type-self builder return value that is used
	 * to chain builder methods.
	 *
	 * @return the type-self builder return value, never {@literal null}.
	 */
	@SuppressWarnings("unchecked")
	protected B myself() {
		return (B) this;
	}

	/**
	 * Specify the unique identifier of this release attempt.
	 *
	 * @param id the service release identifier
	 * @return service release builder
	 */
	public B id(String id) {
		this.id = id;
		return myself();
	}

	/**
	 * Specify the state of this release attempt.
	 *
	 * @param state the service release state
	 * @return service release builder
	 */
	public B state(@Nullable ReleaseState state) {
		this.state = state;
		return myself();
	}

	/**
	 * Adds a single per-artifact resolution result to this release.
	 *
	 * @param entry a service release entry
	 * @return service release builder
	 */
	public B artifact(@Nullable ServiceReleaseEntry entry) {
		if (entry != null) {
			this.artifacts.add(entry);
		}
		return myself();
	}

	/**
	 * Adds multiple per-artifact resolution results to this release.
	 *
	 * @param entries service release entries
	 * @return service release builder
	 */
	public B artifacts(@Nullable Iterable<? extends ServiceReleaseEntry> entries) {
		if (entries != null) {
			for (ServiceReleaseEntry entry : entries) {
				artifact(entry);
			}
		}
		return myself();
	}

	/**
	 * Specify the timestamp when this release was committed as the service's current manifest.
	 *
	 * @param publishedAt the service release publish timestamp
	 * @return service release builder
	 */
	public B publishedAt(@Nullable Instant publishedAt) {
		this.publishedAt = publishedAt;
		return myself();
	}

	/**
	 * Adds a single release error message that caused this release to fail.
	 *
	 * @param error the release error message
	 * @return service release builder
	 */
	public B error(@Nullable String error) {
		if (error != null && !error.isBlank()) {
			this.errors.add(error);
		}
		return myself();
	}

	/**
	 * Adds the release error messages that caused this release to fail.
	 *
	 * @param errors the release error messages
	 * @return service release builder
	 */
	public B errors(@Nullable Iterable<String> errors) {
		if (errors != null) {
			for (String error : errors) {
				error(error);
			}
		}
		return myself();
	}

	/**
	 * Validates the properties collected by this builder, throwing an {@link IllegalArgumentException}
	 * when a required property is missing or invalid, and applying defaults for optional properties
	 * that were left unset.
	 *
	 * @throws IllegalArgumentException if the {@code id} collected by this builder is missing or blank.
	 */
	protected void validate() {
		Asserts.notBlank(id, "Service release identifier can not be blank");
		if (state == null) {
			state = ReleaseState.PENDING;
		}
	}

	/**
	 * Creates the {@link ServiceRelease} instance using the properties collected by this builder.
	 * <p>
	 * Called by {@link #build()} only once {@link #validate()} has completed without throwing.
	 *
	 * @return the service release instance, never {@literal null}.
	 */
	protected abstract T instantiate();

	/**
	 * Validates the properties collected by this builder and creates the {@link ServiceRelease} as a
	 * result of this builder.
	 *
	 * @return the service release instance, never {@literal null}.
	 * @throws IllegalArgumentException if a required property collected by this builder is
	 *                                  missing or invalid, see {@link #validate()}.
	 */
	public final T build() {
		validate();
		return instantiate();
	}

}
