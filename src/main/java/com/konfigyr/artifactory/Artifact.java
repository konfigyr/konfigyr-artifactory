package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Comparator;

/**
 * Represents a uniquely identifiable software artifact within the Konfigyr {@code Artifactory}.
 * <p>
 * An {@code Artifact} corresponds to a distinct software component such as a library,
 * module, or application, typically identified by its <em>Maven coordinates</em>:
 * {@code groupId}, {@code artifactId}, and {@code version}.
 * <p>
 * Artifacts serve as the primary aggregation root for managing different software versions and their
 * corresponding {@link PropertyDescriptor configuration metadata} discovered within uploaded build
 * artifacts or dependency graphs. Each artifact provides contextual information such as its descriptive
 * name, website, and source repository location.
 *
 * <h2>Artifact Purpose</h2>
 * Within the Konfigyr {@code Artifactory}, artifacts provide a stable, addressable entity that:
 * <ul>
 *   <li>Identifies and classifies configuration sources by Maven coordinates.</li>
 *   <li>Serves as the provenance root for {@link PropertyDescriptor property metadata} and dependency tracing.</li>
 *   <li>Supports linking external references such as documentation URLs and SCM repositories.</li>
 * </ul>
 *
 * <h2>Identity and Comparison</h2>
 * Artifacts are uniquely identified by their Maven coordinates, {@code groupId}, {@code artifactId},
 * and {@code version}. The {@link #compareTo(Artifact)} implementation defines a lexicographic ordering
 * consistent with Maven semantics.
 *
 * <h2>Instantiation</h2>
 * Artifacts can be created programmatically using either:
 * <ul>
 *   <li>{@link #of(String, String, String)}: a simple factory method from Maven coordinates.</li>
 *   <li>{@link #builder()}: a fluent builder API for constructing rich {@link DefaultArtifact} instances.</li>
 * </ul>
 *
 * @author : Vladimir Spasic
 * @see PropertyDescriptor
 * @see Release
 * @since 1.0.0
 **/
public interface Artifact extends Comparable<Artifact>, Serializable {

	/**
	 * Creates a new instance of the {@link Artifact} using the Maven coordinates.
	 * <p>
	 * This is a convenience factory method that constructs a {@link Artifact} with the given Maven
	 * coordinates, equivalent to invoking:
	 *
	 * <pre>{@code
	 * Artifact artifact = Artifact.builder()
	 *     .groupId(groupId)
	 *     .artifactId(artifactId)
	 *     .version(version)
	 *     .build();
	 * }</pre>
	 *
	 * @param groupId    the Maven {@code groupId}, e.g. {@code org.springframework.boot}.
	 * @param artifactId the Maven {@code artifactId}, e.g. {@code spring-boot-starter-web}.
	 * @param version    the Maven {@code version}, e.g. {@code 3.3.1}.
	 * @return a new immutable {@link Artifact} instance, never {@literal null}.
	 */
	@NonNull
	static Artifact of(String groupId, String artifactId, String version) {
		return builder().groupId(groupId).artifactId(artifactId).version(version).build();
	}

	/**
	 * Creates a new instance of the {@link DefaultArtifact.Builder} used to create a new instance of
	 * the {@link DefaultArtifact} using the fluent builder API.
	 * <p>
	 * This builder allows additional metadata such as {@link #name()}, {@link #description()},
	 * {@link #website()}, and {@link #repository()} to be specified.
	 *
	 * @return default artifact builder, never {@literal null}.
	 */
	static DefaultArtifact.Builder builder() {
		return new DefaultArtifact.Builder();
	}

	/**
	 * Returns the {@code groupId} Maven coordinate of the artifact.
	 * <p>
	 * The {@code groupId} identifies the organization or domain under which the artifact is published.
	 * For example, {@code org.springframework.boot}.
	 *
	 * @return the {@code groupId} Maven coordinate, never {@literal null}.
	 */
	@NonNull
	String groupId();

	/**
	 * Returns the {@code artifactId} Maven coordinate of the artifact.
	 * <p>
	 * The {@code artifactId} uniquely identifies the module within a given {@code groupId}.
	 * For example, {@code spring-boot-starter-web}.
	 *
	 * @return the {@code artifactId} Maven coordinate, never {@literal null}.
	 */
	@NonNull
	String artifactId();

	/**
	 * Returns the {@code version} Maven coordinate of the artifact.
	 * <p>
	 * The {@code version} identifies the specific release of the artifact. This may represent semantic or
	 * timestamp-based versioning depending on the build system.
	 *
	 * @return the {@code version} Maven coordinate, never {@literal null}.
	 */
	@NonNull
	String version();

	/**
	 * Returns the human-readable name of the artifact.
	 * <p>
	 * The name is an optional descriptive label used for display purposes, such as {@code "Spring Boot Starter Web"}.
	 *
	 * @return artifact name, may be {@literal null}.
	 */
	@Nullable
	String name();

	/**
	 * Returns the textual description of the artifact.
	 * <p>
	 * The description provides a concise explanation of the artifact’s purpose, contents, or usage context.
	 *
	 * @return artifact description, may be {@literal null}.
	 */
	@Nullable
	String description();

	/**
	 * Returns the Uniform Resource Identifier (URI) reference to the artifact’s official website or
	 * online documentation.
	 * <p>
	 * This URI can point to a project homepage, API reference, or user documentation providing additional
	 * context about the artifact.
	 *
	 * @return website location, may be {@literal null}.
	 */
	@Nullable
	URI website();

	/**
	 * Returns the Uniform Resource Identifier (URI) reference to the artifact’s Source Control Management (SCM)
	 * repository.
	 * <p>
	 * This typically references the public Git, SVN, or other repository where the artifact’s source code
	 * is maintained.
	 *
	 * @return repository location, may be {@literal null}.
	 */
	@Nullable
	URI repository();

	/**
	 * Creates an {@link ArtifactMetadata} for this {@link Artifact} with the given collection
	 * of {@link PropertyDescriptor property descriptors}.
	 *
	 * @param descriptors property descriptors used to prepare the artifact metadata instance, never {@literal null}.
	 * @return artifact metadata, never {@literal null}
	 */
	@NonNull
	default ArtifactMetadata toMetadata(@NonNull Collection<? extends PropertyDescriptor> descriptors) {
		return ArtifactMetadata.builder().artifact(this).properties(descriptors).build();
	}

	/**
	 * Compares this artifact to another artifact using Maven coordinate ordering.
	 * <p>
	 * The comparison is lexicographic and performed in the following order: {@code groupId},
	 * then {@code artifactId}, then {@code version}.
	 * <p>
	 * This ensures consistent sorting and equality semantics across artifact collections.
	 *
	 * @param other the artifact to compare against, never {@literal null}.
	 * @return a negative integer, zero, or a positive integer as this artifact is
	 * less than, equal to, or greater than the specified artifact.
	 */
	@Override
	default int compareTo(@NonNull Artifact other) {
		return Comparator.comparing(Artifact::groupId)
				.thenComparing(Artifact::artifactId)
				.thenComparing(Artifact::version)
				.compare(this, other);
	}

}
