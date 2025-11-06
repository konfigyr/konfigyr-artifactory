package com.konfigyr.artifactory;

import java.io.Serial;
import java.io.Serializable;

/**
 * Record that provides additional information about the deprecation of a
 * {@link PropertyDescriptor}.
 *
 * @param reason      A reason why the related property is deprecated, if any.
 * @param replacement The full name of the property that replaces the related deprecated
 *                    property.
 * @author : Vladimir Spasic
 * @since : 16.03.23, Thu
 **/
public record Deprecation(String reason, String replacement) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8199075447827152698L;

}
