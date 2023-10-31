package com.konfigyr.artifactory;

import com.github.zafarkhaja.semver.Version;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.List;

/**
 * A component is a package containing {@link PropertyDescriptor property metadata} that
 * your {@link Artifact} uses (e.g., a library or a framework) within the current release
 * version.
 * <p>
 * By uploading your {@link Component} the Konfigyr Artifactory would create a new
 * {@link Release} with a specified version number, process and store the configuration
 * property descriptors.
 * <p>
 * You can check the status of the {@link Release} at any given time using the Konfigyr
 * Artifactory REST API or using your Konfigyr Artifactory dashboard.
 * <p>
 * The version needs to be <a href="https://semver.org">SemVer</a> compliant. Please read
 * the semantic versioning documentation on how to create your versions.
 *
 * @param version the artifact version that would be used to draft a new release, can't be
 * {@literal null}
 * @param latest when {@code true}, specifies that the drafted release is marked as latest
 * @param properties configuration properties that are used by the current artifact
 * version, can't be {@literal null}
 * @author : Vladimir Spasic
 * @since : 24.09.23, Sun
 **/
public record Component(@Nonnull Version version, boolean latest,
		@Nonnull List<PropertyDescriptor> properties) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8643778042554549460L;

	/**
	 * Creates a new instance of the {@link Component} using the serialized
	 * {@link PropertyDescriptor} data represented as a byte array.
	 * @param version the artifact version that would be used to draft a new release,
	 * can't be {@literal null}
	 * @param latest when {@code true}, specifies that the drafted release is marked as
	 * latest
	 * @param data serialized property descriptors, can't be {@literal null}
	 */
	public Component(@Nonnull Version version, boolean latest, @Nonnull byte[] data) {
		this(version, latest, Serializer.getInstance().read(data));
	}

	/**
	 * Creates a new instance of the {@link Component} using the serialized
	 * {@link PropertyDescriptor} data represented as an {@link InputStream}.
	 * @param version the artifact version that would be used to draft a new release,
	 * can't be {@literal null}
	 * @param latest when {@code true}, specifies that the drafted release is marked as
	 * latest
	 * @param data serialized property descriptors, can't be {@literal null}
	 */
	public Component(@Nonnull Version version, boolean latest, @Nonnull InputStream data) {
		this(version, latest, Serializer.getInstance().read(data));
	}

	/**
	 * Serializes the {@link PropertyDescriptor property descriptors} into a byte array so
	 * it can be uploaded to the Konfigyr Artifactory multipart binary file.
	 * @return serialized metadata, never {@literal null}
	 */
	public @Nonnull byte[] serialize() {
		return Serializer.getInstance().write(properties);
	}

}
