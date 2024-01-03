package io.goboolean.streams.serde;

import java.time.ZonedDateTime;

public final class Model {

    public record Trade(
            String    symbol,
            float     price,
            long      volume,
            ZonedDateTime timestamp
    ) {
        public Trade() {
            this(null, 0, 0, null);
        }

        public Trade(String symbol, float price, long volume, ZonedDateTime timestamp) {
            this.symbol    = symbol;
            this.price     = price;
            this.volume    = volume;
            this.timestamp = timestamp;
        }
    }

    public record Aggregate(
            String    symbol,
            float     open,
            float     close,
            float     high,
            float     low,
            float     average,
            long      volume,
            ZonedDateTime timestamp
    ) {
        public Aggregate() {
            this(null, 0, 0, 0, 0, 0, 0, null);
        }

        public Aggregate(String symbol, float open, float close, float high, float low, float average, long volume, ZonedDateTime timestamp) {
            this.symbol    = symbol;
            this.open      = open;
            this.close     = close;
            this.high      = high;
            this.low       = low;
            this.average   = average;
            this.volume    = volume;
            this.timestamp = timestamp;
        }
    }
}
