package com.konfigyr.artifactory;

import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Internal utility class used by this SDK's builders and immutable value types to validate
 * constructor and setter arguments.
 * <p>
 * Every method follows the same shape: it returns the given value unchanged when it satisfies the
 * check, or throws an {@link IllegalArgumentException} carrying the supplied message when it does
 * not. This allows a guard clause to be expressed as a single assignment instead of a separate
 * {@code if}/{@code throw} statement, for example:
 *
 * <pre>{@code
 * this.groupId = Asserts.notBlank(groupId, "Artifact groupId can not be blank");
 * }</pre>
 *
 * <p>
 * Each check also has an overload accepting a lazily evaluated {@link Supplier} of the failure
 * message. Prefer it when building the message is not free, e.g. it involves string concatenation
 * or formatting, since the supplier is invoked only when the check actually fails.
 * <p>
 * This class is package-private: it exists purely to keep validation logic consistent across this
 * SDK's types and is not part of the public API.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
class Asserts {

	// Prevent instantiation of utility class
	private Asserts() {
		throw new AssertionError("No Asserts instances for you!");
	}

	/**
	 * Ensures that the given object reference is not {@literal null}.
	 *
	 * @param value   the value to check, may be {@literal null}.
	 * @param message the failure message used to construct the thrown exception.
	 * @param <T>     the type of the value being checked.
	 * @return the given {@code value}, never {@literal null}.
	 * @throws IllegalArgumentException if {@code value} is {@literal null}.
	 */
	static <T> T nonNull(@Nullable T value, String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}

	/**
	 * Ensures that the given object reference is not {@literal null}, using a lazily evaluated
	 * message supplier that is only invoked when the check fails.
	 *
	 * @param value           the value to check, may be {@literal null}.
	 * @param messageSupplier supplies the failure message used to construct the thrown exception,
	 *                        invoked only if {@code value} is {@literal null}.
	 * @param <T>             the type of the value being checked.
	 * @return the given {@code value}, never {@literal null}.
	 * @throws IllegalArgumentException if {@code value} is {@literal null}.
	 */
	static <T> T nonNull(@Nullable T value, Supplier<String> messageSupplier) {
		if (value == null) {
			throw new IllegalArgumentException(messageSupplier.get());
		}
		return value;
	}

	/**
	 * Ensures that the given string is not {@literal null}, empty, or composed entirely of
	 * whitespace characters, as defined by {@link String#isBlank()}.
	 *
	 * @param value   the string to check, may be {@literal null}.
	 * @param message the failure message used to construct the thrown exception.
	 * @return the given {@code value}, never {@literal null}, empty, or blank.
	 * @throws IllegalArgumentException if {@code value} is {@literal null}, empty, or blank.
	 */
	static String notBlank(@Nullable String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}

	/**
	 * Ensures that the given string is not {@literal null}, empty, or composed entirely of
	 * whitespace characters, as defined by {@link String#isBlank()}, using a lazily evaluated
	 * message supplier that is only invoked when the check fails.
	 *
	 * @param value           the string to check, may be {@literal null}.
	 * @param messageSupplier supplies the failure message used to construct the thrown exception,
	 *                        invoked only if {@code value} is {@literal null}, empty, or blank.
	 * @return the given {@code value}, never {@literal null}, empty, or blank.
	 * @throws IllegalArgumentException if {@code value} is {@literal null}, empty, or blank.
	 */
	static String notBlank(@Nullable String value, Supplier<String> messageSupplier) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(messageSupplier.get());
		}
		return value;
	}

	/**
	 * Ensures that the given collection is not {@literal null} or empty.
	 *
	 * @param value   the collection to check, may be {@literal null}.
	 * @param message the failure message used to construct the thrown exception.
	 * @param <T>     the element type of the collection being checked.
	 * @return the given {@code value}, never {@literal null} or empty.
	 * @throws IllegalArgumentException if {@code value} is {@literal null} or empty.
	 */
	static <T> Collection<T> notEmpty(@Nullable Collection<T> value, String message) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return value;
	}

	/**
	 * Ensures that the given collection is not {@literal null} or empty, using a lazily evaluated
	 * message supplier that is only invoked when the check fails.
	 *
	 * @param value           the collection to check, may be {@literal null}.
	 * @param messageSupplier supplies the failure message used to construct the thrown exception,
	 *                        invoked only if {@code value} is {@literal null} or empty.
	 * @param <T>             the element type of the collection being checked.
	 * @return the given {@code value}, never {@literal null} or empty.
	 * @throws IllegalArgumentException if {@code value} is {@literal null} or empty.
	 */
	static <T> Collection<T> notEmpty(@Nullable Collection<T> value, Supplier<String> messageSupplier) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(messageSupplier.get());
		}
		return value;
	}
}
