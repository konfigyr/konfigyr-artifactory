package com.konfigyr.artifactory;

/**
 * Describes, for a single {@link Artifact} being resolved during a service's release build, whether the
 * build plugin still needs to upload its {@link ArtifactMetadata}.
 * <p>
 * This is a per-artifact, resolve-time-only status and is intentionally kept as its own enum instead of
 * being folded into {@link ReleaseState}. The two describe different axes at different granularity:
 * <ul>
 *     <li>{@link ArtifactUploadStatus} is per-artifact ("does the plugin need to upload this one")</li>
 *     <li>{@link ReleaseState} is whole-build ("is the build as a whole done")</li>
 * </ul>
 * Forcing one enum to cover both would produce meaningless values in both directions, an individual artifact
 * has no {@code FAILED} upload status, and a build as a whole has no {@code SKIP} state.
 *
 * @author Vladimir Spasic
 * @see ServiceReleaseEntry
 * @see ReleaseState
 * @since 1.0.0
 */
public enum ArtifactUploadStatus {

	/**
	 * The artifact is not yet present in the Konfigyr {@code Artifactory}, or is present but with a
	 * different checksum than what was resolved locally, so the build plugin must upload its
	 * {@link ArtifactMetadata} before the release can proceed.
	 */
	UPLOAD_REQUIRED,

	/**
	 * The artifact is already present in the Konfigyr {@code Artifactory} with a matching checksum, or
	 * was already uploaded earlier in this same build, so the build plugin can skip uploading it.
	 */
	SKIP

}
