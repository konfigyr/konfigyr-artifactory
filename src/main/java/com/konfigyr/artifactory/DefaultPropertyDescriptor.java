package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * Default implementation of the {@link PropertyDescriptor} interface.
 *
 * @param name         unique property name, can't be {@literal null}.
 * @param schema       JSON Schema definition that describes the property value, can't be {@literal null}.
 * @param typeName     original type name of the property, can't be {@literal null}.
 * @param description  description of the configuration property, may be {@literal null}.
 * @param defaultValue default value of the configuration property, may be {@literal null}.
 * @param deprecation  deprecation information, may be {@literal null}.
 * @author : Vladimir Spasic
 * @since 1.0.0
 **/
public record DefaultPropertyDescriptor(
		String name,
		JsonSchema schema,
		String typeName,
		@Nullable String description,
		@Nullable String defaultValue,
		@Nullable Deprecation deprecation
) implements PropertyDescriptor {

	@Serial
	private static final long serialVersionUID = 8577934242497894399L;

	/**
	 * Validates this {@link PropertyDescriptor}, mirroring the checks performed by
	 * {@link PropertyDescriptorBuilder#validate()}, so that the invariant holds regardless of
	 * whether this record is constructed via the {@link Builder} or directly.
	 */
	public DefaultPropertyDescriptor {
		Asserts.notBlank(name, "Property name can not be blank");
		Asserts.nonNull(schema, "Property value schema can not be null");
		Asserts.notBlank(typeName, "Property type name can not be blank");
	}

	/**
	 * Builder class used to create new instances of the {@link DefaultPropertyDescriptor}.
	 */
	public static final class Builder extends PropertyDescriptorBuilder<DefaultPropertyDescriptor, Builder> {

		Builder() {
			// can only be used by types within this package
		}

		/**
		 * Creates the {@link DefaultPropertyDescriptor} as a result of this builder.
		 *
		 * @return property descriptor, never {@literal null}.
		 */
		@Override
		protected DefaultPropertyDescriptor instantiate() {
			return new DefaultPropertyDescriptor(name, schema, typeName, description, defaultValue, deprecation);
		}

	}

}
