package io.goboolean.streams.streams;

import io.goboolean.streams.etcd.EtcdService;
import io.goboolean.streams.etcd.Product;
import io.goboolean.streams.kafka.KafkaTradeProducerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaStreamsTests {

    @Autowired
    private KafkaStreamsService kafkaStreamsService;

    @Autowired
    private EtcdService etcdClient;

    @Autowired
    private KafkaTradeProducerService kafkaTradeProducerService;

    private final String id = "stock.goboolean.io";
    private final String platform = "test";
    private final String symbol = "goboolean";
    private final String locale = "io";
    private final String market = "stock";

    @BeforeEach
    public void setUp() {
        etcdClient.insertProduct(new Product(id, platform, symbol, locale, market));
    }

    @AfterEach
    public void tearDown() {
        etcdClient.deleteAllProducts();
    }

    @Test
    public void testRun() {
        kafkaStreamsService.addStreams(id);
        kafkaStreamsService.run();
    }
}
