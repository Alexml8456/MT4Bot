package com.alex.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:currency.properties")
@ConfigurationProperties("currency")
@Data
public class CurrencyProperties {
    private List<String> pairs = new ArrayList<>();
}