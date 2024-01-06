package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaAggregateProducerService {

    private KafkaTemplate<Integer, Model.Aggregate> kafkaTemplate;

    @Autowired
    public KafkaAggregateProducerService(KafkaTemplate<Integer, Model.Aggregate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String topic, Integer timestamp, Model.Aggregate aggregate) {
        kafkaTemplate.send(topic, timestamp, aggregate);
    }
}
