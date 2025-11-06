package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * Default implementation of the {@link PropertyDescriptor} interface.
 *
 * @param name         unique property name, can't be {@literal null}.
 * @param schema       JSON Schema definition that describes the property value, can't be {@literal null}.
 * @param typeName     original type name of the property, may be {@literal null}.
 * @param description  description of the configuration property, may be {@literal null}.
 * @param defaultValue default value of the configuration property, may be {@literal null}.
 * @param deprecation  deprecation information, may be {@literal null}.
 * @author : Vladimir Spasic
 * @since : 16.03.23, Thu
 **/
public record DefaultPropertyDescriptor(
		@NonNull String name,
		@NonNull String schema,
		@Nullable String typeName,
		@Nullable String description,
		@Nullable String defaultValue,
		@Nullable Deprecation deprecation
) implements PropertyDescriptor {

	@Serial
	private static final long serialVersionUID = 8577934242497894399L;

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
		@NonNull
		@Override
		public DefaultPropertyDescriptor build() {
			if (name == null || name.isBlank()) {
				throw new IllegalArgumentException("Property name can not be blank");
			}
			if (schema == null || schema.isBlank()) {
				throw new IllegalArgumentException("Property value schema can not be blank");
			}
			if (typeName == null || typeName.isBlank()) {
				throw new IllegalArgumentException("Property type name can not be blank");
			}

			return new DefaultPropertyDescriptor(name, schema, typeName, description, defaultValue, deprecation);
		}

	}

}
