package io.goboolean.streams.streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

@SpringBootTest
public class TimeTruncationerTests {


    @Test
    public void truncate1s() {
        TimeTruncationer.Truncationer truncationer = new TimeTruncationer.OneSecTruncationer();

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:45.123Z")),
                ZonedDateTime.parse("2024-01-04T15:30:45Z")
        );
    }

    @Test
    public void truncate5s() {
        TimeTruncationer.Truncationer truncationer = new TimeTruncationer.FiveSecTruncationer();

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:45.123Z")),
                ZonedDateTime.parse("2024-01-04T15:30:45Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:49.12513Z")),
                ZonedDateTime.parse("2024-01-04T15:30:45Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:50.312Z")),
                ZonedDateTime.parse("2024-01-04T15:30:50Z")
        );
    }

    @Test
    public void truncate1m() {
        TimeTruncationer.Truncationer truncationer = new TimeTruncationer.OneMinTruncationer();

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:45.123Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:49.12513Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:50.312Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );
    }

    @Test
    public void truncate5m() {
        TimeTruncationer.Truncationer truncationer = new TimeTruncationer.FiveMinTruncationer();

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:45.123Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:49.12513Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );

        Assertions.assertEquals(
                truncationer.truncate(ZonedDateTime.parse("2024-01-04T15:30:50.312Z")),
                ZonedDateTime.parse("2024-01-04T15:30:00Z")
        );
    }
}
