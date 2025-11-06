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

	protected String id;
	protected String name;
	protected final List<Artifact> artifacts;
	protected Instant createdAt;

	protected ManifestBuilder() {
		this.artifacts = new ArrayList<>();
	}

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
