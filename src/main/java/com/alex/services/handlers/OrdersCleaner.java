package com.alex.services.handlers;

import com.alex.services.CsvMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrdersCleaner {

    @Autowired
    private CsvMetrics csvMetrics;

    @Value("10")
    private long ordersDepth;

    @Scheduled(cron = "0 1 1 * * ?")
    public void checkAndClean(){
        csvMetrics.getBuyOrders().entrySet().stream()
                .filter(orders -> orders.getValue().size() > ordersDepth)
                .forEach(order ->{
                    Double firstOrder = csvMetrics.getBuyOrders().keySet().stream().findFirst().get();
                    log.info("BuyOrder - {} is too old. will be deleted", firstOrder);
                    csvMetrics.getBuyOrders().remove(firstOrder);
                });

        csvMetrics.getSellOrders().entrySet().stream()
                .filter(orders -> orders.getValue().size() > ordersDepth)
                .forEach(order ->{
                    Double firstOrder = csvMetrics.getSellOrders().keySet().stream().findFirst().get();
                    log.info("SellOrder - {} is too old. will be deleted", firstOrder);
                    csvMetrics.getSellOrders().remove(firstOrder);
                });
    }
}
