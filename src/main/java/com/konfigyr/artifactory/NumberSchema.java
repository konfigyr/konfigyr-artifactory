package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * JSON schema that is a subtype of the {@link NumericalSchema} and represents floating or double types.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public final class NumberSchema extends NumericalSchema<Double> {

	@Serial
	private static final long serialVersionUID = 8049992121166133393L;

	private static final NumberSchema INSTANCE = builder().build();

	/**
	 * Returns the shared, immutable {@link NumberSchema} instance with only the {@code type}
	 * property set. This method always returns the same cached instance rather than creating a
	 * new one.
	 *
	 * @return the number schema.
	 */
	public static NumberSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a new {@link NumberSchema.Builder} instance.
	 *
	 * @return the number schema builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private NumberSchema(Builder builder) {
		super(builder);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NumberSchema && super.equals(o);
	}

	/**
	 * Builder for {@link NumberSchema} instances.
	 */
	public static final class Builder extends NumericalSchema.Builder<Double, Builder> {

		private Builder() {
			super(JsonSchemaType.NUMBER);
		}

		@Override
		public NumberSchema build() {
			return new NumberSchema(this);
		}

	}

}
