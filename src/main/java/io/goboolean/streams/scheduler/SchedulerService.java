package io.goboolean.streams.scheduler;

import io.goboolean.streams.etcd.EtcdClient;
import io.goboolean.streams.streams.KafkaStreamsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService {

    @Autowired
    private EtcdClient etcdClient;

    @Autowired
    private KafkaStreamsService kafkaStreamsService;

    @PostConstruct
    public void run() {
        etcdClient.getAllProducts().forEach(product -> {
            kafkaStreamsService.addStreams(product.getId());
        });

        kafkaStreamsService.run();
    }
}