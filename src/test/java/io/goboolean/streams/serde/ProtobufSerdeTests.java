package io.goboolean.streams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.goboolean.streams.serde.Model.Trade;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProtobufSerdeTests {

    @Test
    public void testTradeSerde() throws Exception {

        Trade trade = Trade.newBuilder()
                .setPrice(1000.05)
                .setSize(3)
                .setTimestamp(System.currentTimeMillis())
                .build();

        byte[] bytes = trade.toByteArray();

        Trade newTrade;
        try {
            newTrade = Trade.parseFrom(bytes);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            e.printStackTrace();
            throw new Exception("Failed to parse protobuf data", e);
        }

        assertEquals(trade.getPrice(), newTrade.getPrice());
        assertEquals(trade.getSize(), newTrade.getSize());
        assertEquals(trade.getTimestamp(), newTrade.getTimestamp());
        assertEquals(trade, newTrade);
    }

    @Test
    public void testXXX() {

        Trade trade = Trade.newBuilder()
                .setPrice(1000.05)
                .setSize(3)
                .setTimestamp(System.currentTimeMillis())
                .build();

        ProtobufSerde<Trade> serde = new ProtobufSerde<>(
                new ProtobufSerde.ProtobufSerializer<>(),
                new ProtobufSerde.ProtobufDeserializer<>(Trade.parser())
        );

        Serializer<Trade> serializer = serde.serializer();
        Deserializer<Trade> deserializer = serde.deserializer();

        byte[] bytes = serializer.serialize("topic", trade);
        Trade newTrade = deserializer.deserialize("topic", bytes);

        assertEquals(trade.getPrice(), newTrade.getPrice());
        assertEquals(trade.getSize(), newTrade.getSize());
        assertEquals(trade.getTimestamp(), newTrade.getTimestamp());
        assertEquals(trade, newTrade);

    }
}
