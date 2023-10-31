package com.konfigyr.artifactory;

/**
 * Enumeration that lists supported {@link Artifact} platforms within Konfigyr
 * Artifactory.
 * <p>
 * Platforms are used to identify the environment in which the {@link Artifact artifacts}
 * are used and where they are made available by their respective package managers.
 * <p>
 * This is especially important to Artifactory clients when they are performing lookups
 * against the artifact registry. Imagine you need to grab a list of {@link Artifact
 * artifacts} from a Gradle or NPM project dependencies. You need to make sure, as a
 * client, to only retrieve artifacts matching the {@link Platform}, for which the client
 * is responsible for, and for the package, or dependency, names extracted from that
 * project.
 *
 * @author : Vladimir Spasic
 * @since : 23.09.23, Sat
 **/
public enum Platform {

	JVM

}
