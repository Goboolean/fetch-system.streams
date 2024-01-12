package io.goboolean.streams.streams;

import io.goboolean.streams.serde.Model;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeTransformer implements Transformer<Integer, Model.Trade, KeyValue<Integer, Model.Aggregate>> {
    private ProcessorContext context;

    public class StoreList extends ArrayList<Model.Trade> implements List<Model.Trade>  {

        private KeyValueStore<Integer, Model.Trade> stateStore;

        public StoreList(KeyValueStore<Integer, Model.Trade> stateStore) {
            this.stateStore = stateStore;
            stateStore.all().forEachRemaining(kv -> super.add(kv.value));
        }

        public boolean add(Model.Trade trade) {
            stateStore.put(trade.hashCode(), trade);
            return super.add(trade);
        }

        public void clear() {
            stateStore.all().forEachRemaining(kv -> stateStore.delete(kv.key));
            super.clear();
        }
    }

    private List<Model.Trade> trades;
    private ZonedDateTime roundedTime;

    private TimeTruncationer.Truncationer truncationer = new TimeTruncationer.OneSecTruncationer();

    public TradeTransformer(List<Model.Trade> trades, TimeTruncationer.Truncationer truncationer) {
        this.trades = trades;
        this.truncationer = truncationer;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public KeyValue<Integer, Model.Aggregate> transform(Integer key, Model.Trade value) {
        ZonedDateTime givenRoundedTime = truncationer.truncate(value.timestamp());

        if (roundedTime == null) { // Case when the first record is received
            roundedTime = givenRoundedTime;
            trades.add(value);
            return null;
        }

        if (roundedTime.equals(givenRoundedTime)) { // Case when the time has not changed
            trades.add(value);
            return null;
        }

        else { // Case when the time has changed
            long volume = this.trades.stream()
                    .mapToLong(Model.Trade::volume)
                    .sum();
            float average = (float) this.trades.stream()
                    .mapToDouble(a -> a.price() * a.volume())
                    .sum() / volume;
            float min = this.trades.stream()
                    .map(Model.Trade::price)
                    .min(Float::compareTo)
                    .get();
            float max = this.trades.stream()
                    .map(Model.Trade::price)
                    .max(Float::compareTo)
                    .get();

            Model.Aggregate aggregate = new Model.Aggregate(
                    value.symbol(),
                    trades.get(0).price(),
                    trades.get(trades.size() - 1).price(),
                    max,
                    min,
                    average,
                    volume,
                    roundedTime
            );

            trades.clear();
            trades.add(value);
            roundedTime = givenRoundedTime;

            return KeyValue.pair(key, aggregate);
        }
    }

    @Override
    public void close() {}
}
