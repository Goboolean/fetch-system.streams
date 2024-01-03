package io.goboolean.streams.serde;

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
}
