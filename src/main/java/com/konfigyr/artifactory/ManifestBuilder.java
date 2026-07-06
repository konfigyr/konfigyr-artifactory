package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

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
	protected @Nullable String id;

	/**
	 * The name of the service manifest.
	 */
	protected @Nullable String name;

	/**
	 * Collection of manifest entries that are part of the manifest.
	 */
	protected final List<ManifestEntry> artifacts;

	/**
	 * Timestamp marking the creation of the manifest.
	 */
	protected @Nullable Instant createdAt;

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
	public B createdAt(@Nullable Instant createdAt) {
		this.createdAt = createdAt;
		return myself();
	}

	/**
	 * Adds a single manifest entry to the manifest.
	 *
	 * @param entry a manifest entry.
	 * @return this builder instance.
	 */
	public B artifact(@Nullable ManifestEntry entry) {
		if (entry != null) {
			this.artifacts.add(entry);
		}
		return myself();
	}

	/**
	 * Adds multiple manifest entries to the manifest.
	 *
	 * @param entries list of manifest entries.
	 * @return this builder instance.
	 */
	public B artifacts(@Nullable Iterable<? extends ManifestEntry> entries) {
		if (entries != null) {
			for (ManifestEntry entry : entries) {
				artifact(entry);
			}
		}
		return myself();
	}

	/**
	 * Validates the properties collected by this builder, throwing an {@link IllegalArgumentException}
	 * when a required property is missing or invalid, and applying defaults for optional properties
	 * that were left unset.
	 */
	protected void validate() {
		Asserts.notBlank(id, "Service identifier can not be blank");
		Asserts.notBlank(name, "Service name can not be blank");

		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	/**
	 * Creates the {@link Manifest} instance using the properties collected by this builder.
	 * <p>
	 * Called by {@link #build()} only once {@link #validate()} has completed without throwing.
	 *
	 * @return the manifest instance, never {@literal null}.
	 */
	protected abstract T instantiate();

	/**
	 * Validates the properties collected by this builder and builds a new immutable {@link Manifest}
	 * instance.
	 *
	 * @return a fully initialized manifest, never {@literal null}.
	 */
	public final T build() {
		validate();
		return instantiate();
	}

}
