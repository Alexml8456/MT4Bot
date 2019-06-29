package com.alex.services;

import com.alex.utils.DateTime;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Data
public class KrakenProcessingService {
    @Getter
    private LocalDateTime lastTradeTime;

    @Autowired
    private CandleGenerationService candleGenerationService;

    @Autowired
    private VolumeGenerationService volumeGenerationService;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Getter
    private Map<LocalDateTime, JSONArray> tradesHistory = new ConcurrentHashMap<LocalDateTime, JSONArray>();

    public void process(String message, String ticker) {
        JSONArray trades = new JSONArray(message);
        for (int i = 0; i < trades.getJSONArray(1).length(); i++) {
            double price = trades.getJSONArray(1).getJSONArray(i).getDouble(0);
            BigDecimal volume = BigDecimal.valueOf(trades.getJSONArray(1).getJSONArray(i).getDouble(1));
            LocalDateTime time = DateTime.GMTTimeConverter(trades.getJSONArray(1).getJSONArray(i).getString(2));
            String tradeDirection = trades.getJSONArray(1).getJSONArray(i).getString(3);
            //log.info("price = {}; volume = {}; time = {}; tradeDirection = {}", price, volume, time, tradeDirection);
            lastTradeTime = DateTime.getGMTTimeMillis();
            //String finalTicker = ticker;
            //executor.submit(() -> internalProcess(finalTicker, side, price, size, total, time));
            //executor.submit(() -> candleGenerationService.generate(BigDecimal.valueOf(price), time));
            executor.submit(() -> volumeGenerationService.generate(tradeDirection, volume, time));
        }
    }
}