package com.alex.services;

import com.alex.services.CsvMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrdersCleaner {

    @Autowired
    private CsvMetrics csvMetrics;

    @Value("10")
    private long ordersDepth;

    @Scheduled(cron = "0 20 7,15,23 * * *")
    public void checkAndClean() {
        log.info("Checking orders depth");
        if (csvMetrics.getBuyOrders().size() > ordersDepth) {
            List<LocalDateTime> timeList = new ArrayList<>();
            csvMetrics.getBuyOrders().entrySet().forEach(order -> {
                Boolean orderIsActivated = (Boolean) order.getValue().get("BuyOrderValues").get("OrderActivated");
                if (orderIsActivated.equals(true)) {
                    timeList.add((LocalDateTime) order.getValue().get("BuyOrderValues").get("OrderTime"));
                }
            });
            LocalDateTime minTime = timeList.stream().min(LocalDateTime::compareTo).get();
            csvMetrics.getBuyOrders().entrySet().forEach(order -> {
                LocalDateTime orderTime = (LocalDateTime) order.getValue().get("BuyOrderValues").get("OrderTime");
                if (orderTime.isEqual(minTime)) {
                    Double key = (Double) order.getValue().get("BuyOrderValues").get("Order");
                    log.info("Buy order {} is too old. Will be deleted", key);
                    csvMetrics.getBuyOrders().remove(key);
                }
            });
        } else if (csvMetrics.getSellOrders().size() > ordersDepth) {
            List<LocalDateTime> timeList = new ArrayList<>();
            csvMetrics.getSellOrders().entrySet().forEach(order -> {
                Boolean orderIsActivated = (Boolean) order.getValue().get("SellOrderValues").get("OrderActivated");
                if (orderIsActivated.equals(true)) {
                    timeList.add((LocalDateTime) order.getValue().get("SellOrderValues").get("OrderTime"));
                }
            });
            LocalDateTime minTime = timeList.stream().min(LocalDateTime::compareTo).get();
            csvMetrics.getSellOrders().entrySet().forEach(order -> {
                LocalDateTime orderTime = (LocalDateTime) order.getValue().get("SellOrderValues").get("OrderTime");
                if (orderTime.isEqual(minTime)) {
                    Double key = (Double) order.getValue().get("SellOrderValues").get("Order");
                    log.info("Sell order {} is too old. Will be deleted", key);
                    csvMetrics.getSellOrders().remove(key);
                }
            });
        }
    }
}
