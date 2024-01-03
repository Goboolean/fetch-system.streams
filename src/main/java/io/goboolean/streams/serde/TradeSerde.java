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
}
