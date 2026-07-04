package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

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
	 * The state of the publication. Optional: defaults to {@link PublicationState#PENDING} when
	 * left unset.
	 */
	protected @Nullable PublicationState state;

	/**
	 * Checksum of the {@link PropertyDescriptor property metadata} that was published.
	 */
	protected @Nullable String checksum;

	/**
	 * Timestamp when this publication was created.
	 */
	protected @Nullable Instant publishedAt;

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
	public B state(@Nullable PublicationState state) {
		this.state = state;
		return myself();
	}

	/**
	 * Adds a single publication error message that caused this publication to fail.
	 *
	 * @param error the publication error message
	 * @return publication builder
	 */
	public B error(@Nullable String error) {
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
	public B errors(@Nullable Iterable<String> errors) {
		if (errors != null) {
			for (String error : errors) {
				error(error);
			}
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
	public B publishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
		return myself();
	}

	@Override
	protected void validate() {
		super.validate();

		Asserts.notBlank(checksum, "Publication property metadata checksum can not be blank");
		Asserts.nonNull(publishedAt, "Publication date can not be null");

		if (state == null) {
			state = PublicationState.PENDING;
		}
	}

}
