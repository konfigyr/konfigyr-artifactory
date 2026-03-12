package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

/**
 * Base JSON Schema that represents any numeric type, including integers and floating-point numbers.
 * <p>
 * JSON Schema differentiates between arbitrary numbers ({@code number}) and whole numbers ({@code integer}),
 * allowing for detailed constraints like minimum, maximum, and exclusive boundaries.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 * @param <T> the exact type of the numerical value
 */
public sealed abstract class NumericalSchema<T extends Number> extends JsonSchema permits IntegerSchema, NumberSchema {

	@Serial
	private static final long serialVersionUID = 9035225902876502098L;

	private final String format;
	private final T minimum;
	private final T maximum;
	private final Double multipleOf;
	private final Boolean exclusiveMinimum;
	private final Boolean exclusiveMaximum;

	/**
	 * Creates a new instance of the {@link NumericalSchema} class using the values from the given builder.
	 *
	 * @param builder the builder instance, never {@literal null}.
	 */
	protected NumericalSchema(Builder<T, ?> builder) {
		super(builder);
		this.format = builder.format;
		this.minimum = builder.minimum;
		this.maximum = builder.maximum;
		this.multipleOf = builder.multipleOf;
		this.exclusiveMinimum = builder.exclusiveMinimum;
		this.exclusiveMaximum = builder.exclusiveMaximum;
	}

	/**
	 * Returns the format of the numerical value defined by the schema.
	 *
	 * @return the numeric value format, can be {@literal null}.
	 */
	@Nullable
	public String format() {
		return format;
	}

	/**
	 * A numerical value described by this schema is considered valid if it is greater than, or equal to,
	 * the value of this method.
	 *
	 * @return the minimum value, can be {@literal null}.
	 * @see #exclusiveMinimum()
	 */
	@Nullable
	public T minimum() {
		return minimum;
	}

	/**
	 * A numerical value described by this schema is considered valid if it is less than, or equal to,
	 * the value of this method.
	 *
	 * @return the maximum value, can be {@literal null}.
	 * @see #exclusiveMaximum()
	 */
	@Nullable
	public T maximum() {
		return maximum;
	}

	/**
	 * A numerical value described by this schema is considered valid if the division between the number
	 * and the value of this keyword results in an {@code integer}.
	 *
	 * @return the divisor value, can be {@literal null}.
	 */
	@Nullable
	public Double multipleOf() {
		return multipleOf;
	}

	/**
	 * If this values holds a boolean, then the minimum is required and is used as a reference for comparison.
	 *
	 * @return {@code true} if the minimum value should be exclusive.
	 */
	public boolean exclusiveMinimum() {
		return Boolean.TRUE.equals(exclusiveMinimum);
	}

	/**
	 * If this values holds a boolean, then the maximum is required and is used as a reference for comparison.
	 *
	 * @return {@code true} if the maximum value should be exclusive.
	 */
	public boolean exclusiveMaximum() {
		return Boolean.TRUE.equals(exclusiveMaximum);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NumericalSchema<?> that)) return false;
		if (!super.equals(o)) return false;
		return Objects.equals(format, that.format)
				&& Objects.equals(minimum, that.minimum)
				&& Objects.equals(maximum, that.maximum)
				&& Objects.equals(multipleOf, that.multipleOf)
				&& Objects.equals(exclusiveMinimum, that.exclusiveMinimum)
				&& Objects.equals(exclusiveMaximum, that.exclusiveMaximum);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(format);
		result = 31 * result + Objects.hashCode(minimum);
		result = 31 * result + Objects.hashCode(maximum);
		result = 31 * result + Objects.hashCode(multipleOf);
		result = 31 * result + Objects.hashCode(exclusiveMinimum);
		result = 31 * result + Objects.hashCode(exclusiveMaximum);
		return result;
	}

	@Override
	protected StringBuilder toStringBuilder() {
		final StringBuilder builder = super.toStringBuilder();

		if (format != null) {
			builder.append(", format='").append(format).append('\'');
		}
		if (minimum != null) {
			builder.append(", minimum=").append(minimum);
		}
		if (maximum != null) {
			builder.append(", maximum=").append(maximum);
		}
		if (multipleOf != null) {
			builder.append(", multipleOf=").append(multipleOf);
		}
		if (exclusiveMinimum != null) {
			builder.append(", exclusiveMinimum=").append(exclusiveMinimum);
		}
		if (exclusiveMaximum != null) {
			builder.append(", exclusiveMaximum=").append(exclusiveMaximum);
		}

		return builder;
	}

	/**
	 * Abstract builder class for the builder classes of {@link NumericalSchema} subclasses.
	 *
	 * @param <T> the type of the numerical value.
	 * @param <B> the type of the builder subclass.
	 */
	protected static abstract class Builder<T extends Number, B extends Builder<T, B>> extends JsonSchema.Builder<NumericalSchema<T>, B> {

		private String format;
		private T minimum;
		private T maximum;
		private Double multipleOf;
		private Boolean exclusiveMinimum;
		private Boolean exclusiveMaximum;

		/**
		 * Creates a new instance of the abstract numerical {@link Builder} class with the given
		 * JSON schema type.
		 *
		 * @param type the JSON schema type, must not be {@literal null}.
		 */
		protected Builder(JsonSchemaType type) {
			super(type);
		}

		/**
		 * Specify the format of the numerical value.
		 *
		 * @param format the numerical value format, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B format(@Nullable String format) {
			this.format = format;
			return myself();
		}

		/**
		 * Specify the minimum allowed value described by this schema.
		 * <p>
		 * If the {@link #exclusiveMinimum()} is set to {@literal true}, then the minimum value is exclusive.
		 *
		 * @param minimum the minimum value, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B minimum(@Nullable T minimum) {
			this.minimum = minimum;
			return myself();
		}

		/**
		 * Specify the maximum allowed value described by this schema.
		 * <p>
		 * If the {@link #exclusiveMaximum()} is set to {@literal true}, then the maximum value is exclusive.
		 *
		 * @param maximum the maximum value, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B maximum(@Nullable T maximum) {
			this.maximum = maximum;
			return myself();
		}

		/**
		 * Specify the divisor value used to validate the numerical value.
		 *
		 * @param multipleOf the divisor value, can be {@literal null}.
		 * @return the builder instance.
		 */
		public B multipleOf(@Nullable Double multipleOf) {
			this.multipleOf = multipleOf;
			return myself();
		}

		/**
		 * Specify the minimum value should be exclusive.
		 *
		 * @param exclusiveMinimum {@literal true} if the minimum value should be exclusive, may be {@literal null}.
		 * @return the builder instance.
		 */
		public B exclusiveMinimum(@Nullable Boolean exclusiveMinimum) {
			this.exclusiveMinimum = exclusiveMinimum;
			return myself();
		}

		/**
		 * Specify the maximum value should be exclusive.
		 *
		 * @param exclusiveMaximum {@literal true} if the maximum value should be exclusive, may be {@literal null}.
		 * @return the builder instance.
		 */
		public B exclusiveMaximum(@Nullable Boolean exclusiveMaximum) {
			this.exclusiveMaximum = exclusiveMaximum;
			return myself();
		}

	}

}
