package io.goboolean.streams.serde;

import org.apache.kafka.common.network.Mode;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

@ComponentScan
public class ProtobufAggregateSerde extends ProtobufSerde<Model.Aggregate> {
    public ProtobufAggregateSerde() {
        super(
                new ProtobufSerde.ProtobufSerializer<>(),
                new ProtobufSerde.ProtobufDeserializer<>(Model.Aggregate.parser())
        );
    }

    public static class ProtobufSerializer extends ProtobufSerde.ProtobufSerializer<Model.Aggregate> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, Model.Aggregate data) {
            return data == null ? null : data.toByteArray();
        }

        @Override
        public void close() {}
    }

    public static class ProtobufDeserializer extends ProtobufSerde.ProtobufDeserializer<Model.Aggregate> {

        public ProtobufDeserializer() {
            super(Model.Aggregate.parser());
        }
    }
}