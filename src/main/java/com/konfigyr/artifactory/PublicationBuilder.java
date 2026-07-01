package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder class used to create new instances of the {@link Publication} that can be reused
 * for different implementation.
 *
 * @param <T> the publication implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see Publication
 * @since 1.0.0
 */
public abstract class PublicationBuilder<T extends Publication, B extends PublicationBuilder<T, B>> extends ArtifactBuilder<T, B> {

	/**
	 * The state of the publication.
	 */
	protected PublicationState state;

	/**
	 * Checksum of the {@link PropertyDescriptor property metadata} that was published.
	 */
	protected String checksum;

	/**
	 * Timestamp when this publication was created.
	 */
	protected Instant publishedAt;

	/**
	 * Collection of publication errors that caused this publication to fail.
	 */
	protected final List<String> errors;

	/**
	 * Creates a new {@link PublicationBuilder} instance.
	 */
	protected PublicationBuilder() {
		this.errors = new ArrayList<>();
	}

	@NonNull
	@SuppressWarnings("unchecked")
	protected B myself() {
		return (B) this;
	}

	/**
	 * Specify the {@link Artifact} for which this publication is created.
	 *
	 * @param artifact the published artifact
	 * @return publication builder
	 */
	@NonNull
	public B artifact(Artifact artifact) {
		return groupId(artifact.groupId())
				.artifactId(artifact.artifactId())
				.version(artifact.version())
				.name(artifact.name())
				.description(artifact.description())
				.website(artifact.website())
				.repository(artifact.repository());
	}

	/**
	 * Specify the state of this publication.
	 *
	 * @param state the publication state
	 * @return publication builder
	 */
	@NonNull
	public B state(PublicationState state) {
		this.state = state;
		return myself();
	}

	/**
	 * Adds a single publication error message that caused this publication to fail.
	 *
	 * @param error the publication error message
	 * @return publication builder
	 */
	@NonNull
	public B error(String error) {
		if (error != null && !error.isBlank()) {
			this.errors.add(error);
		}
		return myself();
	}

	/**
	 * Adds the publication error message that caused this publication to fail.
	 *
	 * @param errors the publication error messages
	 * @return publication builder
	 */
	@NonNull
	public B errors(Iterable<String> errors) {
		for (String error : errors) {
			error(error);
		}

		return myself();
	}

	/**
	 * Adds a checksum of the {@link PropertyDescriptor property metadata} that was generated
	 * when this publication was created.
	 *
	 * @param checksum the metadata checksum
	 * @return publication builder
	 */
	@NonNull
	public B checksum(String checksum) {
		this.checksum = checksum;
		return myself();
	}

	/**
	 * Specify the timestamp when this publication was created.
	 *
	 * @param publishedAt the publication date
	 * @return publication builder
	 */
	@NonNull
	public B publishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
		return myself();
	}

	/**
	 * Creates the {@link Publication} as a result of this builder.
	 *
	 * @return the publication instance, never {@literal null}.
	 */
	@NonNull
	public abstract T build();

}
