package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.*;

/**
 * Implementation of a JSON Schema that represents a collection of key-value pairs, where each key
 * (or {@code "property"}) is a string, and the value can be any type. Objects are incredibly powerful,
 * allowing for nested structures. JSON Schema can specify required properties, property dependencies,
 * and even restrict additional properties.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public final class ObjectSchema extends JsonSchema {

	@Serial
	private static final long serialVersionUID = 9081999234831022382L;

	private static final ObjectSchema INSTANCE = builder().build();

	/**
	 * Map of JSON schemas of every property that is present in both the object and the value.
	 */
	private final Map<String, JsonSchema> properties;

	/**
	 * Collection of required property names.
	 */
	private final Collection<String> required;

	/**
	 * JSON schema of property names.
	 */
	private final @Nullable JsonSchema propertyNames;

	/**
	 * JSON schema of additional properties this JSON object schema can have.
	 */
	private final @Nullable JsonSchema additionalProperties;

	/**
	 * Creates a new default {@link ObjectSchema} instance with only the {@code type} property set.
	 *
	 * @return the object schema.
	 */
	public static ObjectSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a {@link Builder} used to create a new {@link ObjectSchema}.
	 *
	 * @return the builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private ObjectSchema(Builder builder) {
		super(builder);
		this.properties = Map.copyOf(builder.properties);
		this.required = List.copyOf(builder.required);
		this.propertyNames = builder.propertyNames;
		this.additionalProperties = builder.additionalProperties;
	}

	/**
	 * Returns the JSON schema of every property that is present in both the object and the value.
	 *
	 * @return the schema of every property contained in the object, never {@literal null} but may be empty.
	 */
	public Map<String, JsonSchema> properties() {
		return properties;
	}

	/**
	 * An object defined by this JSON Schema is considered valid if it contains all property names specified
	 * by this collection.
	 *
	 * @return the required property names, never {@literal null} but may be empty.
	 */
	public Collection<String> required() {
		return required;
	}

	/**
	 * An object defined by this JSON Schema is considered valid if all property names are valid against
	 * the schema defined by the value of this method.
	 *
	 * @return the JSON schema of property names, can be {@literal null}.
	 */
	@Nullable
	public JsonSchema propertyNames() {
		return propertyNames;
	}

	/**
	 * An object defined by this JSON Schema is considered valid if all unchecked properties are valid against
	 * the schema defined by the value of this method.
	 * <p>
	 * Unchecked properties are the properties not checked by the {@link #properties()}.
	 *
	 * @return the JSON schema of additional properties, can be {@literal null}.
	 */
	@Nullable
	public JsonSchema additionalProperties() {
		return additionalProperties;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ObjectSchema that)) return false;
		if (!super.equals(o)) return false;
		return Objects.equals(properties, that.properties)
				&& Objects.equals(required, that.required)
				&& Objects.equals(propertyNames, that.propertyNames)
				&& Objects.equals(additionalProperties, that.additionalProperties);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(properties);
		result = 31 * result + Objects.hashCode(required);
		result = 31 * result + Objects.hashCode(propertyNames);
		result = 31 * result + Objects.hashCode(additionalProperties);
		return result;
	}

	@Override
	protected StringBuilder toStringBuilder() {
		final StringBuilder builder = super.toStringBuilder();

		if (!properties.isEmpty()) {
			builder.append(", properties=").append(properties);
		}
		if (!required.isEmpty()) {
			builder.append(", required=").append(required);
		}
		if (propertyNames != null) {
			builder.append(", propertyNames=").append(propertyNames);
		}
		if (additionalProperties != null) {
			builder.append(", additionalProperties=").append(additionalProperties);
		}

		return builder;
	}

	/**
	 * Builder class for the {@link ObjectSchema} class.
	 */
	public static class Builder extends JsonSchema.Builder<ObjectSchema, Builder> {

		private final Map<String, JsonSchema> properties;
		private final Set<String> required;
		private @Nullable JsonSchema propertyNames;
		private @Nullable JsonSchema additionalProperties;

		private Builder() {
			super(JsonSchemaType.OBJECT);
			this.properties = new LinkedHashMap<>();
			this.required = new LinkedHashSet<>();
		}

		/**
		 * Specify the JSON schema of every property that is present in both the object and the value.
		 *
		 * @param properties the properties schema, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder properties(@Nullable Map<String, JsonSchema> properties) {
			if (properties != null) {
				properties.forEach(this::property);
			}
			return myself();
		}

		/**
		 * Specify the JSON schema of a single property of an object defined by this JSON Schema.
		 *
		 * @param name the property name, can be {@literal null}.
		 * @param property the property schema, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder property(@Nullable String name, @Nullable JsonSchema property) {
			if (name != null && property != null) {
				this.properties.put(name, property);
			}
			return myself();
		}

		/**
		 * Specify the names of the required properties of an object defined by this JSON Schema.
		 *
		 * @param required the required property names, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder required(@Nullable Collection<String> required) {
			if (required != null) {
				this.required.addAll(required);
			}
			return myself();
		}

		/**
		 * Specify the names of the required properties of an object defined by this JSON Schema.
		 *
		 * @param names the required property names, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder required(String @Nullable... names) {
			if (names != null) {
				return required(Arrays.asList(names));
			}
			return myself();
		}

		/**
		 * Specify the JSON schema of property names of an object defined by this JSON Schema.
		 *
		 * @param propertyNames the property names schema, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder propertyNames(@Nullable JsonSchema propertyNames) {
			this.propertyNames = propertyNames;
			return this;
		}

		/**
		 * Specify the JSON schema of additional properties of an object defined by this JSON Schema.
		 *
		 * @param additionalProperties the additional properties schema, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder additionalProperties(@Nullable JsonSchema additionalProperties) {
			this.additionalProperties = additionalProperties;
			return this;
		}

		@Override
		public ObjectSchema build() {
			return new ObjectSchema(this);
		}
	}

}
