package com.alex.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("candles.generate")
@Data
public class PeriodsProperties{
    private List<String> periods = new ArrayList<>();
}

