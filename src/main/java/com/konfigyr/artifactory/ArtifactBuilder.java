package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.net.URI;

/**
 * Abstract builder class used to create new instances of the {@link Artifact} that can be reused
 * for different artifact implementations.
 *
 * @param <T> the artifact implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see Artifact
 * @since : 23.10.25, Thu
 */
public abstract class ArtifactBuilder<T extends Artifact, B extends ArtifactBuilder<T, B>> {

	protected String groupId;
	protected String artifactId;
	protected String version;
	protected String name;
	protected String description;
	protected URI website;
	protected URI repository;

	protected ArtifactBuilder() {
	}

	@NonNull
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
	@NonNull
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
	@NonNull
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
	@NonNull
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
	@NonNull
	public B name(String name) {
		this.name = name;
		return myself();
	}

	/**
	 * Specify the textual description for this {@link Artifact}.
	 *
	 * @param description artifact description
	 * @return artifact builder
	 */
	@NonNull
	public B description(String description) {
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
	@NonNull
	public B website(String website) {
		return website(website == null ? null : URI.create(website));
	}

	/**
	 * Specify an external URL for documentation or homepage for this {@link Artifact}.
	 *
	 * @param website artifact website URL
	 * @return artifact builder
	 */
	@NonNull
	public B website(URI website) {
		this.website = website;
		return myself();
	}

	/**
	 * Specify the source control repository location for this {@link Artifact}.
	 *
	 * @param repository artifact repository URL
	 * @return artifact builder
	 */
	@NonNull
	public B repository(String repository) {
		return repository(repository == null ? null : URI.create(repository));
	}

	/**
	 * Specify the source control repository location for this {@link Artifact}.
	 *
	 * @param repository artifact repository URL
	 * @return artifact builder
	 */
	@NonNull
	public B repository(URI repository) {
		this.repository = repository;
		return myself();
	}

	/**
	 * Creates the {@link Artifact} as a result of this builder.
	 *
	 * @return the artifact instance, never {@literal null}.
	 */
	@NonNull
	public abstract T build();

}
