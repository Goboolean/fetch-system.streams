package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
@EnableKafka
public class KafkaProducerServiceTests {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaTemplate<String, Model.Trade> kafkaTemplate;

    @Test
    public void testProduce() {
        Model.Aggregate aggregate = Model.Aggregate.newBuilder()
                .setTimestamp(100)
                .build();

        kafkaProducerService.sendMessage(aggregate, "test.topic");
    }
}
