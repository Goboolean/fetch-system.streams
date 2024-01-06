package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaAggregateServiceTests {

    @Autowired
    private KafkaAggregateProducerService kafkaAggregateProducerService;

    @Test
    public void testProduceAggregate() {
        Model.Aggregate aggregate = new Model.Aggregate();

        kafkaAggregateProducerService.produce("test.topic", 0, aggregate);
    }
}
