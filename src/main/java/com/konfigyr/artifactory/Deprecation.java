package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Record that provides additional information about the deprecation of a
 * {@link PropertyDescriptor}.
 *
 * @param reason      A reason why the related property is deprecated, if any, may be {@literal null}.
 * @param replacement The full name of the property that replaces the related deprecated
 *                    property, may be {@literal null}.
 * @author : Vladimir Spasic
 * @since 1.0.0
 **/
public record Deprecation(@Nullable String reason, @Nullable String replacement) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8199075447827152698L;

}
