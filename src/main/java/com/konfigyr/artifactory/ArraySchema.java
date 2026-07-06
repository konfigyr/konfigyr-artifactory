package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

/**
 * Array schema represents a list of values, where each value can be of any type. JSON Schema provides
 * mechanisms to validate the number of items in the array, ensure all items conform to a specified schema,
 * or validate against multiple schemas for items in a mixed-type array.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public final class ArraySchema extends JsonSchema {

	@Serial
	private static final long serialVersionUID = 2528115309717371054L;

	private static final ArraySchema INSTANCE = builder().build();

	/**
	 * JSON Schema of the items of the array.
	 */
	private final @Nullable JsonSchema items;

	/**
	 * The minimum size of the array.
	 */
	private final @Nullable Integer minItems;

	/**
	 * The maximum size of the array.
	 */
	private final @Nullable Integer maxItems;

	/**
	 * When set to {@code true}, the array items must be unique.
	 */
	private final @Nullable Boolean uniqueItems;

	/**
	 * Returns the shared, immutable {@link ArraySchema} instance with only the {@code type}
	 * property set. This method always returns the same cached instance rather than creating a
	 * new one.
	 *
	 * @return the array schema.
	 */
	public static ArraySchema instance() {
		return INSTANCE;
	}

	/**
	 * Creates a new {@link ArraySchema} instance with the given {@code items} schema.
	 *
	 * @param items the items schema, can be {@literal null}.
	 * @return the array schema.
	 */
	public static ArraySchema of(@Nullable JsonSchema items) {
		return builder().items(items).build();
	}

	/**
	 * Creates a new {@link ArraySchema.Builder} instance.
	 *
	 * @return the array schema builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private ArraySchema(Builder builder) {
		super(builder);
		this.items = builder.items;
		this.minItems = builder.minItems;
		this.maxItems = builder.maxItems;
		this.uniqueItems = builder.uniqueItems;
	}

	/**
	 * Defines the JSON Schema of the items of the array. Every item in the array must be valid against
	 * this schema.
	 *
	 * @return the items schema, can be {@literal null}.
	 */
	@Nullable
	public JsonSchema items() {
		return items;
	}

	/**
	 * An array described by this schema is considered valid if the number of items it contains is greater than,
	 * or equal to, the value of this method. When non-{@literal null}, this value must be a non-negative integer.
	 *
	 * @return the minimum number of items, can be {@literal null}.
	 */
	@Nullable
	public Integer minItems() {
		return minItems;
	}

	/**
	 * An array described by this schema is considered valid if the number of items it contains is lower than,
	 * or equal to, the value of this method. When non-{@literal null}, this value must be a non-negative integer.
	 *
	 * @return the maximum number of items, can be {@literal null}.
	 */
	@Nullable
	public Integer maxItems() {
		return maxItems;
	}

	/**
	 * An array described by this schema is valid if an item cannot be found more than once in the array.
	 * <p>
	 * If set to {@code false} or {@code null} the validation should be ignored.
	 *
	 * @return {@code true} if the array items must be unique, {@code false} otherwise.
	 */
	public boolean uniqueItems() {
		return Boolean.TRUE.equals(uniqueItems);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ArraySchema that)) return false;
		if (!super.equals(o)) return false;

		return Objects.equals(items, that.items)
				&& Objects.equals(minItems, that.minItems)
				&& Objects.equals(maxItems, that.maxItems)
				&& Objects.equals(uniqueItems, that.uniqueItems);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Objects.hashCode(items);
		result = 31 * result + Objects.hashCode(minItems);
		result = 31 * result + Objects.hashCode(maxItems);
		result = 31 * result + Objects.hashCode(uniqueItems);
		return result;
	}

	@Override
	protected StringBuilder toStringBuilder() {
		final StringBuilder builder = super.toStringBuilder();

		if (items != null) {
			builder.append(", items=").append(items);
		}
		if (minItems != null) {
			builder.append(", minItems=").append(minItems);
		}
		if (maxItems != null) {
			builder.append(", maxItems=").append(maxItems);
		}
		if (uniqueItems != null) {
			builder.append(", uniqueItems=").append(uniqueItems);
		}

		return builder;
	}

	/**
	 * Builder class for the {@link ArraySchema} class.
	 */
	public static final class Builder extends JsonSchema.Builder<ArraySchema, Builder> {
		private @Nullable JsonSchema items;
		private @Nullable Integer minItems;
		private @Nullable Integer maxItems;
		private @Nullable Boolean uniqueItems;

		private Builder() {
			super(JsonSchemaType.ARRAY);
		}

		/**
		 * Specify the JSON schema of the items that should be contained in the array.
		 *
		 * @param items the items schema, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder items(@Nullable JsonSchema items) {
			this.items = items;
			return myself();
		}

		/**
		 * Specify the minimum number of items that should be contained in the array.
		 *
		 * @param minItems the minimum number of items, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder minItems(@Nullable Integer minItems) {
			this.minItems = minItems;
			return myself();
		}

		/**
		 * Specify the maximum number of items that should be contained in the array.
		 *
		 * @param maxItems the maximum number of items, can be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder maxItems(@Nullable Integer maxItems) {
			this.maxItems = maxItems;
			return myself();
		}

		/**
		 * Specify if the array items must be unique.
		 *
		 * @param uniqueItems {@code true} if the array items must be unique, may be {@literal null}.
		 * @return the builder instance.
		 */
		public Builder uniqueItems(@Nullable Boolean uniqueItems) {
			this.uniqueItems = uniqueItems;
			return this;
		}

		@Override
		public ArraySchema build() {
			return new ArraySchema(this);
		}
	}

}
