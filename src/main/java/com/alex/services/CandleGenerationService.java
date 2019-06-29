package com.alex.services;

import com.alex.configuration.PeriodsProperties;
import com.alex.model.Candle;
import com.alex.utils.DateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CandleGenerationService {
    @Autowired
    private PeriodsProperties periods;
    @Value("${candles.depth}")
    private long candleDepth;


    @Getter
    @Setter
    private Map<String, Map<LocalDateTime, Candle>> charts = new ConcurrentSkipListMap<>();

    public void generate(BigDecimal price, LocalDateTime timestamp) {
        if (timestamp.plusMinutes(candleDepth).isBefore(DateTime.getGMTTimeMillis())) {
            log.info("Message is too old. Will be skipped. {}", timestamp);
            return;
        }
        periods.getPeriods().forEach(period -> store(period, generateKey(period, timestamp), price));
    }

    public LocalDateTime generateKey(String period, LocalDateTime timestamp) {
        LocalDateTime key = timestamp.truncatedTo(ChronoUnit.HOURS);

        Long periodInMins = Long.valueOf(period);
        if (periodInMins > 60) {
            key = key.truncatedTo(ChronoUnit.DAYS);
        }

        while (timestamp.isAfter(key) || timestamp.isEqual(key)) {
            key = key.plusMinutes(periodInMins);
        }
        return key.minusMinutes(periodInMins);
    }

    private void store(String period, LocalDateTime key, BigDecimal price) {

        charts.putIfAbsent(period, new ConcurrentSkipListMap<>());
        Map<LocalDateTime, Candle> periodChart = charts.get(period);

        if (periodChart.containsKey(key)) {
            periodChart.get(key).update(price);
        } else {
            periodChart.put(key, new Candle(price));
        }
    }
}