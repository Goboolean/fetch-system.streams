package io.goboolean.streams.streams;


import io.goboolean.streams.serde.Model;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
public class AggregateTransformerTests {

    private AggregateTransformer aggregateTransformer;

    private ArrayList<Model.Aggregate> aggregates = new ArrayList<>(
            Arrays.asList(
                    new Model.Aggregate(
                            "TRADE",
                            20,
                            10,
                            30,
                            10,
                            15,
                            2,
                            ZonedDateTime.parse("2024-01-04T15:30:45Z")
                    ),
                    new Model.Aggregate(
                            "TRADE",
                            30,
                            30,
                            40,
                            20,
                            30,
                            4,
                            ZonedDateTime.parse("2024-01-04T15:30:47Z")
                    ),
                    new Model.Aggregate(
                            "TRADE",
                            40,
                            30,
                            50,
                            30,
                            35,
                            2,
                            ZonedDateTime.parse("2024-01-04T15:30:51Z")
                    )
            )

    );

    public AggregateTransformerTests() {
        this.aggregateTransformer = new AggregateTransformer(new TimeTruncator.FiveSecTruncator());
    }

    @Test
    public void testTransformScenario() {

        KeyValue<Integer, Model.Aggregate> kv0 = aggregateTransformer.transform(0, aggregates.get(0));
        assert kv0 == null;

        KeyValue<Integer, Model.Aggregate> kv1 = aggregateTransformer.transform(0, aggregates.get(1));
        assert kv1 == null;

        KeyValue<Integer, Model.Aggregate> kv2 = aggregateTransformer.transform(0, aggregates.get(2));
        assert kv2.value.equals(new Model.Aggregate(
                "TRADE",
                20,
                30,
                40,
                10,
                25,
                6,
                ZonedDateTime.parse("2024-01-04T15:30:45Z")
        ));
    }
}
