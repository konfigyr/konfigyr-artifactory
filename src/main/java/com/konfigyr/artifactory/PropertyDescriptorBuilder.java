package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

/**
 * Abstract builder class used to create new instances of the {@link PropertyDescriptor} that can be reused
 * for different descriptor implementation.
 *
 * @param <T> the property descriptor implementation that this builder should build
 * @param <B> the builder generic type
 * @author : Vladimir Spasic
 * @see PropertyDescriptor
 * @since 1.0.0
 */
public abstract class PropertyDescriptorBuilder<T extends PropertyDescriptor, B extends PropertyDescriptorBuilder<T, B>> {

	/**
	 * The name of the property.
	 */
	protected @Nullable String name;

	/**
	 * The JSON Schema definition that describes the structure, type, and validation rules.
	 */
	protected @Nullable JsonSchema schema;

	/**
	 * The type name of the expected property value in the language in which this {@link Artifact} was written.
	 * Required; a {@link PropertyDescriptor} without a type name cannot be built.
	 */
	protected @Nullable String typeName;

	/**
	 * Describes the {@link PropertyDescriptor}, what is the actual purpose of this configuration property.
	 */
	protected @Nullable String description;

	/**
	 * The default value which will be used if the property value is not specified.
	 */
	protected @Nullable String defaultValue;

	/**
	 * If the {@link PropertyDescriptor} is deprecated, this field contains the deprecation information.
	 */
	protected @Nullable Deprecation deprecation;

	/**
	 * Creates a new instance of the {@link PropertyDescriptorBuilder}.
	 */
	protected PropertyDescriptorBuilder() {

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
	public B schema(JsonSchema schema) {
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
	 * {@link PropertyDescriptor}.
	 * <p>
	 * Required; {@link #build()} throws {@link IllegalArgumentException} if this value is
	 * {@literal null} or blank.
	 *
	 * @param typeName original type name, can't be {@literal null} or blank
	 * @return builder instance
	 */
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
	public B description(@Nullable String description) {
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
	public B defaultValue(@Nullable String value) {
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
	public B deprecation(String reason) {
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
	public B deprecation(String reason, @Nullable String replacement) {
		return deprecation(new Deprecation(reason, replacement));
	}

	/**
	 * If the {@link PropertyDescriptor} is deprecated, please specify the deprecation
	 * information.
	 *
	 * @param deprecation deprecation information
	 * @return builder instance
	 */
	public B deprecation(@Nullable Deprecation deprecation) {
		this.deprecation = deprecation;
		return myself();
	}

	/**
	 * Validates the properties collected by this builder, throwing an {@link IllegalArgumentException}
	 * when a required property is missing or invalid.
	 *
	 * @throws IllegalArgumentException if the {@code name} or {@code typeName} collected by this
	 *                                  builder is missing or blank, or if the {@code schema} is
	 *                                  {@literal null}.
	 */
	protected void validate() {
		Asserts.notBlank(name, "Property name can not be blank");
		Asserts.nonNull(schema, "Property value schema can not be null");
		Asserts.notBlank(typeName, "Property type name can not be blank");
	}

	/**
	 * Creates the {@link PropertyDescriptor} instance using the properties collected by this builder.
	 * <p>
	 * Called by {@link #build()} only once {@link #validate()} has completed without throwing.
	 *
	 * @return property descriptor, never {@literal null}.
	 */
	protected abstract T instantiate();

	/**
	 * Validates the properties collected by this builder and creates the {@link PropertyDescriptor}
	 * as a result of this builder.
	 *
	 * @return property descriptor, never {@literal null}.
	 * @throws IllegalArgumentException if a required property collected by this builder is
	 *                                  missing or invalid, see {@link #validate()}.
	 */
	public final T build() {
		validate();
		return instantiate();
	}

}
