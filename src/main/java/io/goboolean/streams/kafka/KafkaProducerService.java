package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Model.Aggregate> kafkaTemplate;

    public void sendMessage(Model.Aggregate aggregate, String topicName) {
        kafkaTemplate.send(topicName, "default", aggregate);
    }
}
