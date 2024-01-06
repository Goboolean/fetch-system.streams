package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTradeProducerService {

    private KafkaTemplate<Integer, Model.Trade> kafkaTemplate;

    @Autowired
    public KafkaTradeProducerService(KafkaTemplate<Integer, Model.Trade> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(String topic, Integer timestamp, Model.Trade trade) {
        kafkaTemplate.send(topic, timestamp, trade);
    }
}
