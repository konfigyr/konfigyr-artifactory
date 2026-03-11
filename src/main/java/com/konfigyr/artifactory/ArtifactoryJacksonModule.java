package com.konfigyr.artifactory;

import com.fasterxml.jackson.annotation.*;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonPOJOBuilder;
import tools.jackson.databind.deser.std.StdScalarDeserializer;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.StdScalarSerializer;

import java.util.Collection;

/**
 * Jackson module for {@code konfigyr-artifactory}.
 * <p>
 * This module would register the following abstract type mappings:
 * <table>
 *     <thead>
 *         <tr>
 *             <th>Type</th>
 *             <th>Mapped to</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>{@link Artifact}</td>
 *             <td>{@link DefaultArtifact}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link ArtifactMetadata}</td>
 *             <td>{@link DefaultArtifactMetadata}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link PropertyDescriptor}</td>
 *             <td>{@link DefaultPropertyDescriptor}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link Release}</td>
 *             <td>{@link DefaultRelease}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link Manifest}</td>
 *             <td>{@link DefaultManifest}</td>
 *         </tr>
 *     </tbody>
 * </table>
 * <p>
 * The {@link ArtifactoryJacksonModule} would register the mixins for the following JSON schema
 * types that would be using the corresponding builder types to deserialize the JSON payloads:
 * <ul>
 *     <li>{@link JsonSchema}</li>
 *     <li>{@link ArraySchema}</li>
 *     <li>{@link BooleanSchema}</li>
 *     <li>{@link IntegerSchema}</li>
 *     <li>{@link NullSchema}</li>
 *     <li>{@link NumberSchema}</li>
 *     <li>{@link ObjectSchema}</li>
 *     <li>{@link StringSchema}</li>
 * </ul>
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public class ArtifactoryJacksonModule extends SimpleModule {

	public ArtifactoryJacksonModule() {
		super("artifactory-jackson-module");
	}

	@Override
	public void setupModule(SetupContext context) {
		addSerializer(JsonSchemaType.class, new JsonSchemaTypeSerializer());
		addDeserializer(JsonSchemaType.class, new JsonSchemaTypeDeserializer());

		addAbstractTypeMapping(Artifact.class, DefaultArtifact.class);
		addAbstractTypeMapping(ArtifactMetadata.class, DefaultArtifactMetadata.class);
		addAbstractTypeMapping(PropertyDescriptor.class, DefaultPropertyDescriptor.class);
		addAbstractTypeMapping(Release.class, DefaultRelease.class);
		addAbstractTypeMapping(Manifest.class, DefaultManifest.class);

		setMixInAnnotation(JsonSchema.class, JsonSchemaMixin.class);
		setMixInAnnotation(JsonSchema.Builder.class, JsonSchemaMixin.Builder.class);
		setMixInAnnotation(ArraySchema.class, ArraySchemaMixin.class);
		setMixInAnnotation(ArraySchema.Builder.class, ArraySchemaMixin.Builder.class);
		setMixInAnnotation(BooleanSchema.class, BooleanSchemaMixin.class);
		setMixInAnnotation(BooleanSchema.Builder.class, BooleanSchemaMixin.Builder.class);
		setMixInAnnotation(IntegerSchema.class, IntegerSchemaMixin.class);
		setMixInAnnotation(IntegerSchema.Builder.class, IntegerSchemaMixin.Builder.class);
		setMixInAnnotation(NullSchema.class, NullSchemaMixin.class);
		setMixInAnnotation(NullSchema.Builder.class, NullSchemaMixin.Builder.class);
		setMixInAnnotation(NumberSchema.class, NumberSchemaMixin.class);
		setMixInAnnotation(NumberSchema.Builder.class, NumberSchemaMixin.Builder.class);
		setMixInAnnotation(StringSchema.class, StringSchemaMixin.class);
		setMixInAnnotation(StringSchema.Builder.class, StringSchemaMixin.Builder.class);
		setMixInAnnotation(ObjectSchema.class, ObjectSchemaMixin.class);
		setMixInAnnotation(ObjectSchema.Builder.class, ObjectSchemaMixin.Builder.class);

		super.setupModule(context);
	}

	static final class JsonSchemaTypeSerializer extends StdScalarSerializer<JsonSchemaType> {

		JsonSchemaTypeSerializer() {
			super(JsonSchemaType.class);
		}

		@Override
		public void serialize(JsonSchemaType value, JsonGenerator gen, SerializationContext context) {
			gen.writeString(value.name().toLowerCase(context.getLocale()));
		}

	}

	static final class JsonSchemaTypeDeserializer extends StdScalarDeserializer<JsonSchemaType> {

		JsonSchemaTypeDeserializer() {
			super(JsonSchemaType.class);
		}

		@Override
		public JsonSchemaType deserialize(JsonParser parser, DeserializationContext context) {
			return JsonSchemaType.valueOf(parser.getValueAsString().toUpperCase(context.getLocale()));
		}
	}

	@JsonTypeInfo(
			use = JsonTypeInfo.Id.NAME,
			include = JsonTypeInfo.As.EXISTING_PROPERTY,
			property = "type",
			visible = true
	)
	@JsonSubTypes({
			@JsonSubTypes.Type(name = "array", value = ArraySchema.class),
			@JsonSubTypes.Type(name = "boolean", value = BooleanSchema.class),
			@JsonSubTypes.Type(name = "integer", value = IntegerSchema.class),
			@JsonSubTypes.Type(name = "null", value = NullSchema.class),
			@JsonSubTypes.Type(name = "number", value = NumberSchema.class),
			@JsonSubTypes.Type(name = "object", value = ObjectSchema.class),
			@JsonSubTypes.Type(name = "string", value = StringSchema.class)
	})
	@JsonInclude(
			value = JsonInclude.Include.NON_EMPTY,
			content = JsonInclude.Include.NON_NULL
	)
	@JsonAutoDetect(
			fieldVisibility = JsonAutoDetect.Visibility.ANY,
			getterVisibility = JsonAutoDetect.Visibility.NONE,
			setterVisibility = JsonAutoDetect.Visibility.NONE
	)
	static abstract class JsonSchemaMixin {

		@JsonProperty("type")
		protected JsonSchemaType type;

		@JsonProperty("enum")
		protected Collection<String> enumerations;

		static abstract class Builder {

			@JsonSetter("enum")
			abstract Builder enumerations(Collection<String> enumerations);

		}
	}

	@JsonDeserialize(builder = ArraySchema.Builder.class)
	static abstract class ArraySchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = BooleanSchema.Builder.class)
	static abstract class BooleanSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = IntegerSchema.Builder.class)
	static abstract class IntegerSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = NullSchema.Builder.class)
	static abstract class NullSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = NumberSchema.Builder.class)
	static abstract class NumberSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = StringSchema.Builder.class)
	static abstract class StringSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

		}
	}

	@JsonDeserialize(builder = ObjectSchema.Builder.class)
	static abstract class ObjectSchemaMixin {

		@JsonPOJOBuilder(withPrefix = "")
		static abstract class Builder {

			@JsonSetter("required")
			public abstract Builder required(Collection<String> required);

		}
	}

}
