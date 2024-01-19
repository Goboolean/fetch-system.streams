package io.goboolean.streams.scheduler;

import io.goboolean.streams.etcd.EtcdService;
import io.goboolean.streams.etcd.Product;
import io.goboolean.streams.streams.KafkaStreamsService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerService {

    private static final Logger logger = LogManager.getLogger(EtcdService.class);

    @Autowired
    private EtcdService etcdService;

    @Autowired
    private KafkaStreamsService kafkaStreamsService;

    @PostConstruct
    public void run() {
        List<Product> products = etcdService.getAllProducts();

        ThreadContext.put("number", String.valueOf(products.size()));
        logger.info("Products fetched");

        products.forEach(product -> {
            kafkaStreamsService.addStreams(product.getId());
        });

        kafkaStreamsService.run();

        logger.info("Kafka Streams started");
        ThreadContext.clearAll();
    }
}