package com.alex.services;

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

    @Value("5")
    private long ordersDepth;

    @Scheduled(cron = "0 20 7,15,23 * * *")
    public void checkAndClean() {
        log.info("Checking orders depth");
        if (csvMetrics.getBuyOrders().size() > ordersDepth) {
            csvMetrics.getBuyOrders().entrySet().forEach(order -> {
                Boolean orderIsActivated = (Boolean) order.getValue().get("BuyOrderValues").get("OrderActivated");
                Boolean stopLossOrderIsActivated = (Boolean) order.getValue().get("BuyOrderValues").get("SLOrderActivated");
                Boolean tpOrderIsActivated = (Boolean) order.getValue().get("BuyOrderValues").get("TPOrderActivated");
                if ((orderIsActivated.equals(true) && stopLossOrderIsActivated.equals(true)) || (orderIsActivated.equals(true) && tpOrderIsActivated.equals(true))) {
                    int key = (int) order.getValue().get("BuyOrderValues").get("Order");
                    log.info("Buy order {} is too old. Will be deleted", key);
                    csvMetrics.getBuyOrders().remove(key);
                }
            });
        } else if (csvMetrics.getSellOrders().size() > ordersDepth) {
            csvMetrics.getSellOrders().entrySet().forEach(order -> {
                Boolean orderIsActivated = (Boolean) order.getValue().get("SellOrderValues").get("OrderActivated");
                Boolean stopLossOrderIsActivated = (Boolean) order.getValue().get("SellOrderValues").get("SLOrderActivated");
                Boolean tpOrderIsActivated = (Boolean) order.getValue().get("SellOrderValues").get("TPOrderActivated");
                if ((orderIsActivated.equals(true) && stopLossOrderIsActivated.equals(true)) || (orderIsActivated.equals(true) && tpOrderIsActivated.equals(true))) {
                    int key = (int) order.getValue().get("SellOrderValues").get("Order");
                    log.info("Sell order {} is too old. Will be deleted", key);
                    csvMetrics.getSellOrders().remove(key);
                }
            });
        }
    }
}
