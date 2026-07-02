package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JsonSchemaDigestVisitorTest {

	@Test
	@DisplayName("should create digest visitor using the default algorithm")
	void createUsingDefaultAlgorithm() {
		assertThat(checksum(JsonSchemaDigestVisitor.of(), StringSchema.instance()))
				.isEqualTo(checksum(JsonSchemaDigestVisitor.of(JsonSchemaDigestVisitor.DEFAULT_ALGORITHM), StringSchema.instance()));
	}

	@Test
	@DisplayName("should create digest visitor from an algorithm name")
	void createFromAlgorithm() {
		assertThat(checksum(JsonSchemaDigestVisitor.of("SHA-256"), StringSchema.instance())).isNotEmpty();
	}

	@Test
	@DisplayName("should create digest visitor from an algorithm name and provider")
	void createFromAlgorithmAndProvider() throws NoSuchAlgorithmException {
		final var provider = MessageDigest.getInstance("SHA-256").getProvider().getName();

		assertThat(checksum(JsonSchemaDigestVisitor.of("SHA-256", provider), StringSchema.instance())).isNotEmpty();
	}

	@Test
	@DisplayName("should fail to create digest visitor for an unknown algorithm")
	void unknownAlgorithm() {
		assertThatIllegalStateException()
				.isThrownBy(() -> JsonSchemaDigestVisitor.of("not-a-real-algorithm"))
				.withCauseInstanceOf(NoSuchAlgorithmException.class);
	}

	@Test
	@DisplayName("should fail to create digest visitor for an unknown provider")
	void unknownProvider() {
		assertThatIllegalStateException()
				.isThrownBy(() -> JsonSchemaDigestVisitor.of("SHA-256", "not-a-real-provider"))
				.withCauseInstanceOf(NoSuchProviderException.class);
	}

	@Test
	@DisplayName("should create digest visitor from a message digest instance")
	void createFromMessageDigest() throws NoSuchAlgorithmException {
		final var visitor = JsonSchemaDigestVisitor.of(MessageDigest.getInstance("SHA-256"));

		assertThat(checksum(visitor, StringSchema.instance())).isNotEmpty();
	}

	@Test
	@DisplayName("should return the Base64 encoded digest as the checksum")
	void checksumIsBase64EncodedDigest() {
		final var digest = checksum(JsonSchemaDigestVisitor.of(), StringSchema.instance());

		final var visitor = JsonSchemaDigestVisitor.of();
		StringSchema.instance().accept(visitor);

		assertThat(visitor.checksum()).isEqualTo(Base64.getEncoder().encodeToString(digest));
	}

	@Test
	@DisplayName("should produce different digests for the same schema using different algorithms")
	void differentAlgorithmsProduceDifferentDigests() {
		final JsonSchema schema = StringSchema.builder().title("email").build();

		final var sha256 = checksum(JsonSchemaDigestVisitor.of("SHA-256"), schema);
		final var sha512 = checksum(JsonSchemaDigestVisitor.of("SHA-512"), schema);

		assertThat(sha256).isNotEqualTo(sha512);
	}

	@Test
	@DisplayName("should produce the same digest for equal schemas")
	void deterministic() {
		final JsonSchema schema = StringSchema.builder()
				.title("email")
				.description("User account E-Mail address")
				.minLength(10)
				.maxLength(20)
				.pattern("^[a-zA-Z0-9]+$")
				.build();

		assertThat(checksum(schema)).isEqualTo(checksum(schema));
	}

	@Test
	@DisplayName("should produce different digests for different schemas")
	void sensitiveToChanges() {
		final JsonSchema schema = StringSchema.builder().title("email").build();
		final JsonSchema changed = StringSchema.builder().title("e-mail").build();

		assertThat(checksum(schema)).isNotEqualTo(checksum(changed));
	}

	@Test
	@DisplayName("should ignore object property insertion order")
	void objectPropertyOrderIsIgnored() {
		final Map<String, JsonSchema> ordered = new LinkedHashMap<>();
		ordered.put("name", StringSchema.instance());
		ordered.put("age", IntegerSchema.instance());

		final Map<String, JsonSchema> reversed = new LinkedHashMap<>();
		reversed.put("age", IntegerSchema.instance());
		reversed.put("name", StringSchema.instance());

		final JsonSchema schema = ObjectSchema.builder().properties(ordered).build();
		final JsonSchema reorderedSchema = ObjectSchema.builder().properties(reversed).build();

		assertThat(checksum(schema)).isEqualTo(checksum(reorderedSchema));
	}

	@Test
	@DisplayName("should ignore required property declaration order")
	void requiredPropertyOrderIsIgnored() {
		final JsonSchema schema = ObjectSchema.builder().required("name", "age").build();
		final JsonSchema reordered = ObjectSchema.builder().required("age", "name").build();

		assertThat(checksum(schema)).isEqualTo(checksum(reordered));
	}

	@Test
	@DisplayName("should not confuse adjacent string values without a length prefix")
	void stringsAreLengthPrefixed() {
		final JsonSchema schema = ObjectSchema.builder().required("ab", "c").build();
		final JsonSchema other = ObjectSchema.builder().required("a", "bc").build();

		assertThat(checksum(schema)).isNotEqualTo(checksum(other));
	}

	@Test
	@DisplayName("should treat negative and positive zero as the same numeric value")
	void canonicalizesNegativeZero() {
		final JsonSchema schema = NumberSchema.builder().minimum(0.0).build();
		final JsonSchema negativeZero = NumberSchema.builder().minimum(-0.0).build();

		assertThat(checksum(schema)).isEqualTo(checksum(negativeZero));
	}

	@Test
	@DisplayName("should produce different digests for different numeric bounds")
	void sensitiveToNumericBounds() {
		final JsonSchema schema = NumberSchema.builder().minimum(1.5).build();
		final JsonSchema changed = NumberSchema.builder().minimum(2.5).build();

		assertThat(checksum(schema)).isNotEqualTo(checksum(changed));
	}

	@Test
	@DisplayName("should produce different digests for schemas nested in an array")
	void recursesIntoArrayItems() {
		final JsonSchema schema = ArraySchema.of(StringSchema.instance());
		final JsonSchema changed = ArraySchema.of(IntegerSchema.instance());

		assertThat(checksum(schema)).isNotEqualTo(checksum(changed));
	}

	@Test
	@DisplayName("should produce a non-empty digest for the simplest schemas")
	void neverEmpty() {
		for (final JsonSchema schema : List.of(StringSchema.instance(), IntegerSchema.instance(), ObjectSchema.instance())) {
			assertThat(checksum(schema)).isNotEmpty();
		}
	}

	@Test
	@DisplayName("should generate unique digest for JSON schema describing a complex object")
	void generateSchemaForComplexObject() {
		final var schema = ObjectSchema.builder()
				.title("Test object")
				.description("Test object description")
				.required("username", "age", "active", "status")
				.additionalProperties(StringSchema.instance())
				.propertyNames(StringSchema.instance())
				.property("username", StringSchema.builder()
						.format("email")
						.minLength(12)
						.maxLength(255)
						.example("john.doe@konfigyr.com")
						.example("jane.doe@konfigyr.com")
						.build()
				)
				.property("active", BooleanSchema.builder()
						.description("Is the user active?")
						.defaultValue(true)
						.build()
				)
				.property("age", IntegerSchema.builder()
						.minimum(18L)
						.format("int32")
						.build()
				)
				.property("roles", ArraySchema.builder()
						.items(ObjectSchema.builder()
								.property("name", StringSchema.builder()
										.example("ADMIN")
										.example("USER")
										.example("VISITOR")
										.build()
								)
								.build()
						)
						.build()
				)
				.property("status", StringSchema.builder()
						.enumeration("ACTIVE")
						.enumeration("DISABLED")
						.enumeration("INACTIVE")
						.build()
				)
				.property("height", NumberSchema.builder()
						.minimum(160.00)
						.multipleOf(10.0)
						.deprecated(true)
						.build()
				)
				.property("createdAt", StringSchema.builder()
						.format("date-time")
						.build()
				)
				.property("expiresIn", StringSchema.builder()
						.format("duration")
						.build()
				)
				.build();

		assertThat(checksum(schema))
				.asBase64Encoded()
				.isEqualTo("2qR39F8oBPGehqpBqTKIfdKp1i/cmWqXxjKuUH96Pe0=");
	}

	@MethodSource("defaultSchemaInstances")
	@ParameterizedTest(name = "generate digest for: {0}")
	@DisplayName("should generate unique digests for default JSON schema instances")
	void generateDigestForJsonSchemaInstance(JsonSchema schema, String expected) {
		assertThat(checksum(schema))
				.asBase64Encoded()
				.isEqualTo(expected);
	}

	static Stream<Arguments> defaultSchemaInstances() {
		return Stream.of(
				Arguments.of(BooleanSchema.instance(), "OnqI3txiywOTrr1ndIHMcvTQdV0bTL+Zv4Leo30lZ0U="),
				Arguments.of(NullSchema.instance(), "UHQ0b67r8KpqJsw3i4L1fUTUT5LI+KQj9HolWSPgLeA="),
				Arguments.of(StringSchema.instance(), "QFdOJTtHoUnlUiG9kYKx0h0ETPFsy6SoRvhBf8iOtWc="),
				Arguments.of(IntegerSchema.instance(), "XdHJtGkeaeAJ7MlyXFjfVjxMmgiCutN6r369iIff/ME="),
				Arguments.of(NumberSchema.instance(), "ITXd/grF1SJd3PDO1mErIBPq3hQD5Nngxr7rgXz/tDo="),
				Arguments.of(ArraySchema.instance(), "jUOW5c9T66nyaJen3uaotxOGFp1cHChOSd9RSufj76A="),
				Arguments.of(ObjectSchema.instance(), "+wp6+TnU7clC1BEwJZoQdXjXwvPOU5MscWslg28pHdA=")
		);
	}

	private static byte[] checksum(JsonSchema schema) {
		return checksum(JsonSchemaDigestVisitor.of(), schema);
	}

	private static byte[] checksum(JsonSchemaDigestVisitor visitor, JsonSchema schema) {
		schema.accept(visitor);
		return visitor.digest();
	}

}
