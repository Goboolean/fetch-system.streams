package io.goboolean.streams.config;

import io.goboolean.streams.serde.AggregateSerde;
import io.goboolean.streams.serde.TradeSerde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerdeConfig {

    @Bean
    public AggregateSerde.JsonSerializer jsonAggregateSerializer() {
        return new AggregateSerde.JsonSerializer();
    }

    @Bean
    public AggregateSerde.JsonDeserializer jsonAggregateDeserializer() {
        return new AggregateSerde.JsonDeserializer();
    }

    @Bean
    public AggregateSerde.ProtobufSerializer protobufAggregateSerializer() {
        return new AggregateSerde.ProtobufSerializer();
    }

    @Bean
    public AggregateSerde.ProtobufDeserializer protobufAggregateDeserializer() {
        return new AggregateSerde.ProtobufDeserializer();
    }

    @Bean
    public TradeSerde.JsonSerializer jsonTradeSerializer() {
        return new TradeSerde.JsonSerializer();
    }

    @Bean
    public TradeSerde.JsonDeserializer jsonTradeDeserializer() {
        return new TradeSerde.JsonDeserializer();
    }

    @Bean
    public TradeSerde.ProtobufSerializer protobufTradeSerializer() {
        return new TradeSerde.ProtobufSerializer();
    }

    @Bean
    public TradeSerde.ProtobufDeserializer protobufTradeDeserializer() {
        return new TradeSerde.ProtobufDeserializer();
    }
}