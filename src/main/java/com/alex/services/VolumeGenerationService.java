package com.alex.services;

import com.alex.configuration.PeriodsProperties;
import com.alex.model.Volume;
import com.alex.utils.DateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class VolumeGenerationService {
    @Autowired
    private PeriodsProperties periods;
    @Value("${candles.depth}")
    private long candleDepth;

    @Autowired
    private CandleGenerationService candleGenerationService;

    @Getter
    @Setter
    private Map<String, Map<LocalDateTime, Volume>> volume = new ConcurrentSkipListMap<>();

    public void generate(String direction, BigDecimal volume, LocalDateTime timestamp) {
        if (timestamp.plusMinutes(candleDepth).isBefore(DateTime.getGMTTimeMillis())) {
            log.info("Message is too old. Will be skipped. {}", timestamp);
            return;
        }
        periods.getPeriods().forEach(period -> store(period, direction, candleGenerationService.generateKey(period, timestamp), volume));
    }

    private void store(String period, String direction, LocalDateTime key, BigDecimal price) {

        volume.putIfAbsent(period, new ConcurrentSkipListMap<>());
        Map<LocalDateTime, Volume> periodChart = volume.get(period);

        if (periodChart.containsKey(key)) {
            periodChart.get(key).update(direction, price);
        } else {
            periodChart.put(key, new Volume(direction, price));
        }
    }
}
