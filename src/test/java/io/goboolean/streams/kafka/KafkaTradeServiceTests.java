package io.goboolean.streams.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import io.goboolean.streams.serde.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.TestPropertySource;

import io.goboolean.streams.serde.TradeSerde.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
public class KafkaTradeServiceTests {

    @Autowired
    private KafkaTradeProducerService kafkaProducerService;

    @Test
    public void testProduceTrade() {
        Model.Trade trade = new Model.Trade();

        kafkaProducerService.produce("test.topic", 0, trade);
    }
}
