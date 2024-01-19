package io.goboolean.streams.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SchedulerServiceTests {

    Logger logger = LogManager.getLogger(SchedulerServiceTests.class);

    @Autowired
    private SchedulerService scheduler;

    @BeforeEach
    public void asdf() {
        logger.info("Starting application");
    }

    @Test
    public void testRun() {
        logger.info("Starting application");

        scheduler.run();
    }
}