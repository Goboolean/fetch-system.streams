package io.goboolean.streams.streams;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeTruncationer {

    public abstract static class AbstractTruncationer {
        public abstract ZonedDateTime truncate(ZonedDateTime time);
    }

    public interface Truncationer {
        ZonedDateTime truncate(ZonedDateTime time);
    }

    public static class OneSecTruncationer extends AbstractTruncationer implements Truncationer{
        private int unitValue;

        public OneSecTruncationer() {
            this.unitValue = 1;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withSecond(time.getSecond() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public static class FiveSecTruncationer extends AbstractTruncationer implements Truncationer {
        private int unitValue;

        public FiveSecTruncationer() {
            this.unitValue = 5;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withSecond(time.getSecond() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public static class OneMinTruncationer extends AbstractTruncationer implements Truncationer{
        private int unitValue;

        public OneMinTruncationer() {
            this.unitValue = 1;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withMinute(time.getMinute() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.MINUTES);
        }
    }

    public static class FiveMinTruncationer extends AbstractTruncationer implements Truncationer {
        private int unitValue;

        public FiveMinTruncationer() {
            this.unitValue = 5;
        }
        @Override
        public ZonedDateTime truncate(ZonedDateTime time) {
            return time.withMinute(time.getMinute() / unitValue * unitValue)
                    .truncatedTo(ChronoUnit.MINUTES);
        }
    }
}
