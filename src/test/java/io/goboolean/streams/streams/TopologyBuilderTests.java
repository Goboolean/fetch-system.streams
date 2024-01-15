package io.goboolean.streams.streams;

import io.goboolean.streams.config.KafkaStreamsConfig;
import io.goboolean.streams.config.SerdeConfig;
import io.goboolean.streams.serde.Model;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Assertions;
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
    Serde<Model.Trade> tradeSerde;

    @Autowired
    Serde<Model.Aggregate> aggregateSerde;

    Serde<Integer> integerSerde = new Serdes.IntegerSerde();

    Topology topology;
    TopologyTestDriver testDriver;

    TestInputTopic<Integer, Model.Trade> inputTopic;
    TestOutputTopic<Integer, Model.Aggregate> output1sTopic;
    TestOutputTopic<Integer, Model.Aggregate> output5sTopic;
    TestOutputTopic<Integer, Model.Aggregate> output1mTopic;
    TestOutputTopic<Integer, Model.Aggregate> output5mTopic;

    String topic_t = String.format("%s.%s", topic, "t");
    String topic_1s = String.format("%s.%s", topic, "1s");
    String topic_5s = String.format("%s.%s", topic, "5s");
    String topic_1m = String.format("%s.%s", topic, "1m");
    String topic_5m = String.format("%s.%s", topic, "5m");

    KeyValueStore<Integer, Model.Trade> store_t;
    KeyValueStore<Integer, Model.Aggregate> store_1s;
    KeyValueStore<Integer, Model.Aggregate> store_5s;
    KeyValueStore<Integer, Model.Aggregate> store_1m;


    private final List<Model.Trade> inputList = Arrays.asList(
            new Model.Trade(topic, 20, 1, ZonedDateTime.parse("2024-01-04T15:30:45.123Z")),
            new Model.Trade(topic, 40, 3, ZonedDateTime.parse("2024-01-04T15:30:45.456Z")),
            new Model.Trade(topic, 35, 5, ZonedDateTime.parse("2024-01-04T15:30:46.789Z")),
            new Model.Trade(topic, 35, 1, ZonedDateTime.parse("2024-01-04T15:30:47.312Z")),
            new Model.Trade(topic, 95, 10, ZonedDateTime.parse("2024-01-04T15:30:49.512Z")),
            new Model.Trade(topic, 120, 11, ZonedDateTime.parse("2024-01-04T15:30:51.789Z"))
    );

    private final List<Model.Aggregate> output1sList = Arrays.asList(
            new Model.Aggregate(topic, 20, 40, 40, 20, 35, 4, ZonedDateTime.parse("2024-01-04T15:30:45Z")),
            new Model.Aggregate(topic, 35, 35, 35, 35, 35, 5, ZonedDateTime.parse("2024-01-04T15:30:46Z")),
            new Model.Aggregate(topic, 35, 35, 35, 35, 35, 1, ZonedDateTime.parse("2024-01-04T15:30:47Z")),
            new Model.Aggregate(topic, 95, 95, 95, 95, 95, 10, ZonedDateTime.parse("2024-01-04T15:30:49Z")),
            new Model.Aggregate(topic, 120, 120, 120, 120, 120, 11, ZonedDateTime.parse("2024-01-04T15:30:51Z"))
    );

    private final List<Model.Aggregate> output5sList = Arrays.asList(
            new Model.Aggregate(topic, 20, 95, 95, 20, 65, 20, ZonedDateTime.parse("2024-01-04T15:30:45Z"))
            //new Model.Aggregate(topic, 100, 100, 100, 100, 100, 9, ZonedDateTime.parse("2024-01-04T15:30:50Z"))
    );

    private final List<Model.Aggregate> output1mList = Arrays.asList(
            new Model.Aggregate(
                    topic,
                    20,
                    60,
                    60,
                    20,
                    40,
                    9,
                    ZonedDateTime.parse("2024-01-04T15:30:45Z")
            )
    );

    private final List<Model.Aggregate> output5mList = Arrays.asList(
            new Model.Aggregate(
                    topic,
                    20,
                    60,
                    60,
                    20,
                    40,
                    9,
                    ZonedDateTime.parse("2024-01-04T15:30:45Z")
            )
    );



    private boolean includes(KeyValueStore<Integer, Model.Trade> store, Model.Trade trade) {
        KeyValueIterator<Integer, Model.Trade> iterator = store.all();
        while (iterator.hasNext()) {
            KeyValue<Integer, Model.Trade> next = iterator.next();
            if (next.value.equals(trade)) {
                return true;
            }
        }
        return false;
    }

    private boolean includes(KeyValueStore<Integer, Model.Aggregate> store, Model.Aggregate aggregate) {
        KeyValueIterator<Integer, Model.Aggregate> iterator = store.all();
        while (iterator.hasNext()) {
            KeyValue<Integer, Model.Aggregate> next = iterator.next();
            if (next.value.equals(aggregate)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testBuild() {
        topologyBuilder.addStreams(topic);
        topology = topologyBuilder.build();

        testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic(topic_t, integerSerde.serializer(), tradeSerde.serializer());
        output1sTopic = testDriver.createOutputTopic(topic_1s, integerSerde.deserializer(), aggregateSerde.deserializer());
        output5sTopic = testDriver.createOutputTopic(topic_5s, integerSerde.deserializer(), aggregateSerde.deserializer());
        output1mTopic = testDriver.createOutputTopic(topic_1m, integerSerde.deserializer(), aggregateSerde.deserializer());
        output5mTopic = testDriver.createOutputTopic(topic_5m, integerSerde.deserializer(), aggregateSerde.deserializer());

        store_t = testDriver.getKeyValueStore(topic_t);
        store_1s = testDriver.getKeyValueStore(topic_1s);
        store_5s = testDriver.getKeyValueStore(topic_5s);
        store_1m = testDriver.getKeyValueStore(topic_1m);

        // first and second trade data is inserted
        inputTopic.pipeInput(0, inputList.get(0));
        Assertions.assertTrue(includes(store_t, inputList.get(0)));
        inputTopic.pipeInput(0, inputList.get(1));
        Assertions.assertTrue(includes(store_t, inputList.get(1)));

        Assertions.assertTrue(output1sTopic.isEmpty());

        // 1s later timestamp trade data is inserted, 1s aggregate is generated
        inputTopic.pipeInput(0, inputList.get(2));
        Assertions.assertTrue(includes(store_t, inputList.get(2)));
        Assertions.assertFalse(includes(store_t, inputList.get(0)));
        Assertions.assertFalse(includes(store_t, inputList.get(1)));
        Assertions.assertTrue(includes(store_t, inputList.get(2)));

        Assertions.assertFalse(output1sTopic.isEmpty());
        Assertions.assertEquals(output1sTopic.readValue(), output1sList.get(0));
        Assertions.assertTrue(output1sTopic.isEmpty());
        Assertions.assertTrue(output5sTopic.isEmpty());

        // 1s aggregate is keep generating
        inputTopic.pipeInput(0, inputList.get(3));
        Assertions.assertTrue(includes(store_t, inputList.get(3)));
        inputTopic.pipeInput(0, inputList.get(4));
        Assertions.assertTrue(includes(store_t, inputList.get(4)));

        Assertions.assertEquals(output1sTopic.readValue(), output1sList.get(1));
        Assertions.assertEquals(output1sTopic.readValue(), output1sList.get(2));
        Assertions.assertTrue(output1sTopic.isEmpty());

        Assertions.assertTrue(output5sTopic.isEmpty());

        // 5s later timestamp trade data is inserted, 5s aggregate is generated
        inputTopic.pipeInput(0, inputList.get(5));
        Assertions.assertTrue(includes(store_t, inputList.get(5)));

        Assertions.assertFalse(output5sTopic.isEmpty());
        Assertions.assertEquals(output5sTopic.readValue(), output5sList.get(0));
        Assertions.assertTrue(output5sTopic.isEmpty());

        testDriver.close();

        /// ...
    }
}
