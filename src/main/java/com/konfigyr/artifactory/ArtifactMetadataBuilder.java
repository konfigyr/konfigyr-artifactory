package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract builder class used to create new instances of the {@link ArtifactMetadata} that can be reused
 * for different artifact metadata implementations.
 *
 * @param <T> the artifact metadata implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see ArtifactMetadata
 * @since 1.0.0
 */
public abstract class ArtifactMetadataBuilder<T extends ArtifactMetadata, B extends ArtifactMetadataBuilder<T, B>> extends ArtifactBuilder<T, B> {

	/**
	 * The checksum of all property descriptors within this metadata.
	 * <p>
	 * Optional: if left unset, it is derived automatically when the metadata is built.
	 */
	protected @Nullable String checksum;

	/**
	 * The collection of property descriptors that are a part of the artifact.
	 */
	protected final List<PropertyDescriptor> properties;

	/**
	 * Creates a new {@link ArtifactMetadataBuilder} instance.
	 */
	protected ArtifactMetadataBuilder() {
		properties = new ArrayList<>();
	}

	/**
	 * Specify the {@link Artifact} for which this {@link ArtifactMetadata} would be created for.
	 *
	 * @param artifact artifact
	 * @return artifact metadata builder
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
	 * Specify the {@code checksum} for this {@link ArtifactMetadata}.
	 * <p>
	 * This is optional: when left unset, the checksum is derived automatically from the property
	 * descriptors when the metadata is built. Only set this explicitly when reconstructing metadata
	 * that was already assigned a checksum, e.g. when loading it back from storage.
	 *
	 * @param checksum artifact metadata checksum
	 * @return artifact metadata builder
	 */
	public B checksum(@Nullable String checksum) {
		this.checksum = checksum;
		return myself();
	}

	/**
	 * Adds a single property descriptor to the artifact metadata that should be uploaded.
	 *
	 * @param descriptor property descriptor to be added
	 * @return artifact metadata builder
	 */
	public B property(@Nullable PropertyDescriptor descriptor) {
		if (descriptor != null) {
			this.properties.add(descriptor);
		}
		return myself();
	}

	/**
	 * Adds multiple property descriptors to the artifact metadata that should be uploaded.
	 *
	 * @param descriptors property descriptors to be added
	 * @return artifact metadata builder
	 */
	public B properties(@Nullable Iterable<? extends PropertyDescriptor> descriptors) {
		if (descriptors != null) {
			for (PropertyDescriptor descriptor : descriptors) {
				property(descriptor);
			}
		}
		return myself();
	}

	@Override
	protected void validate() {
		super.validate();
		Asserts.notEmpty(properties, "Artifact metadata must contain at least one property descriptor");
	}

	/**
	 * Computes the checksum of the given, already sorted, property descriptors by visiting each of
	 * their {@link PropertyDescriptor#schema()} with a {@link JsonSchemaDigestVisitor}.
	 * <p>
	 * Available to subclasses so that any {@link ArtifactMetadataBuilder} implementation can derive
	 * a checksum consistently, without depending on {@link DefaultArtifactMetadata}.
	 *
	 * @param properties the sorted property descriptors to be hashed, never {@literal null}.
	 * @return the computed checksum, never {@literal null}.
	 */
	protected static String checksum(List<PropertyDescriptor> properties) {
		final JsonSchemaDigestVisitor visitor = JsonSchemaDigestVisitor.of();

		for (PropertyDescriptor descriptor : properties) {
			descriptor.schema().accept(visitor);
		}

		return visitor.checksum();
	}

}
