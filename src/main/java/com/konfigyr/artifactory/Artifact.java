package com.konfigyr.artifactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;
import java.util.Comparator;

/**
 * Artifact is the main building block of the Konfigyr Artifactory. An artifact can be any
 * type of software that can be tweaked via configuration properties. It consists of the
 * following:
 * <ul>
 * <li>{@link Release artifact releases}</li>
 * <li>{@link PropertyDescriptor configuration property descriptors}</li>
 * </ul>
 * The {@link PropertyDescriptor configuration property metadata} is not directly
 * associated to an {@link Artifact} but rather through the {@link Release published
 * versions or releases} of the artifact itself.
 * <p>
 * This is because the Konfigyr Artifactory needs to track property changes throughout the
 * lifetime of an {@link Artifact}. As the software evolves it matures and changes and as
 * a consequence, the configuration used to customize it also changes. Configuration
 * property from the earliest version may not be present anymore, or it has been changed
 * to receive a different value type, or it is deprecated or replaced. The goal of the
 * Konfigyr Artifactory is to keep track of these changes and provide the best possible
 * support to developers when it comes to configuration management.
 * <p>
 * Artifacts are uniquely identified by their name and namespace they belong to. This way
 * Artifactory avoids clashes on artifacts that are developed by different persons, teams
 * or organizations that may have the same name.
 * <p>
 * Think about if there is an artifact named <strong>crypto</strong> or
 * <strong>utils</strong>, there would be a lot of clashes within the registry and every
 * artifact code owner would struggle.
 * <p>
 * Another attribute that defines the {@link Artifact Artifacts} is their package name and
 * the {@link Platform}. Those two attributes defines how the package is looked up by
 * their package names within their respectable package repositories, like Maven Central,
 * NPM, RubyGems, PyPI and many others.
 *
 * @author : Vladimir Spasic
 * @since : 02.10.22, Sun
 * @see Platform
 * @see PropertyDescriptor
 * @see Release
 **/
public interface Artifact extends Comparable<Artifact>, Serializable {

	/**
	 * Artifact identifier that consists out of the namespace and artifact names.
	 * @return artifact identifier, can't be {@literal null}
	 */
	@Nonnull
	String getIdentifier();

	/**
	 * Namespace identifier to which this {@link Artifact} belongs to.
	 * @return namespace, can't be {@literal null}
	 */
	@Nonnull
	String getNamespace();

	/**
	 * The name of this artifact. Must be unique within the namespace.
	 * @return artifact name, can't be {@literal null}
	 */
	@Nonnull
	String getName();

	/**
	 * What's the underlying platform for this {@link Artifact}.
	 * @return artifact platform, can't be {@literal null}
	 */
	@Nonnull
	Platform getPlatform();

	/**
	 * The name of the package in the responsible package repository for this artifact.
	 * @return artifact name, can't be {@literal null}
	 */
	@Nonnull
	String getPackageName();

	/**
	 * The detailed description of the artifact.
	 * @return artifact description, can be {@literal null}.
	 */
	@Nullable
	String getDescription();

	/**
	 * The URL to the website or documentation of the artifact.
	 * @return website URL, Can be {@literal null}.
	 */
	@Nullable
	String getUrl();

	/**
	 * The URL to the artifact SCM (Source Control Management) repository of the artifact.
	 * @return repository URL, Can be {@literal null}.
	 */
	@Nullable
	String getRepository();

	@Nullable
	Instant getCreatedDate();

	@Nullable
	Instant getLastUpdatedDate();

	@Override
	default int compareTo(@Nonnull Artifact o) {
		return Comparator.comparing(Artifact::getName).compare(this, o);
	}

}
