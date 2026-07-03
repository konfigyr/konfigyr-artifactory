package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.net.URI;

/**
 * Abstract builder class used to create new instances of the {@link Artifact} that can be reused
 * for different artifact implementations.
 *
 * @param <T> the artifact implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see Artifact
 * @since 1.0.0
 */
public abstract class ArtifactBuilder<T extends Artifact, B extends ArtifactBuilder<T, B>> {

	/**
	 * The {@code groupId} coordinate for this {@link Artifact}.
	 */
	protected @Nullable String groupId;

	/**
	 * The {@code artifactId} coordinate for this {@link Artifact}.
	 */
	protected @Nullable String artifactId;

	/**
	 * The {@code version} coordinate for this {@link Artifact}.
	 */
	protected @Nullable String version;

	/**
	 * The human-readable name for this {@link Artifact}.
	 */
	protected @Nullable String name;

	/**
	 * The textual description for this {@link Artifact}.
	 */
	protected @Nullable String description;

	/**
	 * The external URL for documentation or homepage for this {@link Artifact}.
	 */
	protected @Nullable URI website;

	/**
	 * The source control repository location for this {@link Artifact}.
	 */
	protected @Nullable URI repository;

	/**
	 * Creates a new instance of the {@link ArtifactBuilder}.
	 */
	protected ArtifactBuilder() {
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
	 * Specify the {@code groupId} coordinate for this {@link Artifact}.
	 *
	 * @param groupId artifact {@code groupId} coordinate
	 * @return artifact builder
	 */
	public B groupId(String groupId) {
		this.groupId = groupId;
		return myself();
	}

	/**
	 * Specify the {@code artifactId} coordinate for this {@link Artifact}.
	 *
	 * @param artifactId artifact {@code artifactId} coordinate
	 * @return artifact builder
	 */
	public B artifactId(String artifactId) {
		this.artifactId = artifactId;
		return myself();
	}

	/**
	 * Specify the {@code version} coordinate for this {@link Artifact}.
	 *
	 * @param version artifact {@code version} coordinate
	 * @return artifact builder
	 */
	public B version(String version) {
		this.version = version;
		return myself();
	}

	/**
	 * Specify the human-readable name for this {@link Artifact}.
	 *
	 * @param name artifact name
	 * @return artifact builder
	 */
	public B name(@Nullable String name) {
		this.name = name;
		return myself();
	}

	/**
	 * Specify the textual description for this {@link Artifact}.
	 *
	 * @param description artifact description
	 * @return artifact builder
	 */
	public B description(@Nullable String description) {
		this.description = description;
		return myself();
	}

	/**
	 * Specify an external URL for documentation or homepage for this {@link Artifact}.
	 *
	 * @param website artifact website URL
	 * @return artifact builder
	 * @throws IllegalArgumentException If the given website location violates RFC 2396
	 */
	public B website(@Nullable String website) {
		return website(website == null ? null : URI.create(website));
	}

	/**
	 * Specify an external URL for documentation or homepage for this {@link Artifact}.
	 *
	 * @param website artifact website URL
	 * @return artifact builder
	 */
	public B website(@Nullable URI website) {
		this.website = website;
		return myself();
	}

	/**
	 * Specify the source control repository location for this {@link Artifact}.
	 *
	 * @param repository artifact repository URL
	 * @return artifact builder
	 */
	public B repository(@Nullable String repository) {
		return repository(repository == null ? null : URI.create(repository));
	}

	/**
	 * Specify the source control repository location for this {@link Artifact}.
	 *
	 * @param repository artifact repository URL
	 * @return artifact builder
	 */
	public B repository(@Nullable URI repository) {
		this.repository = repository;
		return myself();
	}

	/**
	 * Validates the properties collected by this builder, throwing an {@link IllegalArgumentException}
	 * when a required property is missing or invalid.
	 * <p>
	 * Subclasses that introduce additional required properties should override this method, calling
	 * {@code super.validate()} first so that the coordinate validation performed here still applies.
	 */
	protected void validate() {
		if (groupId == null || groupId.isBlank()) {
			throw new IllegalArgumentException("Artifact groupId can not be blank");
		}
		if (artifactId == null || artifactId.isBlank()) {
			throw new IllegalArgumentException("Artifact artifactId can not be blank");
		}
		if (version == null || version.isBlank()) {
			throw new IllegalArgumentException("Artifact version can not be blank");
		}
	}

	/**
	 * Creates the {@link Artifact} instance using the properties collected by this builder.
	 * <p>
	 * Called by {@link #build()} only once {@link #validate()} has completed without throwing.
	 *
	 * @return the artifact instance, never {@literal null}.
	 */
	protected abstract T instantiate();

	/**
	 * Validates the properties collected by this builder and creates the {@link Artifact} as a
	 * result of this builder.
	 *
	 * @return the artifact instance, never {@literal null}.
	 */
	public final T build() {
		validate();
		return instantiate();
	}

}
