package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

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
 * @since : 23.10.25, Thu
 */
public abstract class ArtifactMetadataBuilder<T extends ArtifactMetadata, B extends ArtifactMetadataBuilder<T, B>> extends ArtifactBuilder<T, B> {

	protected String checksum;
	protected final List<PropertyDescriptor> properties;

	protected ArtifactMetadataBuilder() {
		properties = new ArrayList<>();
	}

	/**
	 * Specify the {@link Artifact} for which this {@link ArtifactMetadata} would be created for.
	 *
	 * @param artifact artifact
	 * @return artifact metadata builder
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
	 * Specify the {@code checksum} for this {@link ArtifactMetadata}.
	 *
	 * @param checksum artifact metadata checksum
	 * @return artifact metadata builder
	 */
	@NonNull
	public B checksum(String checksum) {
		this.checksum = checksum;
		return myself();
	}

	/**
	 * Adds a single property descriptor to the artifact metadata that should be uploaded.
	 *
	 * @param descriptor property descriptor to be added
	 * @return artifact metadata builder
	 */
	@NonNull
	public B property(PropertyDescriptor descriptor) {
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
	@NonNull
	public B properties(Iterable<? extends PropertyDescriptor> descriptors) {
		if (descriptors != null) {
			for (PropertyDescriptor descriptor : descriptors) {
				property(descriptor);
			}
		}
		return myself();
	}

}
