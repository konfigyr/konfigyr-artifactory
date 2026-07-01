package com.konfigyr.artifactory;

/**
 * Describes the states of a {@link ServiceRelease}.
 * <p>
 * This describes a whole service build's process state, {@code "is the build as a whole done?"}, which
 * is a different axis than {@link PublicationState}, which describes a single artifact version's publish
 * status.
 *
 * @author Vladimir Spasic
 * @see ServiceRelease
 * @see PublicationState
 * @since 1.0.0
 */
public enum ReleaseState {

	/**
	 * A build is currently in progress: the build plugin is resolving and uploading the artifacts that
	 * make up the service's {@link Manifest}.
	 */
	PENDING,

	/**
	 * The build was committed and is now the service's current {@link Manifest}.
	 */
	RELEASED,

	/**
	 * The last publish attempt failed — see {@link ServiceRelease#errors()} for the reasons why.
	 */
	FAILED

}
