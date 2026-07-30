package com.konfigyr.artifactory;

/**
 * Represents a semantic format that can be assigned to the {@code format} keyword of a
 * {@link StringSchema}, {@link IntegerSchema}, or {@link NumberSchema}.
 * <p>
 * Implementations tie a wire-level format value, e.g. {@code "date-time"} or {@code "int32"}, to the
 * {@link JsonSchemaType} it is valid on, giving builders a compile-time link between a format and the
 * schema it can be attached to. This interface is a construction-time convenience only: the
 * {@code format} keyword is still serialized and deserialized as a plain {@link String}, and reading a
 * schema document back never yields a {@link JsonSchemaFormat} instance.
 * <p>
 * This interface is intentionally left open, allowing other modules to define their own formats for
 * values that the SDK does not know about.
 *
 * @author Vladimir Spasic
 * @see KonfigyrFormat
 * @since 1.0.0
 */
public interface JsonSchemaFormat {

	/**
	 * Returns the wire-level value of this format, as it would appear in the {@code format} keyword
	 * of a JSON Schema document, e.g. {@code "date-time"} or {@code "int32"}.
	 *
	 * @return the format value, never {@literal null}.
	 */
	String value();

	/**
	 * Returns the {@link JsonSchemaType} that this format is valid on.
	 *
	 * @return the schema type, never {@literal null}.
	 */
	JsonSchemaType type();

}
