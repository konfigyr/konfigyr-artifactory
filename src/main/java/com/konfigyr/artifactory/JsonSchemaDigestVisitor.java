package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation of the {@link JsonSchemaVisitor} that produces a deterministic cryptographic digest
 * for a {@link JsonSchema} tree. The generated digest is used as part of the Artifactory's occurrence
 * identity model and therefore must be:
 *
 * <ul>
 *     <li><b>Deterministic</b>: identical logical values must always produce the same digest</li>
 *     <li><b>Structural</b>: hashing is based on structure and content, not textual representation</li>
 *     <li><b>Canonical</b>: object field ordering and numeric formatting must not influence the result</li>
 * </ul>
 *
 * <h2>Design Principles</h2>
 *
 * Unlike textual hashing approaches (e.g., hashing serialized JSON), this visitor does not rely on JSON
 * string serialization. Instead, it walks the {@link JsonSchema} object graph and feeds deterministic byte
 * sequences into a {@link MessageDigest}. This avoids instability caused by:
 *
 * <ul>
 *     <li>Object key ordering differences</li>
 *     <li>Whitespace or formatting variations</li>
 *     <li>Non-canonical number representations</li>
 *     <li>Library-specific serialization behavior</li>
 * </ul>
 *
 * <h2>Deterministic Encoding Rules</h2>
 *
 * <ul>
 *     <li>Each value emits a stable type marker before its contents.</li>
 *     <li>Object fields are sorted lexicographically by key before hashing.</li>
 *     <li>Arrays preserve their declared element order.</li>
 *     <li>Strings are encoded as UTF-8 with a length prefix so that adjacent fields can never be
 *         confused with one another (e.g. {@code "ab"} followed by {@code "c"} does not hash the same
 *         as {@code "a"} followed by {@code "bc"}).</li>
 *     <li>Numbers are encoded using {@code BigDecimal.toPlainString()} to ensure canonical form,
 *         so that {@code -0.0} and {@code 0.0} hash identically.</li>
 *     <li>Booleans are encoded as a single byte (0 or 1).</li>
 *     <li>{@code null} or {@code empty} values emit a dedicated marker.</li>
 * </ul>
 *
 * <h2>Versioning</h2>
 *
 * A version marker is written into the digest stream at construction time. This ensures that future changes
 * to hashing semantics (e.g., additional normalization rules) can be introduced safely without corrupting
 * previously stored hashes.
 * <p>
 * Any change to encoding rules <b>must</b> increase or change the hash version.
 *
 * <h2>Thread Safety</h2>
 *
 * This class is <b>not thread-safe</b>. Each instance wraps a mutable {@link MessageDigest} and must not
 * be reused across concurrent threads.
 * <p>
 * {@link #digest()} and {@link #checksum()} are terminal operations: like {@link MessageDigest#digest()},
 * retrieving the hash resets the underlying digest. A visitor instance must therefore be used to compute
 * at most one hash, call either method exactly once, after every relevant {@link JsonSchema} has been
 * visited, and discard the visitor afterward.
 *
 * <h2>Usage</h2>
 *
 * A visitor is created using one of the {@code of(...)} factory methods, used to visit a single
 * {@link JsonSchema} tree, and then discarded once its digest has been retrieved:
 *
 * <pre>{@code
 * JsonSchemaDigestVisitor visitor = JsonSchemaDigestVisitor.of("SHA-256");
 * schema.accept(visitor);
 *
 * String checksum = visitor.checksum();
 * }</pre>
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public final class JsonSchemaDigestVisitor implements JsonSchemaVisitor {

	/**
	 * The default digest algorithm used by the {@link #of()} factory method.
	 * <p>
	 * This is the same algorithm used to compute the {@code Base64} encoded checksum returned by
	 * {@link ArtifactMetadata#checksum()}. Changing this default is a breaking change and must be
	 * accompanied by a bump of the {@link #VERSION} marker.
	 */
	public static final String DEFAULT_ALGORITHM = "SHA-256";

	private static final byte NULL_MARKER = 0x00;
	private static final byte PRESENT_MARKER = 0x01;
	private static final byte VERSION = 0x42;

	private final MessageDigest digest;

	private JsonSchemaDigestVisitor(MessageDigest digest) {
		this.digest = digest;
		digest.update(VERSION);
	}

	/**
	 * Creates a new {@link JsonSchemaDigestVisitor} backed by a {@link MessageDigest} instance using
	 * the {@link #DEFAULT_ALGORITHM}.
	 *
	 * @return the digest visitor, never {@literal null}.
	 */
	public static JsonSchemaDigestVisitor of() {
		return of(DEFAULT_ALGORITHM);
	}

	/**
	 * Creates a new {@link JsonSchemaDigestVisitor} backed by a {@link MessageDigest} instance for the
	 * given algorithm name, e.g. {@code SHA-256}.
	 *
	 * @param algorithm the name of the digest algorithm, must not be {@literal null}.
	 * @return the digest visitor, never {@literal null}.
	 * @throws IllegalStateException if the named {@code algorithm} is not available.
	 */
	public static JsonSchemaDigestVisitor of(String algorithm) {
		try {
			return of(MessageDigest.getInstance(algorithm));
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("%s algorithm is not available".formatted(algorithm), ex);
		}
	}

	/**
	 * Creates a new {@link JsonSchemaDigestVisitor} backed by a {@link MessageDigest} instance for the
	 * given algorithm name, obtained from the named security provider.
	 *
	 * @param algorithm the name of the digest algorithm, must not be {@literal null}.
	 * @param provider  the name of the security provider, must not be {@literal null}.
	 * @return the digest visitor, never {@literal null}.
	 * @throws IllegalStateException if the named {@code algorithm} or {@code provider} is not available.
	 */
	public static JsonSchemaDigestVisitor of(String algorithm, String provider) {
		try {
			return of(MessageDigest.getInstance(algorithm, provider));
		} catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
			throw new IllegalStateException("%s algorithm from %s provider is not available".formatted(algorithm, provider), ex);
		}
	}

	/**
	 * Creates a new {@link JsonSchemaDigestVisitor} backed by the given {@link MessageDigest} instance.
	 *
	 * @param digest the message digest instance to be used, must not be {@literal null}.
	 * @return the digest visitor, never {@literal null}.
	 */
	public static JsonSchemaDigestVisitor of(MessageDigest digest) {
		return new JsonSchemaDigestVisitor(digest);
	}

	/**
	 * Completes the digest computation for every {@link JsonSchema} that was visited so far and
	 * returns the resulting hash.
	 *
	 * @return the computed digest bytes, never {@literal null}.
	 */
	public byte[] digest() {
		return digest.digest();
	}

	/**
	 * Completes the digest computation for every {@link JsonSchema} that was visited so far and
	 * returns the resulting hash as a {@code Base64} encoded checksum.
	 * <p>
	 * This is a convenience method equivalent to invoking:
	 *
	 * <pre>{@code
	 * String checksum = Base64.getEncoder().encodeToString(visitor.digest());
	 * }</pre>
	 *
	 * @return the computed checksum, never {@literal null}.
	 * @see #digest()
	 */
	public String checksum() {
		return Base64.getEncoder().encodeToString(digest());
	}

	@Override
	public void visit(JsonSchema schema) {
		// this is the main entry point for generating the digest. Methods in this visitor
		// should be calling this method when they want to update the digest for a schema
		writeType(schema);
		writeString(schema.title());
		writeString(schema.description());
		writeBoolean(schema.deprecated());
		writeCollection(schema.enumerations(), this::writeString);
		writeCollection(schema.examples(), this::writeString);

		JsonSchemaVisitor.super.visit(schema);
	}

	/**
	 * Visits a single {@link PropertyDescriptor}, feeding its identifying fields, {@code name},
	 * {@code typeName}, {@code description}, {@code defaultValue}, and {@code deprecation}, into
	 * the digest, followed by its {@link PropertyDescriptor#schema() schema}.
	 * <p>
	 * Unlike {@link #visit(JsonSchema)}, which only captures the shape and constraints of a
	 * property's value, this method captures the descriptor's full identity, so that renaming,
	 * retyping, redocumenting, or deprecating a property changes its digest even when its schema
	 * does not.
	 *
	 * @param descriptor the property descriptor to visit, never {@literal null}.
	 */
	void visit(PropertyDescriptor descriptor) {
		writeString(descriptor.name());
		writeString(descriptor.typeName());
		writeString(descriptor.description());
		writeString(descriptor.defaultValue());
		writeDeprecation(descriptor.deprecation());
		visit(descriptor.schema());
	}

	@Override
	public void visitObject(ObjectSchema schema) {
		writeSchema(schema.propertyNames());
		writeSchema(schema.additionalProperties());
		writeCollection(schema.required(), this::writeString);
		writeCollection(schema.properties().keySet(), key -> {
			writeString(key);
			writeSchema(schema.properties().get(key));
		});
	}

	@Override
	public void visitArray(ArraySchema schema) {
		writeInteger(schema.minItems());
		writeInteger(schema.maxItems());
		writeBoolean(schema.uniqueItems());
		writeSchema(schema.items());
	}

	@Override
	public void visitString(StringSchema schema) {
		writeString(schema.format());
		writeString(schema.pattern());
		writeInteger(schema.minLength());
		writeInteger(schema.maxLength());
	}

	@Override
	public void visitNumber(NumberSchema schema) {
		writeString(schema.format());
		writeDouble(schema.minimum());
		writeDouble(schema.maximum());
		writeDouble(schema.multipleOf());
		writeBoolean(schema.exclusiveMinimum());
		writeBoolean(schema.exclusiveMaximum());
	}

	@Override
	public void visitInteger(IntegerSchema schema) {
		writeString(schema.format());
		writeLong(schema.minimum());
		writeLong(schema.maximum());
		writeDouble(schema.multipleOf());
		writeBoolean(schema.exclusiveMinimum());
		writeBoolean(schema.exclusiveMaximum());
	}

	private void writeType(JsonSchema schema) {
		final byte type = switch (schema.type()) {
			case OBJECT -> 0x01;
			case ARRAY -> 0x02;
			case BOOLEAN -> 0x03;
			case STRING -> 0x04;
			case NUMBER -> 0x05;
			case INTEGER -> 0x06;
			case NULL -> 0x07;
		};

		digest.update(type);
	}

	private void writeBoolean(boolean value) {
		digest.update(value ? PRESENT_MARKER : NULL_MARKER);
	}

	private void writeInteger(@Nullable Integer value) {
		if (value != null) {
			digest.update(PRESENT_MARKER);
			digest.update(ByteBuffer.allocate(4).putInt(value).array());
		} else {
			digest.update(NULL_MARKER);
		}
	}

	private void writeLong(@Nullable Long value) {
		if (value != null) {
			digest.update(PRESENT_MARKER);
			digest.update(ByteBuffer.allocate(8).putLong(value).array());
		} else {
			digest.update(NULL_MARKER);
		}
	}

	private void writeDouble(@Nullable Double value) {
		if (value != null) {
			// BigDecimal.valueOf(double) parses Double#toString, which already yields the shortest
			// round-tripping decimal representation, so -0.0 and 0.0 collapse to the same string.
			writeString(BigDecimal.valueOf(value).toPlainString());
		} else {
			digest.update(NULL_MARKER);
		}
	}

	private void writeString(@Nullable String value) {
		final String normalized = value == null ? null : value.strip();

		if (normalized == null || normalized.isEmpty()) {
			digest.update(NULL_MARKER);
			return;
		}

		final byte[] bytes = normalized.getBytes(StandardCharsets.UTF_8);

		digest.update(PRESENT_MARKER);
		digest.update(ByteBuffer.allocate(4).putInt(bytes.length).array());
		digest.update(bytes);
	}

	private void writeSchema(@Nullable JsonSchema schema) {
		if (schema != null) {
			visit(schema);
		} else {
			digest.update(NULL_MARKER);
		}
	}

	private void writeDeprecation(@Nullable Deprecation deprecation) {
		if (deprecation != null) {
			digest.update(PRESENT_MARKER);
			writeString(deprecation.reason());
			writeString(deprecation.replacement());
		} else {
			digest.update(NULL_MARKER);
		}
	}

	private void writeCollection(Collection<String> collection, Consumer<String> consumer) {
		if (collection.isEmpty()) {
			digest.update(NULL_MARKER);
			return;
		}

		final List<String> values = new ArrayList<>(collection);
		values.sort(String::compareTo);

		writeInteger(values.size());
		values.forEach(consumer);
	}

}
