package com.konfigyr.artifactory;

import com.github.zafarkhaja.semver.Version;
import io.avaje.jsonb.JsonDataException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ComponentTest {

	@Test
	void shouldSerializeComponent() {
		final var component = new Component(Version.valueOf("1.1.1"), false,
				List.of(PropertyDescriptor.builder().name("test.property").build(),
						PropertyDescriptor.builder()
							.name("another.property")
							.type(PropertyType.BOOLEAN)
							.dataType(DataType.ATOMIC)
							.typeName(Boolean.class.getCanonicalName())
							.defaultValue("true")
							.description("Property description")
							.deprecation("To be removed")
							.hints("true", "false")
							.build()));

		assertThat(new String(component.serialize()))
			.isEqualTo("[{\"name\":\"test.property\",\"type\":\"STRING\",\"data_type\":\"ATOMIC\",\"hints\":[]},"
					+ "{\"name\":\"another.property\",\"type\":\"BOOLEAN\",\"data_type\":\"ATOMIC\","
					+ "\"type_name\":\"java.lang.Boolean\",\"description\":\"Property description\","
					+ "\"default_value\":\"true\",\"hints\":[\"true\",\"false\"],\"deprecation\":"
					+ "{\"reason\":\"To be removed\"}}]");
	}

	@Test
	void shouldDeserializeComponent() {
		final var json = "[{\"name\":\"test.property\",\"type\":\"LOCALE\",\"data_type\":\"COLLECTION\","
				+ "\"type_name\":\"java.util.Locale\",\"description\": \"Language\", "
				+ "\"default_value\": \"de\", \"hints\":[\"en\"]}]";

		assertThat(new Component(Version.valueOf("1.2.4"), true, json.getBytes())).isNotNull()
			.returns(Version.valueOf("1.2.4"), Component::version)
			.returns(true, Component::latest)
			.extracting(Component::properties)
			.asList()
			.contains(PropertyDescriptor.builder()
				.name("test.property")
				.type(PropertyType.LOCALE)
				.dataType(DataType.COLLECTION)
				.typeName(Locale.class.getCanonicalName())
				.description("Language")
				.defaultValue("de")
				.hint("en")
				.build());
	}

	@Test
	void shouldFailToDeserializeInvalidDescriptor() {
		final var json = "invalid json";

		assertThatThrownBy(() -> new Component(Version.valueOf("1.2.4"), true, json.getBytes()))
			.isInstanceOf(JsonDataException.class)
			.hasNoCause();
	}

}