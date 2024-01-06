package io.goboolean.streams.kafka;

import io.goboolean.streams.serde.Model;

public interface KafkaConsumerListener {
    void onMessage(Model.Aggregate aggregate);
}
