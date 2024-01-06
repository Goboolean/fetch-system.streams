package io.goboolean.streams.serde;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class AggregateSerde {

    private static final JsonSerde<Model.Aggregate> jsonSerdeInstance = new JsonSerde<>(Model.Aggregate.class);

    public static class JsonSerializer extends JsonSerde<Model.Aggregate>.JsonSerializer {
        public JsonSerializer() {
            jsonSerdeInstance.super();
        }
    }

    public static class JsonDeserializer extends JsonSerde<Model.Aggregate>.JsonDeserializer {
        public JsonDeserializer() {
            jsonSerdeInstance.super();
        }
    }

    public static class ProtobufSerializer extends ProtobufSerde.ProtobufSerializer<ProtobufModel.AggregateProtobuf> {
        public ProtobufSerializer() {
            super();
        }
    }

    public static class ProtobufDeserializer extends ProtobufSerde.ProtobufDeserializer<ProtobufModel.AggregateProtobuf> {
        public ProtobufDeserializer() {
            super(ProtobufModel.AggregateProtobuf.parser());
        }
    }
}
