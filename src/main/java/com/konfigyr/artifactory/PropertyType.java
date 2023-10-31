package com.konfigyr.artifactory;

/**
 * Enumeration that defines a list of supported property types that is supported by the
 * Konfigyr Config Server.
 *
 * @author : Vladimir Spasic
 * @since : 30.10.22, Mon
 **/
public enum PropertyType {

	/**
	 * The value of the {@link PropertyDescriptor} should be treated as a string. This
	 * type is considered as the default type.
	 */
	STRING,
	/**
	 * The value of the {@link PropertyDescriptor} should be treated as a number type. It
	 * can be an integer, float or long.
	 */
	NUMBER,
	/**
	 * The value of the {@link PropertyDescriptor} should be treated as a simple boolean
	 * type.
	 */
	BOOLEAN,
	/**
	 * Defines the value of the {@link PropertyDescriptor} as a duration. The value is
	 * stored using ISO-8601 formatting style.
	 */
	DURATION,
	/**
	 * Value of the {@link PropertyDescriptor} is a time zone value.
	 */
	TIME_ZONE,
	/**
	 * Defines the value of the {@link PropertyDescriptor} as a date. The value is stored
	 * using ISO-8601 formatting style: <code>YYYY-MM-DD</code>.
	 */
	DATE,
	/**
	 * Defines the value of the {@link PropertyDescriptor} as a date time. The value is
	 * stored using ISO-8601 formatting style:
	 * <code>YYYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm]</code>.
	 */
	DATE_TIME,
	/**
	 * Value of the {@link PropertyDescriptor} is a valid {@link java.net.URI}.
	 */
	URI,
	/**
	 * Value of the {@link PropertyDescriptor} is a valid {@link java.net.InetAddress}.
	 */
	INTERNET_ADDRESS,
	/**
	 * Defines the value of the {@link PropertyDescriptor} as a data size. The value is
	 * stored in the following formats: <pre>
	 * "12KB" -- parses as "12 kilobytes"
	 * "5MB"  -- parses as "5 megabytes"
	 * </pre>
	 */
	DATA_SIZE,
	/**
	 * Value of the {@link PropertyDescriptor} is a media type value.
	 */
	MIME_TYPE,
	/**
	 * Value of the {@link PropertyDescriptor} represents a specific character encoding.
	 */
	CHARSET,
	/**
	 * Value of the {@link PropertyDescriptor} represents a specific locale or language.
	 */
	LOCALE

}
