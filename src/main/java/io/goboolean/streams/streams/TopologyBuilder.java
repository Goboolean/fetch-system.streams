package io.goboolean.streams.streams;

import io.goboolean.streams.serde.Model;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;

public class TopologyBuilder {

    StreamsBuilder builder = new StreamsBuilder();

    @Autowired
    Serde<Model.Trade> tradeSerde;
    @Autowired
    Serde<Model.Aggregate> aggregateSerde;

    public void addStreams(String id) {
        KStream<Integer, Model.Trade> sourceStream = builder.stream(String.format("%s.%s", id, "t"), Consumed.with(Serdes.Integer(), tradeSerde));

        KStream<Integer, Model.Aggregate> merged1sStream = sourceStream.transform(
                () -> new TradeTransformer()
        );
        merged1sStream.to(String.format("%s.%s", id, "1m"), Produced.with(Serdes.Integer(), aggregateSerde));

        KStream<Integer, Model.Aggregate> merged5sStream = merged1sStream.transform(
                () -> new AggregateTransformer(new TimeTruncator.FiveSecTruncator())
        );
        merged5sStream.to(String.format("%s.%s", id, "5m"), Produced.with(Serdes.Integer(), aggregateSerde));

        KStream<Integer, Model.Aggregate> merged1mStream = merged5sStream.transform(
                () -> new AggregateTransformer(new TimeTruncator.OneMinTruncator())
        );
        merged1mStream.to(String.format("%s.%s", id, "1m"), Produced.with(Serdes.Integer(), aggregateSerde));

        KStream<Integer, Model.Aggregate> merged5mStream = merged1mStream.transform(
                () -> new AggregateTransformer(new TimeTruncator.FiveMinTruncator())
        );
        merged5mStream.to(String.format("%s.%s", id, "5m"), Produced.with(Serdes.Integer(), aggregateSerde));
    }

    public Topology build() {
        return builder.build();
    }
}
