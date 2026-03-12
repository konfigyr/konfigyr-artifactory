package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

/**
 * JSON Schema that represents a sequence of zero or more Unicode characters. Strings are versatile and
 * can represent text, dates, or any other character-based data. JSON Schema further allows for the
 * specification of string formats such as {@code date-time}, {@code email}, or {@code hostname},
 * enabling more precise validation of the string's value.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public final class StringSchema extends JsonSchema {

	@Serial
	private static final long serialVersionUID = 6148120199903217852L;

	private static final StringSchema INSTANCE = builder().build();

	private final Integer minLength;
	private final Integer maxLength;
	private final String format;
	private final String pattern;

	/**
	 * Creates a new default {@link StringSchema} instance with only the {@code type} property set.
	 *
	 * @return the string schema.
	 */
	public static StringSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a {@link Builder} used to create a new {@link StringSchema}.
	 *
	 * @return the builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private StringSchema(Builder builder) {
		super(builder);
		this.minLength = builder.minLength;
		this.maxLength = builder.maxLength;
		this.format = builder.format;
		this.pattern = builder.pattern;
	}

	/**
	 * The string value of this JSON Schema is considered valid if its length is greater than, or equal to,
	 * the value of this method.
	 *
	 * @return the minimum length, can be {@literal null}.
	 */
	@Nullable
	public Integer minLength() { return minLength; }

	/**
	 * The string value of this JSON Schema is considered valid if its length is lower than, or equal to,
	 * the value of this method.
	 *
	 * @return the maximum length, can be {@literal null}.
	 */
	@Nullable
	public Integer maxLength() { return maxLength; }

	/**
	 * Returns the format of the string value defined by the schema.
	 *
	 * @return the string value format, can be {@literal null}.
	 */
	@Nullable
	public String format() { return format; }

	/**
	 * The string value of this JSON Schema is considered valid if it matches the regular expression
	 * defined by this method.
	 *
	 * @return the regular expression, can be {@literal null}.
	 */
	@Nullable
	public String pattern() { return pattern; }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof StringSchema that)) return false;
		if (!super.equals(o)) return false;
		return Objects.equals(minLength, that.minLength)
				&& Objects.equals(maxLength, that.maxLength)
				&& Objects.equals(format, that.format)
				&& Objects.equals(pattern, that.pattern);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(minLength);
		result = 31 * result + Objects.hashCode(maxLength);
		result = 31 * result + Objects.hashCode(format);
		result = 31 * result + Objects.hashCode(pattern);
		return result;
	}

	@Override
	protected StringBuilder toStringBuilder() {
		final StringBuilder builder = super.toStringBuilder();

		if (format != null) {
				builder.append(", format='").append(format).append('\'');
		}
		if (pattern != null) {
				builder.append(", pattern='").append(pattern).append('\'');
		}
		if (minLength != null) {
			builder.append(", minLength=").append(minLength);
		}
		if (maxLength != null) {
			builder.append(", maxLength=").append(maxLength);
		}

		return builder;
	}

	/**
	 * Builder class for the {@link StringSchema} class.
	 */
	public static final class Builder extends JsonSchema.Builder<StringSchema, Builder> {

		private Integer minLength;
		private Integer maxLength;
		private String format;
		private String pattern;

		private Builder() {
			super(JsonSchemaType.STRING);
		}

		/**
		 * Specify the minimum length of the string value defined by this schema.
		 *
		 * @param minLength the min length, can be {@literal null}
		 * @return the builder instance.
		 */
		public Builder minLength(@Nullable Integer minLength) {
			this.minLength = minLength;
			return myself();
		}

		/**
		 * Specify the maximum length of the string value defined by this schema.
		 *
		 * @param maxLength the max length, can be {@literal null}
		 * @return the builder instance.
		 */
		public Builder maxLength(@Nullable Integer maxLength) {
			this.maxLength = maxLength;
			return myself();
		}

		/**
		 * Specify the format of the string value.
		 *
		 * @param format the string value format, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder format(@Nullable String format) {
			this.format = format;
			return myself();
		}

		/**
		 * Specify the regular expression that the string value must match.
		 *
		 * @param pattern the regular expression, can be {@literal null}
		 * @return the builder instance.
		 */
		public Builder pattern(@Nullable String pattern) {
			this.pattern = pattern;
			return myself();
		}

		@Override
		public StringSchema build() {
			return new StringSchema(this);
		}
	}

}
