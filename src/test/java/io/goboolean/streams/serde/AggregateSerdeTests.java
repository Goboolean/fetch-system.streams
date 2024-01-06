package io.goboolean.streams.serde;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SpringBootTest
public class AggregateSerdeTests {

    @Autowired
    private AggregateSerde.JsonSerializer jsonSerializer;

    @Autowired
    private AggregateSerde.JsonDeserializer jsonDeserializer;

    @Autowired
    private AggregateSerde.ProtobufSerializer protobufSerializer;

    @Autowired
    private AggregateSerde.ProtobufDeserializer protobufDeserializer;


    @Test
    public void testJsonSerde() {
        Model.Aggregate aggregate = new Model.Aggregate(
                "test",
                1,
                2,
                3,
                4,
                5,
                6,
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        byte[] byteData = jsonSerializer.serialize("test.topic", aggregate);
        Model.Aggregate newAggregate = jsonDeserializer.deserialize("test.topic", byteData);

        assert newAggregate.symbol().equals(aggregate.symbol());
        assert newAggregate.open() == aggregate.open();
        assert newAggregate.close() == aggregate.close();
        assert newAggregate.high() == aggregate.high();
        assert newAggregate.low() == aggregate.low();
        assert newAggregate.average() == aggregate.average();
        assert newAggregate.volume() == aggregate.volume();
        assert newAggregate.timestamp().equals(aggregate.timestamp());
    }

    @Test
    public void testProtobufSerde() {
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        ProtobufModel.AggregateProtobuf aggregate = ProtobufModel.AggregateProtobuf.newBuilder()
                .setOpen(1)
                .setClosed(2)
                .setMax(3)
                .setMin(4)
                .setVolume(5)
                .setTimestamp(timestamp.toEpochSecond() * 1_000_000_000L + timestamp.getNano())
                .build();

        byte[] byteData = protobufSerializer.serialize("test.topic", aggregate);
        ProtobufModel.AggregateProtobuf newAggregate = protobufDeserializer.deserialize("test.topic", byteData);

        assert newAggregate.getOpen() == aggregate.getOpen();
        assert newAggregate.getClosed() == aggregate.getClosed();
        assert newAggregate.getMax() == aggregate.getMax();
        assert newAggregate.getMin() == aggregate.getMin();
        assert newAggregate.getVolume() == aggregate.getVolume();
        assert newAggregate.getTimestamp() == aggregate.getTimestamp();
    }
}
