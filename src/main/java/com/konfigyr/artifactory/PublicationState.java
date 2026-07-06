package com.konfigyr.artifactory;

/**
 * Describes the states of the {@link Publication}.
 *
 * @author : Vladimir Spasic
 * @since 1.0.0
 **/
public enum PublicationState {

	/**
	 * The {@link ArtifactMetadata} package has been uploaded, and the Konfigyr Artifactory is
	 * processing the data and drafting a new {@link Publication} with the specified version.
	 * <p>
	 * The {@link Publication} in this state should not be consumed Konfigyr Clients as it is
	 * not fully processed and finalized.
	 */
	PENDING,

	/**
	 * The {@link ArtifactMetadata} has been full processed and the {@link Publication} should be
	 * made available by Konfigyr Clients.
	 */
	PUBLISHED,

	/**
	 * In case the uploaded {@link ArtifactMetadata} encountered any errors during it's
	 * processing, the {@link Publication} should be moved to a {@code #FAILED} state.
	 * <p>
	 * Users may attempt to upload the {@link ArtifactMetadata} again if the package contained
	 * corrupted data or may retry the operation.
	 * <p>
	 * The {@link Publication} in this state should not be consumed Konfigyr Clients.
	 */
	FAILED

}
