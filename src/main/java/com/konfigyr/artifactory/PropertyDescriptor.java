package com.konfigyr.artifactory;

import io.avaje.jsonb.Json;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Record that defines the description of a configuration property of an {@link Artifact}.
 * <p>
 * The descriptors are associated to an {@link Artifact} through {@link Release releases}.
 * Each release can introduce changes to a {@link PropertyDescriptor}, new properties can
 * be added, existing ones can be updated, deprecated or removed. Because of these changes
 * it is important to know which configuration properties can be used for a specific
 * version of the artifact.
 *
 * @param name unique property name, can't be {@literal null}
 * @param type property type descriptor, can't be {@literal null}
 * @param dataType data type for the property value, can't be {@literal null}
 * @param typeName original type name of the property, can be {@literal null}
 * @param description description of the configuration property, can be {@literal null}
 * @param defaultValue default value of the configuration property, can be {@literal null}
 * @param hints list of possible configuration property values, can't be {@literal null}
 * @param deprecation deprecation information, if any is available
 * @author : Vladimir Spasic
 * @since : 16.03.23, Thu
 **/
@Json
public record PropertyDescriptor(@Nonnull @Json.Property("name") String name,
		@Nonnull @Json.Property("type") PropertyType type, @Nonnull @Json.Property("data_type") DataType dataType,
		@Nullable @Json.Property("type_name") String typeName,
		@Nullable @Json.Property("description") String description,
		@Nullable @Json.Property("default_value") String defaultValue,
		@Nonnull @Json.Property("hints") List<String> hints,
		@Nullable @Json.Property("deprecation") Deprecation deprecation)
		implements
			Serializable,
			Comparable<PropertyDescriptor> {

	@Serial
	private static final long serialVersionUID = 8577934242497894399L;

	@Override
	public int compareTo(@Nonnull PropertyDescriptor o) {
		return Comparator.comparing(PropertyDescriptor::name).compare(this, o);
	}

	/**
	 * Creates a new instance of the {@link PropertyDescriptor.Builder} used to create a
	 * new instance of the {@link PropertyDescriptor} using the fluent builder API.
	 * @return property builder, never {@literal null}
	 */
	public static @Nonnull Builder builder() {
		return new Builder();
	}

	/**
	 * Builder class used to create new instances of the {@link PropertyDescriptor}.
	 */
	public static class Builder {

		private String name;

		private PropertyType type = PropertyType.STRING;

		private DataType dataType = DataType.ATOMIC;

		private String typeName;

		private String description;

		private String defaultValue;

		private Deprecation deprecation;

		private final List<String> hints = new ArrayList<>();

		private Builder() {
		}

		/**
		 * Specify the full name of the property. Konfigyr Artifactory treats this value
		 * as case-sensitive, it does not transform the property names, nor it specifies
		 * any specific conventions. The conventions depend on the {@link Artifact} for
		 * which this property is defined for.
		 * <p>
		 * The name of the property is considered unique per {@link Artifact}, any
		 * duplicates would be rejected.
		 * @param name property name
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Define the configuration {@link PropertyType} that this
		 * {@link PropertyDescriptor} should have.
		 * <p>
		 * This can be used to provide helpful type information, or hints, either to the
		 * user when entering values or by the value converters during serialization.
		 * <p>
		 * If nothing is specified, the {@link PropertyType#STRING} would be used as a
		 * default.
		 * @param type property type
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder type(PropertyType type) {
			this.type = type;
			return this;
		}

		/**
		 * What should be the {@link DataType} of the value for this
		 * {@link PropertyDescriptor}.
		 * <p>
		 * If nothing is specified, the {@link DataType#ATOMIC} would be used as a
		 * default.
		 * @param type value data type
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder dataType(DataType type) {
			this.dataType = type;
			return this;
		}

		/**
		 * Defines what was the type name of the expected property value in the language
		 * in which this {@link Artifact} was written.
		 * <p>
		 * For instance if the language is Java and type is a string, it should be
		 * {@code java.lang.String}.
		 * <p>
		 * This attribute is purely used for informational purposes with intention of
		 * helping the user decide how to define the value for this
		 * <p>
		 * May be omitted if no value type information is available.
		 * {@link PropertyDescriptor}.
		 * @param typeName original type name
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder typeName(String typeName) {
			this.typeName = typeName;
			return this;
		}

		/**
		 * Describe the {@link PropertyDescriptor}, what is the actual purpose of this
		 * configuration property. It is recommended that descriptions are a short
		 * paragraphs, providing a concise summary.
		 * <p>
		 * This description should be displayed to users when defining the value for the
		 * configuration property for the {@link Artifact}
		 * <p>
		 * May be omitted if no description is available.
		 * @param description configuration property description
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Specify the default value which will be used if the property is not specified.
		 * <p>
		 * May be omitted if the default value is not known or set.
		 * @param value default value
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder defaultValue(String value) {
			this.defaultValue = value;
			return this;
		}

		/**
		 * Add a hint to this {@link PropertyDescriptor}. Usually hint is a suggested
		 * value that this configuration property can have.
		 * @param hint value hint
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder hint(String hint) {
			if (hint != null && !hint.isBlank()) {
				this.hints.add(hint);
			}
			return this;
		}

		/**
		 * Add hints to this {@link PropertyDescriptor}. Usually hint is a suggested value
		 * that this configuration property can have.
		 * @param hints value hints
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder hints(String... hints) {
			for (String hint : hints) {
				hint(hint);
			}
			return this;
		}

		/**
		 * Add hints to this {@link PropertyDescriptor}. Usually hint is a suggested value
		 * that this configuration property can have.
		 * @param hints value hints
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder hints(Iterable<String> hints) {
			for (String hint : hints) {
				hint(hint);
			}
			return this;
		}

		/**
		 * If the {@link PropertyDescriptor} is deprecated, please specify the reason why
		 * was it deprecated.
		 * @param reason deprecation reason
		 * @return builder instance
		 */
		public @Nonnull Builder deprecation(String reason) {
			return deprecation(reason, null);
		}

		/**
		 * If the {@link PropertyDescriptor} is deprecated, please specify the reason why
		 * was it deprecated and which property should be used as a replacement, if any.
		 * @param reason deprecation reason
		 * @param replacement name of the property that replaces it
		 * @return builder instance
		 */
		public @Nonnull Builder deprecation(String reason, String replacement) {
			return deprecation(new Deprecation(reason, replacement));
		}

		/**
		 * If the {@link PropertyDescriptor} is deprecated, please specify the deprecation
		 * information.
		 * @param deprecation deprecation information
		 * @return builder instance
		 */
		@Nonnull
		public PropertyDescriptor.Builder deprecation(Deprecation deprecation) {
			this.deprecation = deprecation;
			return this;
		}

		/**
		 * Creates the {@link PropertyDescriptor} as a result of this builder.
		 * @return property descriptor, never {@literal null}.
		 */
		@Nonnull
		public PropertyDescriptor build() {
			if (name == null || name.isBlank()) {
				throw new IllegalArgumentException("Property name can not be blank");
			}

			return new PropertyDescriptor(name, type, dataType, typeName, description, defaultValue, hints,
					deprecation);
		}

	}

}
