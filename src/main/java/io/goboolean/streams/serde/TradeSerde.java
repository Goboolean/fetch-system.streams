package io.goboolean.streams.serde;

public class TradeSerde {

    private static final JsonSerde<io.goboolean.streams.serde.Model.Trade> jsonSerdeInstance = new JsonSerde<>(Model.Trade.class);

    public static class JsonSerializer extends JsonSerde<Model.Trade>.JsonSerializer {
        public JsonSerializer() {
            jsonSerdeInstance.super();
        }
    }

    public static class JsonDeserializer extends JsonSerde<Model.Trade>.JsonDeserializer {
        public JsonDeserializer() {
            jsonSerdeInstance.super();
        }
    }

    public static class ProtobufSerializer extends ProtobufSerde.ProtobufSerializer<ProtobufModel.TradeProtobuf> {
        public ProtobufSerializer() {
            super();
        }
    }

    public static class ProtobufDeserializer extends ProtobufSerde.ProtobufDeserializer<ProtobufModel.TradeProtobuf> {
        public ProtobufDeserializer() {
            super(ProtobufModel.TradeProtobuf.parser());
        }
    }
}
