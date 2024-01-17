package io.goboolean.streams.streams;


import io.goboolean.streams.serde.Model;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AggregateTransformerTests {

    private AggregateTransformer aggregateTransformer;
    private final String symbol = "stock.goboolean.io";

    private List<Model.Aggregate> aggregates = Arrays.asList(
            new Model.Aggregate(
                    symbol,
                    20,
                    10,
                    30,
                    10,
                    15,
                    2,
                    ZonedDateTime.parse("2024-01-04T15:30:45Z")
            ),
            new Model.Aggregate(
                    symbol,
                    30,
                    30,
                    40,
                    20,
                    30,
                    4,
                    ZonedDateTime.parse("2024-01-04T15:30:47Z")
            ),
            new Model.Aggregate(
                    symbol,
                    40,
                    30,
                    50,
                    30,
                    35,
                    2,
                    ZonedDateTime.parse("2024-01-04T15:30:51Z")
            ),
            new Model.Aggregate(
                    symbol,
                    50,
                    20,
                    60,
                    20,
                    55,
                    2,
                    ZonedDateTime.parse("2024-01-04T15:30:54Z")
            )
    );
    private List<Model.Aggregate> result = Arrays.asList(
            new Model.Aggregate(
                    symbol,
                    20,
                    30,
                    40,
                    10,
                    25,
                    6,
                    ZonedDateTime.parse("2024-01-04T15:30:45Z")
            ),
            new Model.Aggregate(
                    symbol,
                    40,
                    20,
                    60,
                    20,
                    45,
                    4,
                    ZonedDateTime.parse("2024-01-04T15:30:50Z")
            )
    );

    public AggregateTransformerTests() {
        this.aggregateTransformer = new AggregateTransformer(
                "",
                new TimeTruncationer.FiveSecTruncationer(), Duration.ofSeconds(1), null);
    }

    @Test
    public void testTransformScenario() {

        KeyValue<Integer, Model.Aggregate> kv0 = aggregateTransformer.transform(0, aggregates.get(0));
        Assertions.assertNull(kv0);

        KeyValue<Integer, Model.Aggregate> kv1 = aggregateTransformer.transform(0, aggregates.get(1));
        Assertions.assertNull(kv1);

        KeyValue<Integer, Model.Aggregate> kv2 = aggregateTransformer.transform(0, aggregates.get(2));
        Assertions.assertNotNull(kv2);
        Assertions.assertEquals(kv2.value, result.get(0));

        KeyValue<Integer, Model.Aggregate> kv3 = aggregateTransformer.transform(0, aggregates.get(3));
        Assertions.assertNotNull(kv3);
        Assertions.assertEquals(kv3.value, result.get(1));
    }
}
