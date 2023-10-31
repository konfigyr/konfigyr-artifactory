package com.konfigyr.artifactory;

import io.avaje.jsonb.Jsonb;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

final class Serializer {

	private static final Serializer INSTANCE = new Serializer();

	static Serializer getInstance() {
		return INSTANCE;
	}

	private final Jsonb jsonb;

	private Serializer() {
		jsonb = Jsonb.builder().failOnUnknown(true).serializeNulls(false).serializeEmpty(true).build();
	}

	@Nonnull
	List<PropertyDescriptor> read(@Nonnull byte[] data) {
		return read(new ByteArrayInputStream(data));
	}

	@Nonnull
	List<PropertyDescriptor> read(@Nonnull InputStream data) {
		return jsonb.type(PropertyDescriptor.class).list().fromJson(data);
	}

	@Nonnull
	byte[] write(@Nonnull List<PropertyDescriptor> properties) {
		return jsonb.type(PropertyDescriptor.class).list().toJsonBytes(properties);
	}

}
