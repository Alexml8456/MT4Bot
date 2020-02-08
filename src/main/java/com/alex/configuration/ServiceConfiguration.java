package com.alex.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
@EnableScheduling
public class ServiceConfiguration {
    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor accountsTaskExecutor = new ThreadPoolTaskExecutor();
        accountsTaskExecutor.setCorePoolSize(5);
        accountsTaskExecutor.setMaxPoolSize(5);
        accountsTaskExecutor.setQueueCapacity(1000);
        accountsTaskExecutor.initialize();
        return accountsTaskExecutor;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("scheduled-");
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }
}
