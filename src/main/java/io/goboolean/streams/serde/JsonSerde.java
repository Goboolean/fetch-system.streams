package io.goboolean.streams.serde;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Map;

public class JsonSerde<T> implements Serde<T> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTimeAdapter())
            .create();
    private final Class<T> targetType;

    public JsonSerde(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public Serializer<T> serializer() {
        return new JsonSerializer();
    }

    @Override
    public Deserializer<T> deserializer() {
        return new JsonDeserializer();
    }

    protected class JsonSerializer implements Serializer<T> {
        @Override
        public byte[] serialize(String topic, T data) {
            return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        }
    }

    protected class JsonDeserializer implements Deserializer<T> {
        @Override
        public T deserialize(String topic, byte[] data) {
            if (data == null) {
                return null;
            }
            return gson.fromJson(new String(data, StandardCharsets.UTF_8), targetType);
        }
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}
