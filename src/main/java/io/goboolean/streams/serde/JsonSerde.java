package io.goboolean.streams.serde;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import java.util.Map;

public class JsonSerde<T> implements Serde<T> {
    private final Serializer<T> serializer;
    private final Deserializer<T> deserializer;

    public JsonSerde(Class<T> classType) {
        this.serializer = new JsonSerializer<>();
        this.deserializer = new JsonDeserializer<>(classType);
    }

    @Override
    public Serializer<T> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }

    public static class JsonSerializer<T> implements Serializer<T> {
        private final Gson gson = new Gson();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, T data) {
            return data == null ? null : gson.toJson(data).getBytes();
        }

        @Override
        public void close() {}
    }

    public static class JsonDeserializer<T> implements Deserializer<T> {
        private final Class<T> classType;
        private final Gson gson = new Gson();

        public JsonDeserializer(Class<T> classType) {
            this.classType = classType;
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public T deserialize(String topic, byte[] data) {
            try {
                return data == null ? null : gson.fromJson(new String(data), classType);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize JSON data", e);
            }
        }

        @Override
        public void close() {}
    }
}
