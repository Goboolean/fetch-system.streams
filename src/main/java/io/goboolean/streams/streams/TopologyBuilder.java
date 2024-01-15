package io.goboolean.streams.streams;

import io.goboolean.streams.serde.Model;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class TopologyBuilder {

    StreamsBuilder builder = new StreamsBuilder();

    @Autowired
    Serde<Model.Trade> tradeSerde;
    @Autowired
    Serde<Model.Aggregate> aggregateSerde;

    public void addStreams(String id) {

        String topic_t = String.format("%s.%s", id, "t");

        KStream<Integer, Model.Trade> sourceStream = builder.stream(topic_t, Consumed.with(Serdes.Integer(), tradeSerde));

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(topic_t),
                        Serdes.Integer(),
                        tradeSerde));

        KStream<Integer, Model.Aggregate> merged1sStream = sourceStream.transform(
                () -> new TradeTransformer(topic_t), topic_t);


        String topic_1s = String.format("%s.%s", id, "1s");
        merged1sStream.to(topic_1s, Produced.with(Serdes.Integer(), aggregateSerde));

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(topic_1s),
                        Serdes.Integer(),
                        aggregateSerde));

        KStream<Integer, Model.Aggregate> merged5sStream = merged1sStream.transform(
                () -> new AggregateTransformer(
                        topic_1s, new TimeTruncationer.FiveSecTruncationer(), Duration.ofSeconds(1)), topic_1s);


        String topic_5s = String.format("%s.%s", id, "5s");
        merged5sStream.to(topic_5s, Produced.with(Serdes.Integer(), aggregateSerde));

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(topic_5s),
                        Serdes.Integer(),
                        aggregateSerde));

        KStream<Integer, Model.Aggregate> merged1mStream = merged5sStream.transform(
                () -> new AggregateTransformer(
                        topic_5s, new TimeTruncationer.OneMinTruncationer(), Duration.ofSeconds(5)), topic_5s);


        String topic_1m = String.format("%s.%s", id, "1m");
        merged1mStream.to(topic_1m, Produced.with(Serdes.Integer(), aggregateSerde));

        builder.addStateStore(
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(topic_1m),
                        Serdes.Integer(),
                        aggregateSerde));

        KStream<Integer, Model.Aggregate> merged5mStream = merged1mStream.transform(
                () -> new AggregateTransformer(
                        topic_1m, new TimeTruncationer.FiveMinTruncationer(), Duration.ofMinutes(1)), topic_1m);


        String topic_5m = String.format("%s.%s", id, "5m");
        merged5mStream.to(topic_5m, Produced.with(Serdes.Integer(), aggregateSerde));
    }

    public Topology build() {
        return builder.build();
    }
}
