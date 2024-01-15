package io.goboolean.streams.config;

import io.goboolean.streams.serde.JsonSerde;
import io.goboolean.streams.serde.Model;
import io.goboolean.streams.streams.TopologyBuilder;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    @Value("${kafka.bootstrap-servers}")

    @Bean
    public Serde<Model.Trade> tradeSerde() {
        return new JsonSerde<>(Model.Trade.class);
    }

    @Bean
    public Serde<Model.Aggregate> aggregateSerde() {
        return new JsonSerde<>(Model.Aggregate.class);
    }

    @Bean
    public Properties props() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        return props;
    }

    @Bean
    public TopologyBuilder topologyBuilder() {
        return new TopologyBuilder();
    }
}
