package com.alex.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@PropertySources({
        @PropertySource("classpath:generator.properties")
})
@EnableConfigurationProperties({PeriodsProperties.class})
@EnableScheduling
public class CandleGeneratorConfiguration {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor accountsTaskExecutor = new ThreadPoolTaskExecutor();
        accountsTaskExecutor.setCorePoolSize(1);
        accountsTaskExecutor.setMaxPoolSize(1);
        accountsTaskExecutor.setQueueCapacity(1000);
        accountsTaskExecutor.initialize();
        return accountsTaskExecutor;
    }
}