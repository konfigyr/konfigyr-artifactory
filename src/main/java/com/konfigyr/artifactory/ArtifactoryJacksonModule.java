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
import java.util.Locale;

/**
 * Jackson module for {@code konfigyr-artifactory}.
 * <p>
 * This module would register the following abstract type mappings:
 * <table>
 *     <caption>Type mappings</caption>
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
 *             <td>{@link Publication}</td>
 *             <td>{@link DefaultPublication}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link Manifest}</td>
 *             <td>{@link DefaultManifest}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link ManifestEntry}</td>
 *             <td>{@link DefaultManifestEntry}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link ServiceRelease}</td>
 *             <td>{@link DefaultServiceRelease}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link ServiceReleaseEntry}</td>
 *             <td>{@link DefaultServiceReleaseEntry}</td>
 *         </tr>
 *         <tr>
 *             <td>{@link ServiceReleaseCandidate}</td>
 *             <td>{@link DefaultServiceReleaseCandidate}</td>
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
 * <p>
 * Instances are stateless once constructed: {@link #setupModule(SetupContext)} only registers the
 * mappings above and does not retain any mutable state afterward. It is therefore safe to construct
 * a single instance and share it across threads, e.g. as a singleton Spring bean registered on a
 * shared {@code JsonMapper}/{@code ObjectMapper}.
 *
 * @author Vladimir Spasic
 * @since 1.0.0
 */
public class ArtifactoryJacksonModule extends SimpleModule {

	/**
	 * Creates a new instance of the {@link ArtifactoryJacksonModule} with the following name:
	 * {@code artifactory-jackson-module} and unspecified {@link tools.jackson.core.Version}.
	 */
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
		addAbstractTypeMapping(Publication.class, DefaultPublication.class);
		addAbstractTypeMapping(Manifest.class, DefaultManifest.class);
		addAbstractTypeMapping(ManifestEntry.class, DefaultManifestEntry.class);
		addAbstractTypeMapping(ServiceRelease.class, DefaultServiceRelease.class);
		addAbstractTypeMapping(ServiceReleaseEntry.class, DefaultServiceReleaseEntry.class);
		addAbstractTypeMapping(ServiceReleaseCandidate.class, DefaultServiceReleaseCandidate.class);

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
			gen.writeString(value.name().toLowerCase(Locale.ROOT));
		}

	}

	static final class JsonSchemaTypeDeserializer extends StdScalarDeserializer<JsonSchemaType> {

		JsonSchemaTypeDeserializer() {
			super(JsonSchemaType.class);
		}

		@Override
		public JsonSchemaType deserialize(JsonParser parser, DeserializationContext context) {
			return JsonSchemaType.valueOf(parser.getValueAsString().toUpperCase(Locale.ROOT));
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
