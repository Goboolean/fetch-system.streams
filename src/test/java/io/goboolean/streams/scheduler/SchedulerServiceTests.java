package io.goboolean.streams.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchedulerTests {

    @Autowired
    private SchedulerService scheduler;

    @Test
    public void testRun() {
        scheduler.run();
    }
}