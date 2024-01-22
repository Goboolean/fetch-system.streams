package io.goboolean.streams.config;

import io.goboolean.streams.kafka.KafkaAggregateProducerService;
import io.goboolean.streams.kafka.KafkaTradeProducerService;
import io.goboolean.streams.serde.AggregateSerde;
import io.goboolean.streams.serde.Model;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAggregateProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<Integer, Model.Aggregate> kafkaAggregateTemplate() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AggregateSerde.JsonSerializer.class.getName());

        ProducerFactory<Integer, Model.Aggregate> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAggregateProducerService kafkaAggregateProducerService() {
        return new KafkaAggregateProducerService(kafkaAggregateTemplate());
    }

}
