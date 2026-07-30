package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of the {@link JsonSchemaFormat} values known to, and built into, the Konfigyr Artifactory
 * SDK.
 * <p>
 * This is a closed set of formats maintained by this SDK. Other modules that need formats outside of
 * this set should provide their own {@link JsonSchemaFormat} implementation rather than extending this
 * enumeration.
 *
 * @author Vladimir Spasic
 * @see JsonSchemaFormat
 * @since 1.0.0
 */
public enum KonfigyrFormat implements JsonSchemaFormat {

	/**
	 * A Uniform Resource Identifier, e.g. {@code https://konfigyr.com}.
	 * <p>
	 * Applies to Java types: {@link java.net.URI}, {@link java.net.URL}, {@link java.io.File},
	 * {@link java.nio.file.Path}.
	 * <p>
	 * Validation: must be a syntactically valid URI as defined by RFC 3986.
	 */
	URI("uri", JsonSchemaType.STRING),

	/**
	 * A date, e.g. {@code 2024-01-31}.
	 * <p>
	 * Applies to Java type: {@link java.time.LocalDate}.
	 * <p>
	 * Validation: must be a {@code full-date} as defined by RFC 3339, i.e. {@code yyyy-MM-dd}.
	 */
	DATE("date", JsonSchemaType.STRING),

	/**
	 * A date and time, e.g. {@code 2024-01-31T13:00:00Z}.
	 * <p>
	 * Applies to Java types: {@link java.time.LocalDateTime}, {@link java.time.ZonedDateTime},
	 * {@link java.time.OffsetDateTime}, {@link java.time.Instant}, {@link java.util.Date},
	 * {@link java.util.Calendar}.
	 * <p>
	 * Validation: must be a {@code date-time} as defined by RFC 3339, i.e.
	 * {@code yyyy-MM-dd'T'HH:mm:ss[.SSS](Z|+HH:mm)}.
	 */
	DATE_TIME("date-time", JsonSchemaType.STRING),

	/**
	 * A time, e.g. {@code 13:00:00}.
	 * <p>
	 * Applies to Java types: {@link java.time.LocalTime}, {@link java.time.OffsetTime}.
	 * <p>
	 * Validation: must be a {@code full-time} as defined by RFC 3339, i.e.
	 * {@code HH:mm:ss[.SSS](Z|+HH:mm)}.
	 */
	TIME("time", JsonSchemaType.STRING),

	/**
	 * A duration, e.g. {@code PT1H30M}.
	 * <p>
	 * Applies to Java types: {@link java.time.Duration}, {@link java.time.Period}.
	 * <p>
	 * Validation: must be an ISO-8601 duration, i.e. {@code P[n]Y[n]M[n]DT[n]H[n]M[n]S} or, for
	 * {@code Duration}, the {@code PT[n]H[n]M[n]S} subset.
	 */
	DURATION("duration", JsonSchemaType.STRING),

	/**
	 * A universally unique identifier, e.g. {@code 3fa85f64-5717-4562-b3fc-2c963f66afa6}.
	 * <p>
	 * Applies to Java type: {@link java.util.UUID}.
	 * <p>
	 * Validation: must be an RFC 4122 UUID, i.e., 32 hexadecimal digits grouped as
	 * {@code 8-4-4-4-12}.
	 */
	UUID("uuid", JsonSchemaType.STRING),

	/**
	 * A character set name, e.g. {@code UTF-8}.
	 * <p>
	 * Applies to Java type: {@link java.nio.charset.Charset}.
	 * <p>
	 * Validation: must be a name resolvable via {@link java.nio.charset.Charset#forName(String)}.
	 */
	CHARSET("charset", JsonSchemaType.STRING),

	/**
	 * A regular expression, e.g. {@code ^[a-zA-Z0-9]+$}.
	 * <p>
	 * Applies to Java type: {@link java.util.regex.Pattern}.
	 * <p>
	 * Validation: must compile as a valid regular expression, i.e., accepted by
	 * {@link java.util.regex.Pattern#compile(String)}.
	 */
	REGEX("regex", JsonSchemaType.STRING),

	/**
	 * A time zone identifier, e.g. {@code Europe/Belgrade}.
	 * <p>
	 * Applies to Java types: {@link java.time.ZoneId}, {@link java.util.TimeZone}.
	 * <p>
	 * Validation: must be a valid IANA time zone id or a fixed offset, i.e., accepted by
	 * {@link java.time.ZoneId#of(String)}.
	 */
	TIME_ZONE("time-zone", JsonSchemaType.STRING),

	/**
	 * A year and month, e.g. {@code 2024-01}.
	 * <p>
	 * Applies to Java type: {@link java.time.YearMonth}.
	 * <p>
	 * Validation: must be in the form {@code yyyy-MM}.
	 */
	YEAR_MONTH("year-month", JsonSchemaType.STRING),

	/**
	 * A year, e.g. {@code 2024}.
	 * <p>
	 * Applies to Java type: {@link java.time.Year}.
	 * <p>
	 * Validation: must be a four-digit year, i.e. {@code yyyy}.
	 */
	YEAR("year", JsonSchemaType.STRING),

	/**
	 * A language tag, e.g. {@code en-US}.
	 * <p>
	 * Applies to Java type: {@link java.util.Locale}.
	 * <p>
	 * Validation: must be a well-formed BCP 47 language tag, i.e., accepted by
	 * {@link java.util.Locale#forLanguageTag(String)}.
	 */
	LANGUAGE("language", JsonSchemaType.STRING),

	/**
	 * A currency code, e.g. {@code EUR}.
	 * <p>
	 * Applies to Java type: {@link java.util.Currency}.
	 * <p>
	 * Validation: must be an ISO 4217 three-letter currency code, i.e., accepted by
	 * {@link java.util.Currency#getInstance(String)}.
	 */
	CURRENCY("currency", JsonSchemaType.STRING),

	/**
	 * An IPv4 address, e.g. {@code 127.0.0.1}.
	 * <p>
	 * Applies to Java type: {@link java.net.Inet4Address}.
	 * <p>
	 * Validation: must be a dotted-quad IPv4 address, i.e., four decimal octets, {@code 0-255} each,
	 * separated by dots.
	 */
	IPV4("ipv4", JsonSchemaType.STRING),

	/**
	 * An IPv6 address, e.g. {@code ::1}.
	 * <p>
	 * Applies to Java type: {@link java.net.Inet6Address}.
	 * <p>
	 * Validation: must be an RFC 4291 IPv6 address, i.e., eight colon-separated hextets, optionally
	 * compressed with {@code ::}.
	 */
	IPV6("ipv6", JsonSchemaType.STRING),

	/**
	 * A resource location, e.g. {@code classpath:application.yml}.
	 * <p>
	 * Applies to Spring's {@code org.springframework.core.io.Resource} type.
	 * <p>
	 * Validation: must be a location resolvable by a Spring {@code ResourceLoader}, e.g., prefixed
	 * with {@code classpath:}, {@code file:}, or a well-formed URL.
	 */
	RESOURCE("resource", JsonSchemaType.STRING),

	/**
	 * A MIME type, e.g. {@code application/json}.
	 * <p>
	 * Applies to Spring's {@code org.springframework.util.MimeType} type.
	 * <p>
	 * Validation: must be a {@code type/subtype} media type as defined by RFC 2046.
	 */
	MIME_TYPE("mime-type", JsonSchemaType.STRING),

	/**
	 * A human-readable data size, e.g. {@code 10MB}.
	 * <p>
	 * Applies to Spring's {@code org.springframework.util.unit.DataSize} type.
	 * <p>
	 * Validation: must be a magnitude followed by a unit, e.g. {@code B}, {@code KB}, {@code MB},
	 * {@code GB}, or {@code TB}, as accepted by Spring's {@code DataSize.parse(CharSequence)}.
	 */
	DATA_SIZE("data-size", JsonSchemaType.STRING),

	/**
	 * A 16-bit signed integer.
	 * <p>
	 * Applies to Java types: {@link Short}, {@code short}.
	 * <p>
	 * Validation: value must fit within a signed 16-bit range, i.e. {@code -32768} to {@code 32767}.
	 */
	INT16("int16", JsonSchemaType.INTEGER),

	/**
	 * A 32-bit signed integer.
	 * <p>
	 * Applies to Java types: {@link Integer}, {@code int}.
	 * <p>
	 * Validation: value must fit within a signed 32-bit range, i.e. {@code -2^31} to {@code 2^31-1}.
	 */
	INT32("int32", JsonSchemaType.INTEGER),

	/**
	 * A 64-bit signed integer.
	 * <p>
	 * Applies to Java types: {@link Long}, {@code long}.
	 * <p>
	 * Validation: value must fit within a signed 64-bit range, i.e. {@code -2^63} to {@code 2^63-1}.
	 */
	INT64("int64", JsonSchemaType.INTEGER),

	/**
	 * A single-precision floating point number.
	 * <p>
	 * Applies to Java types: {@link Float}, {@code float}.
	 * <p>
	 * Validation: value must be representable as an IEEE 754 single-precision (32-bit) value.
	 */
	FLOAT("float", JsonSchemaType.NUMBER),

	/**
	 * A double-precision floating point number.
	 * <p>
	 * Applies to Java types: {@link Double}, {@code double}.
	 * <p>
	 * Validation: value must be representable as an IEEE 754 double-precision (64-bit) value.
	 */
	DOUBLE("double", JsonSchemaType.NUMBER);

	private final String value;
	private final JsonSchemaType type;

	KonfigyrFormat(String value, JsonSchemaType type) {
		this.value = value;
		this.type = type;
	}

	@Override
	public String value() {
		return value;
	}

	@Override
	public JsonSchemaType type() {
		return type;
	}

	/**
	 * Attempts to resolve a {@link KonfigyrFormat} constant whose {@link #value()} matches the given
	 * wire-level format value.
	 * <p>
	 * This lookup is scoped to this enumeration's own closed set of formats. It would not resolve a
	 * {@link JsonSchemaFormat} defined by another module.
	 *
	 * @param value the wire-level format value to resolve, can be {@literal null}.
	 * @return an {@link Optional} containing the matching format, or empty if none was found.
	 */
	public static Optional<KonfigyrFormat> fromValue(@Nullable String value) {
		if (value == null || value.isBlank()) {
			return Optional.empty();
		}
		return Arrays.stream(values())
				.filter(format -> format.value.equals(value))
				.findFirst();
	}

}
