package io.goboolean.streams.serde;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import java.util.Map;

public class JsonSerde<T extends com.google.protobuf.GeneratedMessageV3> implements Serde<T> {
    private final Serializer<T> serializer;
    private final Deserializer<T> deserializer;

    public JsonSerde(Serializer<T> serializer, Deserializer<T> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }

    public static class JsonSerializer<T extends com.google.protobuf.GeneratedMessageV3> implements Serializer<T> {
        private final Gson gson = new Gson();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, T data) {

            JsonObject jsonObject = new JsonObject();

            for (FieldDescriptor field : data.getDescriptorForType().getFields()) {
                Object value = data.getField(field);

                switch (field.getType()) {
                    case INT32:
                    case INT64:
                    case FLOAT:
                    case DOUBLE:
                    case BOOL:
                        jsonObject.addProperty(field.getName(), (Number) value);
                        break;
                    case STRING:
                        jsonObject.addProperty(field.getName(), (String) value);
                        break;
                    default:
                        throw new RuntimeException("Unsupported type: " + field.getType());
                }
            }

            String jsonString = gson.toJson(jsonObject);
            return jsonString.getBytes();
        }

        @Override
        public void close() {}
    }

    public static class JsonDeserializer<T extends com.google.protobuf.GeneratedMessageV3> implements Deserializer<T> {
        private final Gson gson = new Gson();

        public JsonDeserializer() {}

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public T deserialize(String topic, byte[] data) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void close() {}
    }
}
