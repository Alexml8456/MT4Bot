package com.alex.services;

import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TradeCondition {

    @Autowired
    private TimeMetrics timeMetrics;

    private LocalDateTime lastConditionTime;

    public void checkTradeCondition() {
        LocalDateTime oneHourBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.HOURS).minusHours(1);

        if(lastConditionTime!=null){

        }

        if (conditionOneToBuy()){
            lastConditionTime = DateTime.getGMTTimeMillis();

            log.info("Time to Buy!");
        }
        else if (conditionOneToSell()){
            lastConditionTime = DateTime.getGMTTimeMillis();

            log.info("Time to Sell!");
        }
    }

    private boolean conditionOneToBuy() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue() > 0 && timeMetrics.getCsvMetrics().get(15).get(mapSize - 2).getSsValue() < 0;
    }

    private boolean conditionOneToSell() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue() < 0 && timeMetrics.getCsvMetrics().get(15).get(mapSize - 2).getSsValue() > 0;
    }
}
