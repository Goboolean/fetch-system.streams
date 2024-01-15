package io.goboolean.streams.config;

import io.goboolean.streams.scheduler.SchedulerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {

    @Bean
    public SchedulerService schedulerService() {
        return new SchedulerService();
    }
}
