package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating immutable {@link Manifest} instances that can be reused for different implementations.
 * <p>
 * Implementations should provide fluent methods for constructing manifests, ensuring immutability and null-safety
 * for all required fields.
 *
 * @param <T> the manifest implementation that this builder should build
 * @param <B> the builder generic type
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public abstract class ManifestBuilder<T extends Manifest, B extends ManifestBuilder<T, B>> {

	/**
	 * The unique identifier of the service manifest.
	 */
	protected String id;

	/**
	 * The name of the service manifest.
	 */
	protected String name;

	/**
	 * Collection of artifacts that are part of the manifest.
	 */
	protected final List<Artifact> artifacts;

	/**
	 * Timestamp marking the creation of the manifest.
	 */
	protected Instant createdAt;

	/**
	 * Creates a new {@link ManifestBuilder} instance.
	 */
	protected ManifestBuilder() {
		this.artifacts = new ArrayList<>();
	}

	/**
	 * Utility method that returns the type-self builder return value that is used
	 * to chain builder methods.
	 *
	 * @return the type-self builder return value, never {@literal null}.
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	protected B myself() {
		return (B) this;
	}

	/**
	 * Sets the service identifier for this manifest.
	 *
	 * @param id the service identifier.
	 * @return this builder instance.
	 */
	@NonNull
	public B id(String id) {
		this.id = id;
		return myself();
	}

	/**
	 * Sets the service name for this manifest.
	 *
	 * @param name the service name.
	 * @return this builder instance.
	 */
	@NonNull
	public B name(String name) {
		this.name = name;
		return myself();
	}

	/**
	 * Sets the manifest creation timestamp.
	 *
	 * @param createdAt creation timestamp.
	 * @return this builder instance.
	 */
	@NonNull
	public B createdAt(Instant createdAt) {
		this.createdAt = createdAt;
		return myself();
	}

	/**
	 * Adds a single artifact entry to the manifest.
	 *
	 * @param artifact a manifest artifact entry.
	 * @return this builder instance.
	 */
	@NonNull
	public B artifact(Artifact artifact) {
		if (artifact != null) {
			this.artifacts.add(artifact);
		}
		return myself();
	}

	/**
	 * Adds multiple artifact entries to the manifest.
	 *
	 * @param artifacts list of manifest artifact entries.
	 * @return this builder instance.
	 */
	@NonNull
	public B artifacts(Iterable<? extends Artifact> artifacts) {
		if (artifacts != null) {
			for (Artifact artifact : artifacts) {
				artifact(artifact);
			}
		}
		return myself();
	}

	/**
	 * Builds a new immutable {@link Manifest} instance.
	 *
	 * @return a fully initialized manifest, never {@literal null}.
	 */
	@NonNull
	abstract T build();

}
