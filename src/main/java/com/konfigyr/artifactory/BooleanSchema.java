package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * JSON Schema that represents the boolean type. This type is straightforward, accepting only two values:
 * {@code true} or {@code false}.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public final class BooleanSchema extends JsonSchema {

	@Serial
	private static final long serialVersionUID = 1458174855625187785L;

	private static final BooleanSchema INSTANCE = builder().build();

	/**
	 * Creates a new default {@link BooleanSchema} instance with only the {@code type} property set.
	 *
	 * @return the boolean schema.
	 */
	public static BooleanSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a new {@link BooleanSchema.Builder} instance.
	 *
	 * @return the boolean schema builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private BooleanSchema(Builder builder) {
		super(builder);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof BooleanSchema && super.equals(o);
	}

	/**
	 * Builder for {@link BooleanSchema} instances.
	 */
	public static final class Builder extends JsonSchema.Builder<JsonSchema, Builder> {

		private Builder() {
			super(JsonSchemaType.BOOLEAN);
		}

		@Override
		public BooleanSchema build() {
			return new BooleanSchema(this);
		}

	}

}
