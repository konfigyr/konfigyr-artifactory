package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

class KonfigyrFormatTest {

	@ParameterizedTest(name = "resolve format: {0}")
	@EnumSource(KonfigyrFormat.class)
	@DisplayName("should resolve a known format by its wire value")
	void resolvesKnownFormat(KonfigyrFormat format) {
		assertThat(KonfigyrFormat.fromValue(format.value()))
				.contains(format);
	}

	@Test
	@DisplayName("should not resolve an unknown format")
	void doesNotResolveUnknownFormat() {
		assertThat(KonfigyrFormat.fromValue("does-not-exist"))
				.isEmpty();
	}

	@Test
	@DisplayName("should attach a matching format to a string schema")
	void attachesMatchingFormatToStringSchema() {
		final var schema = StringSchema.builder()
				.format(KonfigyrFormat.UUID)
				.build();

		assertThat(schema.format()).isEqualTo("uuid");
	}

	@Test
	@DisplayName("should clear the format when a null format is given to a string schema")
	void clearsFormatOnStringSchema() {
		final var schema = StringSchema.builder()
				.format((JsonSchemaFormat) null)
				.build();

		assertThat(schema.format()).isNull();
	}

	@Test
	@DisplayName("should reject a non-string format on a string schema")
	void rejectsMismatchedFormatOnStringSchema() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> StringSchema.builder().format(KonfigyrFormat.INT32))
				.withMessageContaining("int32")
				.withMessageContaining("string");
	}

	@Test
	@DisplayName("should attach a matching format to an integer schema")
	void attachesMatchingFormatToIntegerSchema() {
		final var schema = IntegerSchema.builder()
				.format(KonfigyrFormat.INT64)
				.build();

		assertThat(schema.format()).isEqualTo("int64");
	}

	@Test
	@DisplayName("should clear the format when a null format is given to an integer schema")
	void clearsFormatOnIntegerSchema() {
		final var schema = IntegerSchema.builder()
				.format((JsonSchemaFormat) null)
				.build();

		assertThat(schema.format()).isNull();
	}

	@Test
	@DisplayName("should reject a non-integer format on an integer schema")
	void rejectsMismatchedFormatOnIntegerSchema() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> IntegerSchema.builder().format(KonfigyrFormat.UUID))
				.withMessageContaining("uuid")
				.withMessageContaining("integer");
	}

	@Test
	@DisplayName("should attach a matching format to a number schema")
	void attachesMatchingFormatToNumberSchema() {
		final var schema = NumberSchema.builder()
				.format(KonfigyrFormat.DOUBLE)
				.build();

		assertThat(schema.format()).isEqualTo("double");
	}

	@Test
	@DisplayName("should clear the format when a null format is given to a number schema")
	void clearsFormatOnNumberSchema() {
		final var schema = NumberSchema.builder()
				.format((JsonSchemaFormat) null)
				.build();

		assertThat(schema.format()).isNull();
	}

	@Test
	@DisplayName("should reject a non-number format on a number schema")
	void rejectsMismatchedFormatOnNumberSchema() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> NumberSchema.builder().format(KonfigyrFormat.URI))
				.withMessageContaining("uri")
				.withMessageContaining("number");
	}

}
