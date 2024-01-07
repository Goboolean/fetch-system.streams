package io.goboolean.streams.streams;

import io.goboolean.streams.serde.Model;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AggregateTransformer implements Transformer<Integer, Model.Aggregate, KeyValue<Integer, Model.Aggregate>> {
    private ProcessorContext context;

    private ArrayList<Model.Aggregate> aggregates = new ArrayList<>();
    private ZonedDateTime roundedTime;

    private TimeTruncator.Truncator truncator;

    public AggregateTransformer(TimeTruncator.Truncator truncator) {
        this.truncator = truncator;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public KeyValue<Integer, Model.Aggregate> transform(Integer key, Model.Aggregate value) {
        ZonedDateTime givenRoundedTime = truncator.truncate(value.timestamp());

        if (roundedTime == null) { // Case when the first record is received
            roundedTime = givenRoundedTime;
            aggregates.add(value);
            return null;
        }

        if (roundedTime.equals(givenRoundedTime)) { // Case when the time has not changed
            aggregates.add(value);
            return null;
        }

        // Case when the time has been changed
        long volume = aggregates.stream()
                .mapToLong(Model.Aggregate::volume)
                .sum();
        float average = (float) aggregates.stream()
                .mapToDouble(a -> a.average() * a.volume())
                .sum() / volume;

        Model.Aggregate merged = aggregates.stream()
                .reduce((a1, a2) -> new Model.Aggregate(
                        a1.symbol(),
                        a1.open(),
                        a2.close(),
                        Math.max(a1.high(), a2.high()),
                        Math.min(a1.low(), a2.low()),
                        average,
                        volume,
                        roundedTime
                ))
                .get();

        aggregates.clear();
        aggregates.add(value);
        roundedTime = givenRoundedTime;

        return KeyValue.pair(key, merged);
    }

    @Override
    public void close() {}
}
