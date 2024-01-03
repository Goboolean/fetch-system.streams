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
        System.out.println(newAggregate.timestamp());
        System.out.println(aggregate.timestamp());
        assert newAggregate.timestamp().equals(aggregate.timestamp());
    }
}
