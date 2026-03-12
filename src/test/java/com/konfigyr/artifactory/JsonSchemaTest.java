package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class JsonSchemaTest {

	@Test
	@DisplayName("should create a detailed string schema instance")
	void createDetailedStringSchema() {
		final var schema = StringSchema.builder()
				.title("email")
				.description("User account E-Mail address")
				.minLength(10)
				.maxLength(20)
				.pattern("^[a-zA-Z0-9]+$")
				.format("email")
				.defaultValue("john.doe@konfigyr.com")
				.enumerations(List.of("john.doe@konfigyr.com", "jane.doe@konfigyr.com"))
				.examples(List.of("john.doe@konfigyr.com"))
				.build();

		assertThat(schema)
				.returns(JsonSchemaType.STRING, JsonSchema::type)
				.returns("email", JsonSchema::title)
				.returns("User account E-Mail address", JsonSchema::description)
				.returns(10, StringSchema::minLength)
				.returns(20, StringSchema::maxLength)
				.returns("^[a-zA-Z0-9]+$", StringSchema::pattern)
				.returns("email", StringSchema::format)
				.returns("john.doe@konfigyr.com", JsonSchema::defaultValue);

		assertThat(schema.enumerations())
				.containsExactly("jane.doe@konfigyr.com", "john.doe@konfigyr.com");

		assertThat(schema.examples())
				.containsExactly("john.doe@konfigyr.com");

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='string', title='%s', description='%s', " +
						"default='%s', examples=%s, enum=%s, format='%s', pattern='%s', minLength=%d, maxLength=%d)",
						schema.title(), schema.description(), schema.defaultValue(), schema.examples(),
						schema.enumerations(), schema.format(), schema.pattern(), schema.minLength(), schema.maxLength()
				);
	}

	@Test
	@DisplayName("should create default schema instance")
	void createDefaultSchemas() {
		assertThat(ArraySchema.instance())
				.isEqualTo(ArraySchema.builder().build())
				.hasSameHashCodeAs(ArraySchema.builder().build());

		assertThat(BooleanSchema.instance())
				.isEqualTo(BooleanSchema.builder().build())
				.hasSameHashCodeAs(BooleanSchema.builder().build());

		assertThat(IntegerSchema.instance())
				.isEqualTo(IntegerSchema.builder().build())
				.hasSameHashCodeAs(IntegerSchema.builder().build());

		assertThat(NumberSchema.instance())
				.isEqualTo(NumberSchema.builder().build())
				.hasSameHashCodeAs(NumberSchema.builder().build());

		assertThat(NullSchema.instance())
				.isEqualTo(NullSchema.builder().build())
				.hasSameHashCodeAs(NullSchema.builder().build());

		assertThat(ObjectSchema.instance())
				.isEqualTo(ObjectSchema.builder().build())
				.hasSameHashCodeAs(ObjectSchema.builder().build());

		assertThat(StringSchema.instance())
				.isEqualTo(StringSchema.builder().build())
				.hasSameHashCodeAs(StringSchema.builder().build());
	}

	@MethodSource("visitingSchemas")
	@ParameterizedTest(name = "visit schema: {0}")
	@DisplayName("should visit schema instance based on the type")
	void createVisitSchemas(JsonSchema schema) {
		final var visitor = new TestVisitor();

		assertThatNoException().isThrownBy(() -> schema.accept(visitor));

		assertThat(visitor.types)
				.hasSize(1)
				.containsEntry(schema.type(), 1);
	}

	static Stream<Arguments> visitingSchemas() {
		return Stream.of(
				Arguments.of(ArraySchema.instance()),
				Arguments.of(BooleanSchema.instance()),
				Arguments.of(IntegerSchema.instance()),
				Arguments.of(NumberSchema.instance()),
				Arguments.of(NullSchema.instance()),
				Arguments.of(ObjectSchema.instance()),
				Arguments.of(StringSchema.instance())
		);
	}


	@Test
	@DisplayName("should create an integer schema instance")
	void createIntegerSchema() {
		final var schema = IntegerSchema.builder()
				.title("age")
				.minimum(18L)
				.format("int32")
				.exclusiveMinimum(true)
				.build();

		assertThat(schema)
				.returns(JsonSchemaType.INTEGER, JsonSchema::type);

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='integer', title='%s', format='%s', minimum=%s, exclusiveMinimum=%s)",
						schema.title(), schema.format(), schema.minimum(), schema.exclusiveMinimum());
	}

	@Test
	@DisplayName("should create a number schema instance")
	void createNumberSchema() {
		final var schema = NumberSchema.builder()
				.title("weight")
				.minimum(0.200)
				.maximum(100.00)
				.format("kilograms")
				.multipleOf(0.1)
				.exclusiveMaximum(true)
				.build();

		assertThat(schema)
				.returns(JsonSchemaType.NUMBER, JsonSchema::type);

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='number', title='%s', format='%s', minimum=%s, maximum=%s, multipleOf=%s, exclusiveMaximum=%s)",
						schema.title(), schema.format(), schema.minimum(), schema.maximum(), schema.multipleOf(), schema.exclusiveMaximum());
	}

	@Test
	@DisplayName("should create an array schema instance")
	void createArraySchema() {
		final var schema = ArraySchema.builder()
				.items(StringSchema.builder()
						.title("duration")
						.format("duration")
						.example("1d")
						.build()
				)
				.uniqueItems(true)
				.minItems(1)
				.maxItems(10)
				.build();

		assertThat(schema)
				.returns(JsonSchemaType.ARRAY, JsonSchema::type);

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='array', items=%s, minItems=%s, maxItems=%s, uniqueItems=%s)",
						schema.items(), schema.minItems(), schema.maxItems(), schema.uniqueItems());
	}

	@Test
	@DisplayName("should create an array schema instance from items schema")
	void createArraySchemaFromItems() {
		final var item = NumberSchema.builder()
				.title("height")
				.format("cm")
				.example("182")
				.build();

		assertThat(ArraySchema.of(item))
				.returns(JsonSchemaType.ARRAY, JsonSchema::type)
				.returns(item, ArraySchema::items)
				.returns(null, JsonSchema::title)
				.returns(null, JsonSchema::description)
				.returns(Collections.emptyList(), JsonSchema::enumerations)
				.returns(Collections.emptyList(), JsonSchema::examples)
				.returns(null, JsonSchema::defaultValue)
				.returns(false, JsonSchema::deprecated)
				.returns(null, ArraySchema::minItems)
				.returns(null, ArraySchema::maxItems)
				.returns(false, ArraySchema::uniqueItems);
	}

	@Test
	@DisplayName("should create an object schema instance")
	void createObjectSchema() {
		final var schema = ObjectSchema.builder()
				.properties(
						Map.of(
								"username", StringSchema.builder()
										.format("email")
										.build(),
								"name", StringSchema.builder()
										.deprecated(true)
										.build(),
								"status", StringSchema.builder()
										.enumeration("ACTIVE")
										.enumeration("DISABLED")
										.build(),
								"age", IntegerSchema.builder()
										.defaultValue(18)
										.build()
						)
				)
				.required("username", "status")
				.propertyNames(
						StringSchema.builder()
								.pattern("[a-zA-Z0-9]+")
								.build()
				)
				.additionalProperties(
						StringSchema.builder()
								.example("id")
								.build()
				)
				.build();

		assertThat(schema)
				.returns(JsonSchemaType.OBJECT, JsonSchema::type);

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='object', properties=%s, required=%s, propertyNames=%s, additionalProperties=%s)",
						schema.properties(), schema.required(), schema.propertyNames(), schema.additionalProperties());
	}

	@Test
	@DisplayName("should create a boolean schema instance")
	void createBooleanSchema() {
		final var schema = BooleanSchema.builder().build();

		assertThat(schema)
				.returns(JsonSchemaType.BOOLEAN, JsonSchema::type)
				.isEqualTo(BooleanSchema.instance())
				.hasSameHashCodeAs(BooleanSchema.instance());

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='boolean')");
	}

	@Test
	@DisplayName("should create a null schema instance")
	void createNullSchema() {
		final var schema = NullSchema.builder().build();

		assertThat(schema)
				.returns(JsonSchemaType.NULL, JsonSchema::type)
				.isEqualTo(NullSchema.instance())
				.hasSameHashCodeAs(NullSchema.instance());

		assertThat(schema.toString())
				.isEqualTo("JsonSchema(type='null')");
	}

	@NullMarked
	private static final class TestVisitor implements JsonSchemaVisitor {

		final Map<JsonSchemaType, Integer> types = new LinkedHashMap<>();

		@Override
		public void visitObject(ObjectSchema schema) {
			JsonSchemaVisitor.super.visitObject(schema);
			types.compute(JsonSchemaType.OBJECT, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitArray(ArraySchema schema) {
			JsonSchemaVisitor.super.visitArray(schema);
			types.compute(JsonSchemaType.ARRAY, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitString(StringSchema schema) {
			JsonSchemaVisitor.super.visitString(schema);
			types.compute(JsonSchemaType.STRING, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitNumber(NumberSchema schema) {
			JsonSchemaVisitor.super.visitNumber(schema);
			types.compute(JsonSchemaType.NUMBER, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitInteger(IntegerSchema schema) {
			JsonSchemaVisitor.super.visitInteger(schema);
			types.compute(JsonSchemaType.INTEGER, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitBoolean(BooleanSchema schema) {
			JsonSchemaVisitor.super.visitBoolean(schema);
			types.compute(JsonSchemaType.BOOLEAN, (k, v) -> v == null ? 1 : v + 1);
		}

		@Override
		public void visitNull(NullSchema schema) {
			JsonSchemaVisitor.super.visitNull(schema);
			types.compute(JsonSchemaType.NULL, (k, v) -> v == null ? 1 : v + 1);
		}
	}

}
