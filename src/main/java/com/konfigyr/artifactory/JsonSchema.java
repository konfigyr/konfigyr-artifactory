package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a sealed abstract type for creating specific JSON Schema types.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public sealed abstract class JsonSchema implements Serializable permits ArraySchema, BooleanSchema,
		NullSchema, NumericalSchema, ObjectSchema, StringSchema {

	@Serial
	private static final long serialVersionUID = 5425911613580873399L;

	/**
	 * The type of this JSON schema instance.
	 */
	@NonNull
	protected final JsonSchemaType type;

	/**
	 * The title for this JSON schema instance.
	 */
	protected final String title;

	/**
	 * The description for this JSON schema instance.
	 */
	protected final String description;

	/**
	 * The default value for this JSON schema instance.
	 */
	protected final Object defaultValue;

	/**
	 * Is this JSON schema instance deprecated?
	 */
	protected final Boolean deprecated;

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
	protected JsonSchema(@NonNull Builder<?, ?> builder) {
		this.type = builder.type;
		this.title = builder.title;
		this.description = builder.description;
		this.defaultValue = builder.defaultValue;
		this.deprecated = builder.deprecated;
		this.examples = List.copyOf(builder.examples);
		this.enumerations = List.copyOf(builder.enumerations);
	}

	/**
	 * Returns the {@link JsonSchema} of the schema.
	 *
	 * @return the schema type.
	 */
	@NonNull
	public JsonSchemaType type() {
		return type;
	}

	/**
	 * Contains a short description about the schema.
	 *
	 * @return the schema title, can be {@literal null}.
	 */
	@Nullable
	public String title() {
		return title;
	}

	/**
	 * Provides an explanation about the purpose of the instance described by this schema about the schema.
	 *
	 * @return the schema description, can be {@literal null}.
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
	public void accept(@NonNull JsonSchemaVisitor visitor) {
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
		if (examples != null && !examples.isEmpty()) {
			builder.append(", examples=").append(examples);
		}
		if (enumerations != null && !enumerations.isEmpty()) {
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
		public B deprecated(Boolean deprecated) {
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
