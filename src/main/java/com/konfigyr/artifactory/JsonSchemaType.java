package com.konfigyr.artifactory;

/**
 * Enumeration that specifies supported JSON Schema data types. Each data type comes with its unique
 * properties and validation requirements. These types are crucial for defining the acceptable dat
 * format and values within a JSON document.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public enum JsonSchemaType {

	/**
	 * Represents a string or text, e.g. {@code "a string"}.
	 */
	STRING,

	/**
	 * Represents an integer or a float, e.g. {@code -5, 10, -5.8, 10.2}
	 */
	NUMBER,

	/**
	 * The integer type is a subtype of {@link #NUMBER} type used for containing only integer numbers.
	 */
	INTEGER,

	/**
	 * Type that represents a key-value map, where the key must be a string and the value
	 * can be any type, e.g. {@code {"key": "value", "other-key": 5}}
	 *
	 */
	OBJECT,

	/**
	 * Array type represents an ordered list of any data types, e.g. {@code [1, -2.5, "some string", null]}
	 */
	ARRAY,

	/**
	 * Represents a boolean value, e.g. {@code true} or {@code false}
	 */
	BOOLEAN,

	/**
	 * Type that indicates that a value is missing, e.g. {@code null}
	 */
	NULL

}
