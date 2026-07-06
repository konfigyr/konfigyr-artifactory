package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AssertsTest {

	@Test
	@DisplayName("nonNull should return the value when it is not null")
	void nonNullReturnsValue() {
		assertThat(Asserts.nonNull("value", "message")).isEqualTo("value");
		assertThat(Asserts.nonNull("value", () -> "message")).isEqualTo("value");
	}

	@Test
	@DisplayName("nonNull should throw when the value is null")
	void nonNullThrowsWhenNull() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.nonNull(null, "value can not be null"))
				.withMessage("value can not be null");
	}

	@Test
	@DisplayName("nonNull should throw with the message from the lazy supplier when the value is null")
	void nonNullThrowsWithLazyMessageWhenNull() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.nonNull(null, () -> "value can not be null"))
				.withMessage("value can not be null");
	}

	@Test
	@DisplayName("nonNull should not evaluate the lazy message supplier when the value is not null")
	void nonNullDoesNotEvaluateSupplierWhenValid() {
		Asserts.nonNull("value", () -> {
			throw new AssertionError("message supplier should not have been evaluated");
		});
	}

	@Test
	@DisplayName("notBlank should return the value when it is not null, empty, or blank")
	void notBlankReturnsValue() {
		assertThat(Asserts.notBlank("value", "message")).isEqualTo("value");
		assertThat(Asserts.notBlank("value", () -> "message")).isEqualTo("value");
	}

	@Test
	@DisplayName("notBlank should throw when the value is null, empty, or blank")
	void notBlankThrowsWhenNullEmptyOrBlank() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank(null, "value can not be blank"))
				.withMessage("value can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank("", "value can not be blank"))
				.withMessage("value can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank("   ", "value can not be blank"))
				.withMessage("value can not be blank");
	}

	@Test
	@DisplayName("notBlank should throw with the message from the lazy supplier when the value is null, empty, or blank")
	void notBlankThrowsWithLazyMessageWhenNullEmptyOrBlank() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank(null, () -> "value can not be blank"))
				.withMessage("value can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank("", () -> "value can not be blank"))
				.withMessage("value can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notBlank("   ", () -> "value can not be blank"))
				.withMessage("value can not be blank");
	}

	@Test
	@DisplayName("notBlank should not evaluate the lazy message supplier when the value is valid")
	void notBlankDoesNotEvaluateSupplierWhenValid() {
		Asserts.notBlank("value", () -> {
			throw new AssertionError("message supplier should not have been evaluated");
		});
	}

	@Test
	@DisplayName("notEmpty should return the value when it is not null or empty")
	void notEmptyReturnsValue() {
		final var value = List.of("value");

		assertThat(Asserts.notEmpty(value, "message")).isEqualTo(value);
		assertThat(Asserts.notEmpty(value, () -> "message")).isEqualTo(value);
	}

	@Test
	@DisplayName("notEmpty should throw when the value is null or empty")
	void notEmptyThrowsWhenNullOrEmpty() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notEmpty(null, "value can not be empty"))
				.withMessage("value can not be empty");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notEmpty(Collections.emptyList(), "value can not be empty"))
				.withMessage("value can not be empty");
	}

	@Test
	@DisplayName("notEmpty should throw with the message from the lazy supplier when the value is null or empty")
	void notEmptyThrowsWithLazyMessageWhenNullOrEmpty() {
		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notEmpty(null, () -> "value can not be empty"))
				.withMessage("value can not be empty");

		assertThatIllegalArgumentException()
				.isThrownBy(() -> Asserts.notEmpty(Collections.emptyList(), () -> "value can not be empty"))
				.withMessage("value can not be empty");
	}

	@Test
	@DisplayName("notEmpty should not evaluate the lazy message supplier when the value is valid")
	void notEmptyDoesNotEvaluateSupplierWhenValid() {
		Asserts.notEmpty(List.of("value"), () -> {
			throw new AssertionError("message supplier should not have been evaluated");
		});
	}

}
