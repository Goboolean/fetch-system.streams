package io.goboolean.streams.streams;

import io.goboolean.streams.serde.Model;
import org.apache.kafka.streams.KeyValue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
public class TradeTransformerTests {

    private TradeTransformer tradeTransformer;
    public TradeTransformerTests() {
        this.tradeTransformer = new TradeTransformer();
    }

    private ArrayList<Model.Trade> trades = new ArrayList<>(Arrays.asList(
            new Model.Trade(
                    "TRADE",
                    20,
                    1,
                    ZonedDateTime.parse("2024-01-04T15:30:45.123Z")
            ),
            new Model.Trade(
                    "TRADE",
                    10,
                    1,
                    ZonedDateTime.parse("2024-01-04T15:30:45.456Z")
            ),
            new Model.Trade(
                    "TRADE",
                    30,
                    1,
                    ZonedDateTime.parse("2024-01-04T15:30:46.2Z")
            )
    ));

    @Test
    public void testTransformScenario() {
        KeyValue<Integer, Model.Aggregate> kv0 = tradeTransformer.transform(0, trades.get(0));
        assert kv0 == null;

        KeyValue<Integer, Model.Aggregate> kv1 = tradeTransformer.transform(0, trades.get(1));
        assert kv1 == null;

        KeyValue<Integer, Model.Aggregate> kv2 = tradeTransformer.transform(0, trades.get(2));
        assert kv2.value.equals(new Model.Aggregate(
                "TRADE",
                20,
                10,
                20,
                10,
                15,
                2,
                ZonedDateTime.parse("2024-01-04T15:30:45Z")
        ));
    }
}
