package io.goboolean.streams.streams;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeTruncator {

    public abstract static class AbstractTruncator {
        public abstract ZonedDateTime truncate(ZonedDateTime time);
    }

    public interface Truncator {
        ZonedDateTime truncate(ZonedDateTime time);
    }

    public static class OneSecTruncator extends AbstractTruncator implements Truncator{
        private int unitValue;

        public OneSecTruncator() {
            this.unitValue = 1;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withSecond(time.getSecond() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public static class FiveSecTruncator extends AbstractTruncator implements Truncator {
        private int unitValue;

        public FiveSecTruncator() {
            this.unitValue = 5;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withSecond(time.getSecond() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public static class OneMinTruncator extends AbstractTruncator implements Truncator{
        private int unitValue;

        public OneMinTruncator() {
            this.unitValue = 1;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withMinute(time.getMinute() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.MINUTES);
        }
    }

    public static class FiveMinTruncator extends AbstractTruncator implements Truncator {
        private int unitValue;

        public FiveMinTruncator() {
            this.unitValue = 5;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withMinute(time.getMinute() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.MINUTES);
        }
    }
}
