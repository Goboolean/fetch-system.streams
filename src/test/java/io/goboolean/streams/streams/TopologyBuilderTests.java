package io.goboolean.streams.streams;

import io.goboolean.streams.config.KafkaStreamsConfig;
import io.goboolean.streams.config.SerdeConfig;
import io.goboolean.streams.serde.Model;
import io.goboolean.streams.serde.TradeSerde;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootTest
@ContextConfiguration(classes = {SerdeConfig.class, KafkaStreamsConfig.class})
public class TopologyBuilderTests {

    private final String topic = "stock.goboolean.io";

    @Autowired
    Properties props;

    @Autowired
    TopologyBuilder topologyBuilder;

    @Autowired
    TradeSerde.JsonSerializer tradeSerializer;

    IntegerSerializer integerSerializer = new IntegerSerializer();

    private final List<Model.Trade> inputList = Arrays.asList(
            new Model.Trade(
                    "TRADE",
                    20,
                    1,
                    ZonedDateTime.parse("2024-01-04T15:30:45.123Z")
            )
    );

    TestInputTopic<Integer, Model.Trade> inputTopic;

    @Test
    public void testBuild() {
        topologyBuilder.addStreams(topic);
        Topology topology = topologyBuilder.build();

        TopologyTestDriver testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic(String.format("%s.%s", topic, "t"), integerSerializer, tradeSerializer);

        inputTopic.pipeInput(0, inputList.get(0));

        testDriver.close();
    }
}
