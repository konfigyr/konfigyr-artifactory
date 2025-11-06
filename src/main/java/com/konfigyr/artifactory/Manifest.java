package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Represents a versioned manifest of all artifacts currently used by a specific Konfigyr {@code Service}.
 * <p>
 * A {@code Manifest} provides a consistent view of the software components that make up a service’s runtime or
 * deployment environment. It is primarily used by build plugins and integrations to detect configuration drift
 * between the state of the deployed service and the state registered in the Konfigyr {@code Artifactory}.
 *
 * <h2>Manifest Purpose</h2>
 * Within the Konfigyr {@code Artifactory}, a manifest:
 * <ul>
 *   <li>Captures the current dependency composition of a {@code Service}.</li>
 *   <li>Acts as a reference snapshot for detecting new, missing, or outdated {@link Artifact}s.</li>
 *   <li>Serves as an upload boundary; plugins only submit metadata for artifacts not already present in the manifest.</li>
 *   <li>Supports provenance tracking, enabling diffs between manifests to identify configuration changes across releases.</li>
 * </ul>
 *
 * <h2>Typical Workflow</h2>
 * <ol>
 *   <li>A service build process requests the current manifest from the Konfigyr {@code Artifactory}.</li>
 *   <li>The build plugin compares the retrieved manifest with the current state to identify new or changed artifacts.</li>
 *   <li>Only the metadata for new or modified artifacts is uploaded.</li>
 * </ol>
 *
 * @author Vladimir Spasic
 * @see Artifact
 * @since 1.0.0
 */
public interface Manifest extends Iterable<Artifact>, Serializable {

	/**
	 * Builder factory method for constructing new {@link DefaultManifest} instances.
	 *
	 * @return a new build instance, never {@literal null}.
	 */
	static DefaultManifest.Builder builder() {
		return new DefaultManifest.Builder();
	}

	/**
	 * Returns the unique identifier of the {@code Service} this manifest belongs to.
	 *
	 * @return the service identifier, never {@literal null}.
	 */
	@NonNull
	String id();

	/**
	 * Returns the name of the {@code Service} this manifest belongs to.
	 *
	 * @return the service name, never {@literal null}.
	 */
	@NonNull
	String name();

	/**
	 * Returns the timestamp when this manifest was created.
	 * <p>
	 * This timestamp reflects when the manifest snapshot was generated, not when it was uploaded to Konfigyr.
	 *
	 * @return creation timestamp, never {@literal null}.
	 */
	@NonNull
	Instant createdAt();

	/**
	 * Returns the list of all {@link Artifact artifacts} that belong to this manifest.
	 * <p>
	 * Each entry describes a distinct {@link Artifact} currently in use by the service.
	 *
	 * @return immutable list of manifest entries, never {@literal null} but may be empty.
	 */
	@NonNull
	List<Artifact> artifacts();

	/**
	 * Checks if a specific {@link Artifact} is contained within this manifest by matching the given
	 * artifact coordinates.
	 *
	 * @param artifact the artifact to be checked, must not be {@literal null}.
	 * @return {@code true} if given artifact is contained in this manifest entry, or {@code false} if not.
	 */
	default boolean contains(@NonNull Artifact artifact) {
		for (Artifact candidate : this) {
			if (candidate.groupId().equals(artifact.groupId()) && candidate.artifactId().equals(artifact.artifactId())
					&& candidate.version().equals(artifact.version())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds a specific {@link Artifact} in this manifest matching the given artifact coordinates.
	 *
	 * @param groupId    the artifact’s {@code groupId}, must not be {@literal null}.
	 * @param artifactId the artifact’s {@code artifactId}, must not be {@literal null}.
	 * @return an {@link Optional} containing the matching manifest entry, or empty if not found.
	 */
	@NonNull
	default Optional<Artifact> find(String groupId, String artifactId) {
		for (Artifact artifact : this) {
			if (artifact.groupId().equals(groupId) && artifact.artifactId().equals(artifactId)) {
				return Optional.of(artifact);
			}
		}
		return Optional.empty();
	}

	@NonNull
	@Override
	default Iterator<Artifact> iterator() {
		return artifacts().iterator();
	}

}
