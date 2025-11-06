package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;

/**
 * Abstract builder class used to create new instances of the {@link PropertyDescriptor} that can be reused
 * for different descriptor implementation.
 *
 * @param <T> the property descriptor implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see PropertyDescriptor
 * @since : 23.10.25, Thu
 */
public abstract class PropertyDescriptorBuilder<T extends PropertyDescriptor, B extends PropertyDescriptorBuilder<T, B>> {

	protected String name;
	protected String schema;
	protected String typeName;
	protected String description;
	protected String defaultValue;
	protected Deprecation deprecation;

	protected PropertyDescriptorBuilder() {

	}

	@SuppressWarnings("unchecked")
	protected B myself() {
		return (B) this;
	}

	/**
	 * Specify the full name of the property. Konfigyr Artifactory treats this value
	 * as case-sensitive, it does not transform the property names, nor it specifies
	 * any specific conventions. The conventions depend on the {@link Artifact} for
	 * which this property is defined for.
	 * <p>
	 * The name of the property is considered unique per {@link Artifact}, any
	 * duplicates would be rejected.
	 *
	 * @param name property name
	 * @return builder instance
	 */
	@NonNull
	public B name(String name) {
		this.name = name;
		return myself();
	}

	/**
	 * What should be the JSON Schema definition that describes the structure, type, and validation rules
	 * of the value for this {@link PropertyDescriptor}.
	 *
	 * @param schema JSON Schema
	 * @return builder instance
	 */
	@NonNull
	public B schema(String schema) {
		this.schema = schema;
		return myself();
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
	 *
	 * @param typeName original type name
	 * @return builder instance
	 */
	@NonNull
	public B typeName(String typeName) {
		this.typeName = typeName;
		return myself();
	}

	/**
	 * Describe the {@link PropertyDescriptor}, what is the actual purpose of this configuration property.
	 * It is recommended that descriptions are a short paragraph, providing a concise summary.
	 * <p>
	 * This description should be displayed to users when defining the value for the
	 * configuration property for the {@link Artifact}.
	 * <p>
	 * May be omitted if no description is available.
	 *
	 * @param description configuration property description
	 * @return builder instance
	 */
	@NonNull
	public B description(String description) {
		this.description = description;
		return myself();
	}

	/**
	 * Specify the default value which will be used if the property is not specified.
	 * <p>
	 * May be omitted if the default value is not known or set.
	 *
	 * @param value default value
	 * @return builder instance
	 */
	@NonNull
	public B defaultValue(String value) {
		this.defaultValue = value;
		return myself();
	}

	/**
	 * If the {@link PropertyDescriptor} is deprecated, please specify the reason why
	 * was it deprecated.
	 *
	 * @param reason deprecation reason
	 * @return builder instance
	 */
	public @NonNull B deprecation(String reason) {
		return deprecation(reason, null);
	}

	/**
	 * If the {@link PropertyDescriptor} is deprecated, please specify the reason why
	 * was it deprecated and which property should be used as a replacement, if any.
	 *
	 * @param reason      deprecation reason
	 * @param replacement name of the property that replaces it
	 * @return builder instance
	 */
	public @NonNull B deprecation(String reason, String replacement) {
		return deprecation(new Deprecation(reason, replacement));
	}

	/**
	 * If the {@link PropertyDescriptor} is deprecated, please specify the deprecation
	 * information.
	 *
	 * @param deprecation deprecation information
	 * @return builder instance
	 */
	@NonNull
	public B deprecation(Deprecation deprecation) {
		this.deprecation = deprecation;
		return myself();
	}

	/**
	 * Creates the {@link PropertyDescriptor} as a result of this builder.
	 *
	 * @return property descriptor, never {@literal null}.
	 */
	@NonNull
	public abstract T build();

}
