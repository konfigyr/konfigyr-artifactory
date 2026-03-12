package com.konfigyr.artifactory;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * The {@code null} type in JSON Schema represents the absence of a value.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
@NullMarked
public final class NullSchema extends JsonSchema {

	@Serial
	private static final long serialVersionUID = 4523757291667609185L;

	private static final NullSchema INSTANCE = builder().build();

	/**
	 * Creates a new default {@link NullSchema} instance with only the {@code type} property set.
	 *
	 * @return the null schema.
	 */
	public static NullSchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a new {@link NullSchema.Builder} instance.
	 *
	 * @return the boolean schema builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private NullSchema(Builder builder) {
		super(builder);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NullSchema && super.equals(o);
	}

	/**
	 * Builder for {@link NullSchema} instances.
	 */
	public static final class Builder extends JsonSchema.Builder<JsonSchema, Builder> {

		private Builder() {
			super(JsonSchemaType.NULL);
		}

		@Override
		public NullSchema build() {
			return new NullSchema(this);
		}

	}

}
