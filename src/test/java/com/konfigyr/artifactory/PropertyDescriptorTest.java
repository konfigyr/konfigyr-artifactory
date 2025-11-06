package com.konfigyr.artifactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PropertyDescriptorTest {

	@Test
	@DisplayName("should create default property descriptor using the fluent builder")
	void createDefaultDescriptor() {
		final var descriptor = PropertyDescriptor.builder()
				.name("spring.application.name")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.defaultValue("my-awesome-application")
				.description("The name of the Spring application.")
				.deprecation("To be removed in 2.0.0.")
				.build();

		assertThat(descriptor)
				.isNotNull()
				.returns("spring.application.name", PropertyDescriptor::name)
				.returns("java.lang.String", PropertyDescriptor::typeName)
				.returns("my-awesome-application", PropertyDescriptor::defaultValue)
				.returns("The name of the Spring application.", PropertyDescriptor::description)
				.returns(new Deprecation("To be removed in 2.0.0.", null), PropertyDescriptor::deprecation);
	}

	@Test
	@DisplayName("builder should validate required properties")
	void validateRequiredProperties() {
		final var builder = PropertyDescriptor.builder();

		assertThatIllegalArgumentException()
				.isThrownBy(builder::build)
				.withMessage("Property name can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.name("spring.application.name")::build)
				.withMessage("Property value schema can not be blank");

		assertThatIllegalArgumentException()
				.isThrownBy(builder.schema("{\"type\":\"string\"}")::build)
				.withMessage("Property type name can not be blank");

		assertThat(builder.typeName("java.lang.String").build())
				.isNotNull()
				.returns("spring.application.name", PropertyDescriptor::name)
				.returns("java.lang.String", PropertyDescriptor::typeName)
				.returns(null, PropertyDescriptor::defaultValue)
				.returns(null, PropertyDescriptor::description)
				.returns(null, PropertyDescriptor::deprecation);
	}

	@Test
	@DisplayName("should sort property descriptors based on their names")
	void sortedDescriptors() {
		final var first = PropertyDescriptor.builder()
				.name("spring.application.group")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.build();

		final var second = PropertyDescriptor.builder()
				.name("spring.application.name")
				.typeName("java.lang.String")
				.schema("{\"type\":\"string\"}")
				.build();

		final var third = PropertyDescriptor.builder()
				.name("spring.banner.charset")
				.typeName("java.nio.charset.Charset")
				.schema("{\"type\":\"string\", \"format\":\"charset\"}")
				.build();

		final var fourth = PropertyDescriptor.builder()
				.name("spring.devtools.livereload.enabled")
				.typeName("java.lang.Boolean")
				.schema("{\"type\":\"boolean\"}")
				.build();

		assertThat(Stream.of(fourth, first, third, second).sorted())
				.containsExactly(first, second, third, fourth);
	}

}
