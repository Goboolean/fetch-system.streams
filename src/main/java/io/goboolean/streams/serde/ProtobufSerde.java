package io.goboolean.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

@ComponentScan

public class ProtobufSerde<T extends com.google.protobuf.GeneratedMessageV3> implements Serde<T> {
    private final Serializer<T> serializer;
    private final Deserializer<T> deserializer;

    public ProtobufSerde(Serializer<T> serializer, Deserializer<T> deserializer) {
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

    public static class ProtobufSerializer<T extends com.google.protobuf.GeneratedMessageV3> implements Serializer<T> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, T data) {
            return data == null ? null : data.toByteArray();
        }

        @Override
        public void close() {}
    }

    public static class ProtobufDeserializer<T extends com.google.protobuf.GeneratedMessageV3> implements Deserializer<T> {
        private final com.google.protobuf.Parser<T> parser;

        public ProtobufDeserializer(com.google.protobuf.Parser<T> parser) {
            this.parser = parser;
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public T deserialize(String topic, byte[] data) {
            try {
                return data == null ? null : parser.parseFrom(data);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw new RuntimeException("Failed to deserialize data", e);
            }
        }

        @Override
        public void close() {}
    }
}
