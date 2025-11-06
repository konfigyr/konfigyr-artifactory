package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a request sent by a build plugin to upload configuration property metadata for a
 * specific {@link Artifact} into the Konfigyr {@code Artifactory}.
 * <p>
 * This request contains the artifact’s identifying coordinates, contextual information, and the
 * list of configuration property definitions discovered within the project’s classpath.
 * The {@code Artifactory} uses this information to deduplicate, version, and persist metadata
 * entries into the {@code Artifactory} registry.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public interface ArtifactMetadata extends Artifact, Iterable<PropertyDescriptor> {

	/**
	 * Creates a new instance of the {@link ArtifactMetadata} using the Maven coordinates and property
	 * descriptors
	 * <p>
	 * This is a convenience factory method that constructs a {@link ArtifactMetadata} with the given Maven
	 * coordinates and properties, equivalent to invoking:
	 *
	 * <pre>{@code
	 * ArtifactMetadata artifact = ArtifactMetadata.builder()
	 *     .groupId(groupId)
	 *     .artifactId(artifactId)
	 *     .version(version)
	 *     .properties(properties)
	 *     .build();
	 * }</pre>
	 *
	 * @param groupId     the Maven {@code groupId}, e.g. {@code org.springframework.boot}.
	 * @param artifactId  the Maven {@code artifactId}, e.g. {@code spring-boot-starter-web}.
	 * @param version     the Maven {@code version}, e.g. {@code 3.3.1}.
	 * @param descriptors the property descriptors to be added
	 * @return a new immutable {@link ArtifactMetadata} instance, never {@literal null}.
	 */
	@NonNull
	static ArtifactMetadata of(String groupId, String artifactId, String version, PropertyDescriptor... descriptors) {
		return builder()
				.groupId(groupId)
				.artifactId(artifactId)
				.version(version)
				.properties(Arrays.asList(descriptors))
				.build();
	}

	/**
	 * Creates a new instance of the {@link DefaultArtifactMetadata.Builder} used to create a new instance of
	 * the {@link DefaultArtifactMetadata} using the fluent builder API.
	 *
	 * @return default artifact metadata builder, never {@literal null}.
	 */
	static DefaultArtifactMetadata.Builder builder() {
		return new DefaultArtifactMetadata.Builder();
	}

	/**
	 * Returns the {@code Base64} encoded string of SHA-256 checksum that is generated based on
	 * the artifact metadata.
	 *
	 * @return checksum of the artifact metadata, may be {@literal null}.
	 */
	@Nullable
	String checksum();

	/**
	 * Returns the sorted collection of {@link PropertyDescriptor property descriptors} that
	 * are extracted from the build and should be uploaded to the {@code Artifactory}.
	 *
	 * @return the configuration property metadata extracted from the build, never {@literal null}.
	 */
	@NonNull
	List<PropertyDescriptor> properties();

	@NonNull
	@Override
	default Iterator<PropertyDescriptor> iterator() {
		return properties().iterator();
	}
}
