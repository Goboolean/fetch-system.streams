package io.goboolean.streams.scheduler;

import io.goboolean.streams.etcd.EtcdService;
import io.goboolean.streams.streams.KafkaStreamsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService {

    @Autowired
    private EtcdService etcdService;

    @Autowired
    private KafkaStreamsService kafkaStreamsService;

    @PostConstruct
    public void run() {
        etcdService.getAllProducts().forEach(product -> {
            kafkaStreamsService.addStreams(product.getId());
        });

        kafkaStreamsService.run();
    }
}