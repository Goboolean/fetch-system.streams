package io.goboolean.streams.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class KafkaStreamsService {

    @Autowired
    Properties props;

    @Autowired
    TopologyBuilder topologyBuilder;

    private KafkaStreams streams;

    public void addStreams(String productId) {
        topologyBuilder.addStreams(productId);
    }

    public void run() {
        streams = new KafkaStreams(topologyBuilder.build(), props);

        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
