package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a single, settled entry within a service's {@link Manifest}: an {@link Artifact} together
 * with the checksum and provenance of the {@link ArtifactMetadata} that was captured for it.
 * <p>
 * {@link ManifestEntry} extends {@link Artifact}, not {@link Publication}, even though the two look
 * superficially similar. A settled entry needs to say where its metadata came from ({@link #source()})
 * and what its checksum is ({@link #checksum()}), but it must not carry {@link Publication#state()} or
 * {@link Publication#errors()}. Those describe an in-progress publish attempt, not a property of a settled
 * entry: once a {@link Manifest} is published, every entry in it is equally "there", there is no such thing
 * as one entry being {@code PENDING} while the manifest itself is live. {@link Publication} carries
 * {@code state()}/{@code errors()} for a good reason at its own level, since an artifact version genuinely
 * does go through a pending-to-settled lifecycle. Extending it here would import fields that are meaningless
 * for a manifest entry and would silently invite a future reader to ask "why is this always empty?"
 *
 * @author Vladimir Spasic
 * @see Manifest
 * @see ArtifactSource
 * @since 1.0.0
 */
public interface ManifestEntry extends Artifact, Serializable {

	/**
	 * Creates a new instance of the {@link DefaultManifestEntry.Builder} used to create a new instance
	 * of the {@link DefaultManifestEntry} using the fluent builder API.
	 *
	 * @return default manifest entry builder, never {@literal null}
	 */
	static DefaultManifestEntry.Builder builder() {
		return new DefaultManifestEntry.Builder();
	}

	/**
	 * Checksum of the {@link ArtifactMetadata artifact metadata} that was captured for this entry.
	 *
	 * @return manifest entry checksum, never {@literal null}.
	 */
	@NonNull
	String checksum();

	/**
	 * Where this entry's {@link ArtifactMetadata} originated from.
	 *
	 * @return manifest entry source, never {@literal null}.
	 */
	@NonNull
	ArtifactSource source();

	/**
	 * Timestamp when this entry's metadata was captured.
	 *
	 * @return manifest entry resolution timestamp, never {@literal null}.
	 */
	@NonNull
	Instant resolvedAt();

}
