package io.goboolean.streams.serde;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SpringBootTest
public class TradeSerdeTests {

    @Autowired
    private TradeSerde.JsonSerializer jsonSerializer;

    @Autowired
    private TradeSerde.JsonDeserializer jsonDeserializer;

    @Autowired
    private TradeSerde.ProtobufSerializer protobufSerializer;

    @Autowired
    private TradeSerde.ProtobufDeserializer protobufDeserializer;

    @Test
    public void testJsonSerde() {
        Model.Trade trade = new Model.Trade(
                "test",
                1,
                2,
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        byte[] byteData = jsonSerializer.serialize("test.topic", trade);
        Model.Trade newTrade = jsonDeserializer.deserialize("test.topic", byteData);

        assert newTrade.symbol().equals(trade.symbol());
        assert newTrade.price() == trade.price();
        assert newTrade.volume() == trade.volume();
        assert newTrade.timestamp().equals(trade.timestamp());
    }

    @Test
    public void testProtobufSerde() {
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        ProtobufModel.TradeProtobuf trade = ProtobufModel.TradeProtobuf.newBuilder()
                .setPrice(1)
                .setSize(2)
                .setTimestamp(timestamp.toEpochSecond())
                .build();

        byte[] byteData = protobufSerializer.serialize("test.topic", trade);
        ProtobufModel.TradeProtobuf newTrade = protobufDeserializer.deserialize("test.topic", byteData);

        assert newTrade.getPrice() == trade.getPrice();
        assert newTrade.getSize() == trade.getSize();
        assert newTrade.getTimestamp() == trade.getTimestamp();
    }
}
