package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.net.URI;

/**
 * Describes the basic identity and descriptive metadata of an artifact managed by the {@code Artifactory}.
 * <p>
 * An artifact in the Konfigyr Artifactory represents a reusable software component, typically identified
 * by its Maven coordinates. These coordinates uniquely identify the logical artifact across all versions
 * and releases.
 * <p>
 * The {@link ArtifactDescriptor} interface defines the minimal set of information that characterizes such
 * an artifact. It is intentionally limited to fields that describe the artifact itself rather than a
 * specific release. Implementations may represent different stages of the artifact lifecycle, such as:
 * <ul>
 *     <li>a persistent artifact definition stored in the Artifactory domain</li>
 *     <li>a versioned artifact representing a specific release</li>
 *     <li>a metadata payload submitted by a build plugin during ingestion</li>
 * </ul>
 * <p>
 * The artifact identity is defined by the combination of {@code groupId} and {@code artifactId}. These
 * values correspond to the Maven coordinate system and together uniquely identify the logical artifact
 * across all versions.
 * <p>
 * In addition to the coordinates, the descriptor may provide optional descriptive metadata such as a
 * human-readable name, a textual description, and references to documentation or source code repositories.
 * This metadata is intended to help developers understand the purpose and origin of the artifact and may
 * be displayed in user interfaces or documentation systems.
 * <p>
 * Implementations of this interface are expected to be immutable and serializable. This allows descriptors
 * to be safely transported across system boundaries, persisted in storage, or included in metadata ingestion
 * workflows.
 * <p>
 * The {@code ArtifactDescriptor} does not include version information. Versioning is handled separately by
 * types such as {@link Artifact}, which represent a specific release of the artifact.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public interface ArtifactDescriptor extends Serializable {

	/**
	 * Returns the {@code groupId} component of the artifact coordinates.
	 * <p>
	 * The group identifier typically follows the reverse-domain naming convention (for example
	 * {@code org.springframework} or {@code io.github.example}) and groups related artifacts under
	 * a common namespace.
	 * <p>
	 * Together with {@link #artifactId()}, this value uniquely identifies the logical artifact within
	 * the Konfigyr {@code Artifactory}.
	 *
	 * @return the {@code groupId} coordinate of the artifact, never {@literal null}.
	 */
	String groupId();

	/**
	 * Returns the {@code artifactId} component of the artifact coordinates.
	 * <p>
	 * The artifact identifier represents the specific module or library within a group. Examples include
	 * {@code spring-core}, {@code konfigyr-sdk}, or {@code jackson-databind}.
	 * <p>
	 * Combined with {@link #groupId()}, this value uniquely identifies the logical artifact across all versions.
	 *
	 * @return the {@code artifactId} coordinate of the artifact, never {@literal null}.
	 */
	String artifactId();

	/**
	 * Returns the human-readable name of the artifact.
	 * <p>
	 * The name provides a more descriptive label for the artifact than the technical Maven coordinates and
	 * is primarily intended for display in user interfaces, documentation, or artifact catalogs.
	 * <p>
	 * For example, an artifact with coordinates {@code org.springframework:spring-core} may expose the
	 * name {@code Spring Core}.
	 *
	 * @return the artifact name, or {@literal null} if not specified
	 */
	@Nullable
	String name();

	/**
	 * Returns the textual description of the artifact.
	 * <p>
	 * The description typically explains the purpose, capabilities, or intended usage of the artifact.
	 * It may originate from project metadata such as Maven POM files or build plugin configuration.
	 * <p>
	 * This value is optional and may be used by tooling or user interfaces to provide additional context
	 * about the artifact.
	 *
	 * @return the artifact description, or {@literal null} if not available
	 */
	@Nullable
	String description();

	/**
	 * Returns a reference to a website containing documentation or additional information about the artifact.
	 * <p>
	 * This may point to a project homepage, documentation portal, or other publicly accessible resources
	 * that help users understand how the artifact should be used.
	 * <p>
	 * The URI is optional and may be absent if the artifact does not expose an official documentation site.
	 *
	 * @return the documentation or project website URI, or {@literal null} if none exists
	 */
	@Nullable
	URI website();

	/**
	 * Returns a reference to the source control repository (SCM) associated with the artifact.
	 * <p>
	 * This URI typically points to a source code hosting platform such as GitHub, GitLab, or Bitbucket and
	 * may be used by tooling to link artifacts with their corresponding source repositories.
	 * <p>
	 * The repository reference is optional and may not be available for all artifacts.
	 *
	 * @return the source control repository URI, or {@literal null} if none exists
	 */
	@Nullable
	URI repository();

}
