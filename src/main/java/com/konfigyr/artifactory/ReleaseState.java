package com.konfigyr.artifactory;

/**
 * Describes the states of the {@link Release}.
 *
 * @author : Vladimir Spasic
 * @since : 24.09.23, Sun
 **/
public enum ReleaseState {

	/**
	 * The {@link ArtifactMetadata} package has been uploaded, and the Konfigyr Artifactory is
	 * processing the data and drafting a new {@link Release} with the specified version.
	 * <p>
	 * The {@link Release} in this state should not be consumed Konfigyr Clients as it is
	 * not fully processed and finalized.
	 */
	PENDING,

	/**
	 * The {@link ArtifactMetadata} has been full processed and the {@link Release} should be
	 * made available by Konfigyr Clients.
	 */
	RELEASED,

	/**
	 * In case the uploaded {@link ArtifactMetadata} encountered any errors during it's
	 * processing, the {@link Release} should be moved to a {@code #FAILED} state.
	 * <p>
	 * Users may attempt to upload the {@link ArtifactMetadata} again if the package contained
	 * corrupted data or may retry the operation.
	 * <p>
	 * The {@link Release} in this state should not be consumed Konfigyr Clients.
	 */
	FAILED

}
