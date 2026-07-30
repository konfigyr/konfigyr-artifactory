package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a single node of a JSON Schema (draft 2020-12) tree, as used by
 * {@link PropertyDescriptor#schema()} to describe the structure and validation rules of a
 * configuration property's value.
 * <p>
 * This class holds the keywords common to every JSON Schema type: {@link #title()},
 * {@link #description()}, {@link #defaultValue()}, {@link #deprecated()}, {@link #examples()}, and
 * {@link #enumerations()}. Type-specific keywords, such as {@code minLength} for strings or
 * {@code properties} for objects, are declared by the corresponding subtype: {@link ArraySchema},
 * {@link BooleanSchema}, {@link IntegerSchema}, {@link NullSchema}, {@link NumberSchema},
 * {@link ObjectSchema}, and {@link StringSchema}.
 * <p>
 * Instances are immutable and are constructed through each subtype's own {@code builder()}.
 * Traversal of a schema tree, e.g. for validation or serialization, is done via the
 * {@link JsonSchemaVisitor} accepted by {@link #accept(JsonSchemaVisitor)}.
 *
 * @author Vladimir Spasic
 * @see JsonSchemaVisitor
 * @see PropertyDescriptor
 * @see <a href="https://json-schema.org/draft/2020-12/json-schema-core.html">JSON Schema</a>
 * @since 1.0.0
 */
public sealed abstract class JsonSchema implements Serializable permits ArraySchema, BooleanSchema,
		NullSchema, NumericalSchema, ObjectSchema, StringSchema {

	@Serial
	private static final long serialVersionUID = 5425911613580873399L;

	/**
	 * The type of this JSON schema instance.
	 */
	protected final JsonSchemaType type;

	/**
	 * The title for this JSON schema instance.
	 */
	protected final @Nullable String title;

	/**
	 * The description for this JSON schema instance.
	 */
	protected final @Nullable String description;

	/**
	 * The default value for this JSON schema instance.
	 */
	protected final @Nullable Object defaultValue;

	/**
	 * Is this JSON schema instance deprecated?
	 */
	protected final @Nullable Boolean deprecated;

	/**
	 * Collection of examples for this JSON schema instance.
	 */
	private final Collection<String> examples;

	/**
	 * Collection of enum values for this JSON schema instance.
	 */
	private final Collection<String> enumerations;

	/**
	 * Creates a new instance of the {@link JsonSchema} class using the values from the given builder.
	 *
	 * @param builder the builder instance, never {@literal null}.
	 */
	protected JsonSchema(Builder<?, ?> builder) {
		this.type = builder.type;
		this.title = builder.title;
		this.description = builder.description;
		this.defaultValue = builder.defaultValue;
		this.deprecated = builder.deprecated;
		this.examples = List.copyOf(builder.examples);
		this.enumerations = List.copyOf(builder.enumerations);
	}

	/**
	 * Returns the {@link JsonSchemaType} of this schema, identifying which JSON Schema data type
	 * it represents.
	 *
	 * @return the schema type, never {@literal null}.
	 */
	public JsonSchemaType type() {
		return type;
	}

	/**
	 * Returns a short, human-readable label for this schema (the JSON Schema {@code title} keyword),
	 * intended for display next to the value it describes.
	 * <p>
	 * Distinct from {@link #description()}, which provides a longer explanation of the value's
	 * purpose rather than a short label.
	 *
	 * @return the schema title, can be {@literal null}.
	 * @see #description()
	 */
	@Nullable
	public String title() {
		return title;
	}

	/**
	 * Returns a longer, human-readable explanation of the purpose or intended usage of the value
	 * described by this schema (the JSON Schema {@code description} keyword).
	 * <p>
	 * Distinct from {@link #title()}, which provides a short label rather than a full explanation.
	 *
	 * @return the schema description, can be {@literal null}.
	 * @see #title()
	 */
	@Nullable
	public String description() {
		return description;
	}

	/**
	 * Returns a default value associated with this schema.
	 *
	 * @return the default value, can be {@literal null}.
	 */
	@Nullable
	public Object defaultValue() {
		return defaultValue;
	}

	/**
	 * Checks if the value behind this schema is considered as deprecated.
	 *
	 * @return {@code true} if this schema is deprecated, {@code false} otherwise.
	 */
	public boolean deprecated() {
		return Boolean.TRUE.equals(deprecated);
	}

	/**
	 * Returns a collection of example objects that are used to better describe the value behind this schema.
	 *
	 * @return the example objects, never {@literal null} but may be empty.
	 */
	public Collection<String> examples() {
		return examples;
	}

	/**
	 * Returns a collection of objects used to restrict a value to a fixed set of values.
	 *
	 * @return the enum objects, never {@literal null} but may be empty.
	 */
	public Collection<String> enumerations() {
		return enumerations;
	}

	/**
	 * Accepts a {@link JsonSchemaVisitor} to perform operations on this schema node.
	 * <p>
	 * This method facilitates the Visitor Pattern, allowing external logic (like validators or serializers) to
	 * traverse the schema graph without modifying the structural classes.
	 *
	 * @param visitor The visitor implementation to execute, never {@literal null}.
	 */
	public void accept(JsonSchemaVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof JsonSchema that)) return false;
		return type == that.type
				&& Objects.equals(title, that.title)
				&& Objects.equals(description, that.description)
				&& Objects.equals(defaultValue, that.defaultValue)
				&& Objects.equals(deprecated, that.deprecated)
				&& Objects.equals(examples, that.examples)
				&& Objects.equals(enumerations, that.enumerations);
	}

	@Override
	public int hashCode() {
		int result = type.hashCode();
		result = 31 * result + Objects.hashCode(title);
		result = 31 * result + Objects.hashCode(description);
		result = 31 * result + Objects.hashCode(defaultValue);
		result = 31 * result + Objects.hashCode(deprecated);
		result = 31 * result + Objects.hashCode(examples);
		result = 31 * result + Objects.hashCode(enumerations);
		return result;
	}

	@Override
	public final String toString() {
		return "JsonSchema(" + toStringBuilder() + ")";
	}

	/**
	 * Method that creates a {@link StringBuilder} with the properties of the schema that is used
	 * to create the {@link String} representation of the schema.
	 *
	 * @return the string builder instance.
	 */
	protected StringBuilder toStringBuilder() {
		final StringBuilder builder = new StringBuilder("type='")
				.append(type.name().toLowerCase())
				.append('\'');

		if (title != null) {
			builder.append(", title='").append(title).append('\'');
		}
		if (description != null) {
			builder.append(", description='").append(description).append('\'');
		}
		if (defaultValue != null) {
			builder.append(", default='").append(defaultValue).append('\'');
		}
		if (deprecated != null) {
			builder.append(", deprecated=").append(deprecated);
		}
		if (!examples.isEmpty()) {
			builder.append(", examples=").append(examples);
		}
		if (!enumerations.isEmpty()) {
			builder.append(", enum=").append(enumerations);
		}
		return builder;
	}

	/**
	 * Abstract builder class for the builder classes of {@link JsonSchema} subclasses. This builder is
	 * used to load the generic properties of all types of schemas like {@code title} or {@code description}.
	 *
	 * @param <T> the type of the schema being built by the builder subclass.
	 * @param <B> the type of the builder subclass.
	 */
	@NullMarked
	public static abstract class Builder<T extends JsonSchema, B extends Builder<T, B>> {

		/**
		 * The type of this JSON schema instance.
		 */
		protected final JsonSchemaType type;

		/**
		 * The title for this JSON schema instance.
		 */
		protected @Nullable String title;

		/**
		 * The description for this JSON schema instance.
		 */
		protected @Nullable String description;

		/**
		 * The default value for this JSON schema instance.
		 */
		protected @Nullable Object defaultValue;

		/**
		 * Is this JSON schema instance deprecated?
		 */
		protected @Nullable Boolean deprecated;

		/**
		 * Collection of examples for this JSON schema instance.
		 */
		private final Collection<String> examples;

		/**
		 * Collection of enum values for this JSON schema instance.
		 */
		private final Collection<String> enumerations;

		/**
		 * Creates a new builder instance for the given schema type.
		 *
		 * @param type the schema type to be built, must not be {@literal null}.
		 */
		protected Builder(JsonSchemaType type) {
			this.type = type;
			this.examples = new ArrayList<>();
			this.enumerations = new TreeSet<>();
		}

		/**
		 * Utility method that returns the type-self builder return value that is used
		 * to chain builder methods.
		 *
		 * @return the type-self builder return value, never {@literal null}.
		 */
		@SuppressWarnings("unchecked")
		protected B myself() {
			return (B) this;
		}

		/**
		 * Resolves the wire-level value of the given {@link JsonSchemaFormat}, validating that it is
		 * valid for the given schema type.
		 *
		 * @param format the format to resolve, can be {@literal null}.
		 * @param type the schema type the format must be valid for, never {@literal null}.
		 * @return the format's wire-level value, or {@literal null} if the given format was {@literal null}.
		 * @throws IllegalArgumentException if the given format is not valid for the given schema type.
		 */
		protected static @Nullable String requireFormat(@Nullable JsonSchemaFormat format, JsonSchemaType type) {
			if (format == null) {
				return null;
			}
			if (format.type() != type) {
				throw new IllegalArgumentException(
						"Format '%s' is not valid for the '%s' schema type, it is only valid for the '%s' schema type"
								.formatted(format.value(), type.name().toLowerCase(), format.type().name().toLowerCase())
				);
			}
			return format.value();
		}

		/**
		 * Specify a short description of the schema that is being built by this builder.
		 *
		 * @param title the schema title, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B title(@Nullable String title) {
			this.title = title;
			return myself();
		}

		/**
		 * Specify an explanation about the purpose of the schema that is being built by this builder.
		 *
		 * @param description the schema description, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B description(@Nullable String description) {
			this.description = description;
			return myself();
		}

		/**
		 * Supply a default value for the schema that is being built by this builder.
		 *
		 * @param defaultValue the default value, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B defaultValue(@Nullable Object defaultValue) {
			this.defaultValue = defaultValue;
			return myself();
		}

		/**
		 * Specify if the schema that is being built by this builder is deprecated.
		 *
		 * @param deprecated the deprecated flag, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B deprecated(@Nullable Boolean deprecated) {
			this.deprecated = deprecated;
			return myself();
		}

		/**
		 * Specify the examples of an object defined by this JSON Schema.
		 *
		 * @param examples the examples collection, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B examples(@Nullable Collection<String> examples) {
			if (examples != null) {
				examples.forEach(this::example);
			}
			return myself();
		}

		/**
		 * Specify the example of an object defined by this JSON Schema.
		 *
		 * @param example the examples object, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B example(@Nullable String example) {
			if (example != null) {
				this.examples.add(example);
			}
			return myself();
		}

		/**
		 * Specify the collection enum object values that define acceptable values for this JSON Schema.
		 *
		 * @param enumerations the enum collection, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B enumerations(@Nullable Collection<String> enumerations) {
			if (enumerations != null) {
				enumerations.forEach(this::enumeration);
			}
			return myself();
		}

		/**
		 * Specify an enum object value that defines an acceptable value for this JSON Schema.
		 *
		 * @param enumeration the enum object, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B enumeration(@Nullable String enumeration) {
			if (enumeration != null) {
				this.enumerations.add(enumeration);
			}
			return myself();
		}

		/**
		 * Creates a {@link JsonSchema} instance based on the properties set on this builder.
		 *
		 * @return the JSON schema instance, never {@literal null}
		 */
		public abstract T build();

	}

}
