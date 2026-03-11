package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;

/**
 * A visitor for the {@link JsonSchema} hierarchy.
 * <p>
 * This interface follows the Visitor Pattern, allowing for double-dispatch  operations across
 * the schema graph. It provides default "no-op" implementations for each node type to allow for
 * selective overriding.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public interface JsonSchemaVisitor {

	/**
	 * The entry point for the visitor.
	 * <p>
	 * Performs a type-check on the schema and dispatches to the specialized visit method.
	 * This implementation uses a pattern-matching approach or type-dispatching logic.
	 *
	 * @param schema The generic schema node to visit.
	 */
	default void visit(JsonSchema schema) {
		switch (schema.type()) {
			case OBJECT: visitObject((ObjectSchema) schema); break;
			case ARRAY: visitArray((ArraySchema) schema); break;
			case STRING: visitString((StringSchema) schema); break;
			case NUMBER: visitNumber((NumberSchema) schema); break;
			case INTEGER: visitInteger((IntegerSchema) schema); break;
			case BOOLEAN: visitBoolean((BooleanSchema) schema); break;
			case NULL: visitNull((NullSchema) schema); break;
		}
	}

	/**
	 * Visits a schema node representing a JSON {@code object}.
	 * <p>
	 * Handles keywords such as {@code properties}, {@code required}, and {@code additionalProperties}.
	 *
	 * @param schema The object-type schema.
	 */
	default void visitObject(ObjectSchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code array}.
	 * <p>
	 * Handles keywords such as {@code items}, {@code prefixItems}, and {@code minItems}.
	 *
	 * @param schema The array-type schema.
	 */
	default void visitArray(ArraySchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code string}.
	 * <p>
	 * Handles keywords such as {@code minLength}, {@code maxLength}, and {@code pattern}.
	 *
	 * @param schema The string-type schema.
	 */
	default void visitString(StringSchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code number}.
	 * <p>
	 * Handles keywords such as {@code minimum}, {@code maximum}, and {@code multipleOf}.
	 *
	 * @param schema The number-type schema.
	 */
	default void visitNumber(NumberSchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code integer}.
	 * <p>
	 * In Draft 2020-12, integers are a subset of numbers but may have specific constraints.
	 *
	 * @param schema The integer-type schema.
	 */
	default void visitInteger(IntegerSchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code boolean}.
	 * <p>
	 * Used for schemas that are strictly {@code { "type": "boolean" }}.
	 *
	 * @param schema The boolean-type schema.
	 */
	default void visitBoolean(BooleanSchema schema) {

	}

	/**
	 * Visits a schema node representing a JSON {@code null}.
	 * <p>
	 * Used for schemas that strictly enforce the {@code null} value.
	 *
	 * @param schema The null-type schema.
	 */
	default void visitNull(NullSchema schema) {

	}

}
