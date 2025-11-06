package com.konfigyr.artifactory;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Describes the metadata of a configuration property discovered within an {@link Artifact}.
 * <p>
 * A {@code PropertyDescriptor} represents the structural and semantic definition of a configuration property
 * that can be declared by a Spring Boot module, library, or application. It provides immutable access to the
 * property’s identifying name, data type, description, default value, and any additional semantic hints or
 * deprecation metadata.
 *
 * <h2>Property Descriptor Purpose</h2>
 * Within the Konfigyr {@code Artifactory}, a {@code PropertyDescriptor} serves as:
 * <ul>
 *   <li>A lightweight, immutable projection of configuration metadata extracted from build artifacts.</li>
 *   <li>A descriptor used for displaying configuration properties within namespaces and services.</li>
 *   <li>A reference point for provenance tracking, linking a property definition to specific artifact versions.</li>
 *   <li>An input source for configuration validation, schema export, or documentation generation.</li>
 * </ul>
 *
 * @author Vladimir Spasic
 * @see Deprecation
 * @see <a href="https://docs.spring.io/spring-boot/specification/configuration-metadata/index.html">Spring Boot configuration metadata</a>
 * @see <a href="https://docs.spring.io/spring-boot/how-to/properties-and-configuration.html">Spring Boot documentation</a>
 * @since 1.0.0
 */
public interface PropertyDescriptor extends Comparable<PropertyDescriptor>, Serializable {

	/**
	 * Creates a new instance of the {@link DefaultPropertyDescriptor.Builder} used to create a
	 * new instance of the {@link DefaultPropertyDescriptor} using the fluent builder API.
	 *
	 * @return default property descriptor builder, never {@literal null}
	 */
	static DefaultPropertyDescriptor.Builder builder() {
		return new DefaultPropertyDescriptor.Builder();
	}

	/**
	 * Returns the logical property name.
	 * <p>
	 * For example, {@code spring.datasource.url} or {@code server.port}.
	 *
	 * @return the property name, never {@literal null}.
	 */
	@NonNull
	String name();

	/**
	 * Returns the JSON Schema definition that describes the structure, type, and validation rules of the
	 * property's value.
	 * <p>
	 * The schema serves as the contract between the backend and the user interface. It enables type-safe
	 * editing, validation, and rendering of complex configuration structures such as objects, lists, or maps.
	 * <p>
	 * Examples:
	 * <ul>
	 *   <li>For a simple scalar property with type of <code>java.lang.String</code>:
	 *     <pre>{@code {"type": "string"}}</pre>
	 *   </li>
	 *   <li>For a dynamic configuration property with type <code>com.example.Account</code>:
	 *     <pre>{@code
	 *     {
	 *       "type": "object",
	 *       "properties": {
	 *         "name": {"type": "string"},
	 *         "age": {"type": "integer"},
	 *         "enabled": {"type": "boolean"}
	 *       },
	 *       "required": ["name", "age", "enabled"]
	 *     }
	 *     }</pre>
	 *   </li>
	 *   <li>For a collection of dynamic configuration values with type <code>com.example.Account[]</code>:
	 *     <pre>{@code
	 *     {
	 *       "type": "array",
	 *       "items": {
	 *         "type": "object",
	 *         "properties": {
	 *           "name": {"type": "string"},
	 *           "age": {"type": "integer"},
	 *           "enabled": {"type": "boolean"}
	 *         },
	 *         "required": ["name", "age", "enabled"]
	 *       }
	 *     }
	 *     }</pre>
	 *   </li>
	 *   <li>For a map of dynamic configuration values with type <code>java.util.Map&lt;java.lang.String,com.example.Account&gt;</code>:
	 *     <pre>{@code
	 *     {
	 *       "type": "object",
	 *       "propertyNames" : {
	 *         "type" : "string"
	 *       },
	 *       "additionalProperties" : {
	 *         "type": "object",
	 *         "properties": {
	 *           "name": {"type": "string"},
	 *           "age": {"type": "integer"},
	 *           "enabled": {"type": "boolean"}
	 *         },
	 *         "required": ["name", "age", "enabled"]
	 *       }
	 *     }
	 *     }</pre>
	 *   </li>
	 * </ul>
	 *
	 * @return a JSON Schema node describing the property's value structure, never {@code null}.
	 * @see <a href="https://json-schema.org/draft/2020-12/json-schema-core.html">JSON Schema</a>
	 */
	@NonNull
	String schema();

	/**
	 * Returns the fully qualified Java type name of the property value.
	 * <p>
	 * For example, {@code java.lang.String} or {@code java.util.Boolean}. This information is mainly used for
	 * introspection.
	 *
	 * @return the fully qualified type name, never {@literal null}.
	 */
	@Nullable
	String typeName();

	/**
	 * Returns a human-readable description of the property’s purpose.
	 * <p>
	 * This may include guidance on expected values, usage context, or side effects of changing the configuration.
	 *
	 * @return a textual description of the property, may be {@literal null}.
	 */
	@Nullable
	String description();

	/**
	 * Returns the default value for this property, if defined.
	 * <p>
	 * The default value is usually determined by the library or framework providing the configuration property.
	 *
	 * @return the default property value, or {@literal null} if none is defined.
	 */
	@Nullable
	String defaultValue();

	/**
	 * Returns deprecation metadata associated with this property, if applicable.
	 * <p>
	 * If a property is deprecated, the {@link Deprecation} object provides the reason, replacement suggestion,
	 * and since-which-version metadata.
	 *
	 * @return deprecation metadata, may be {@literal null}.
	 */
	@Nullable
	Deprecation deprecation();

	@Override
	default int compareTo(@NonNull PropertyDescriptor other) {
		return Comparator.comparing(PropertyDescriptor::name).compare(this, other);
	}
}
