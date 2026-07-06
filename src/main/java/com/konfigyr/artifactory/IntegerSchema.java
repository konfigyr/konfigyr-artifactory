package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * JSON schema that is a subtype of the {@link NumericalSchema} and represents the integer type.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public final class IntegerSchema extends NumericalSchema<Long> {

	@Serial
	private static final long serialVersionUID = 4655768043562605521L;

	private static final IntegerSchema INSTANCE = builder().build();

	/**
	 * Returns the shared, immutable {@link IntegerSchema} instance with only the {@code type}
	 * property set. This method always returns the same cached instance rather than creating a
	 * new one.
	 *
	 * @return the integer schema.
	 */
	public static IntegerSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a new {@link IntegerSchema.Builder} instance.
	 *
	 * @return the integer schema builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private IntegerSchema(Builder builder) {
		super(builder);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof IntegerSchema && super.equals(o);
	}

	/**
	 * Builder for {@link IntegerSchema} instances.
	 */
	public static final class Builder extends NumericalSchema.Builder<Long, Builder> {

		private Builder() {
			super(JsonSchemaType.INTEGER);
		}

		@Override
		public IntegerSchema build() {
			return new IntegerSchema(this);
		}

	}

}
