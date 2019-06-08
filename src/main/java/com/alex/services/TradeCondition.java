package com.alex.services;

import com.alex.telegram.TelegramBot;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class TradeCondition {

    @Value("${instrument}")
    private String instrument;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TimeMetrics timeMetrics;

    private LocalDateTime lastConditionTime = DateTime.getGMTTimeMillis();

    public void checkTradeCondition() {
        LocalDateTime oneHourBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1);

        if (lastConditionTime.isBefore(oneHourBefore)) {
            if (conditionSecondToBuy()) {
                lastConditionTime = DateTime.getGMTTimeMillis();
                log.info(getValues("Buy-based on TFX values"));
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-based on TFX values"));
            } else if (conditionSecondToSell()) {
                lastConditionTime = DateTime.getGMTTimeMillis();
                log.info(getValues("Sell-based on TFX values"));
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-based on TFX values"));
            } else if (conditionOneToBuy()) {
                lastConditionTime = DateTime.getGMTTimeMillis();
                log.info(getValues("Buy-based on SS values"));
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-based on SS values"));
            } else if (conditionOneToSell()) {
                lastConditionTime = DateTime.getGMTTimeMillis();
                log.info(getValues("Sell-based on SS values"));
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-based on SS values"));
            }
        }
    }

    private boolean conditionOneToBuy() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue() > 0 && timeMetrics.getCsvMetrics().get(15).get(mapSize - 2).getSsValue() < 0;
    }

    private boolean conditionSecondToBuy() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getTfxValue() < -0.9;
    }

    private boolean conditionOneToSell() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue() < 0 && timeMetrics.getCsvMetrics().get(15).get(mapSize - 2).getSsValue() > 0;
    }

    private boolean conditionSecondToSell() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getTfxValue() > 0.9;
    }

    private boolean conditionThirdToBuy() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue().toString().contains("-0.0");
    }

    private boolean conditionThirdToSell() {
        int mapSize = timeMetrics.getCsvMetrics().get(15).size();
        return timeMetrics.getCsvMetrics().get(15).get(mapSize - 1).getSsValue().toString().contains("0.0");
    }

    private String getValues(String direction) {
        StringBuilder builder = new StringBuilder();
        builder.append("--");
        builder.append(instrument);
        builder.append(" ");
        builder.append(direction);
        builder.append("--\n");
        timeMetrics.getCsvMetrics().forEach((key, values) -> {
            builder.append("P=");
            builder.append(key);
            builder.append("; SS=");
            builder.append(values.get(values.size() - 1).getSsValue());
            builder.append("; TFX=");
            builder.append(values.get(values.size() - 1).getTfxValue());
            builder.append("; CP=");
            builder.append(values.get(values.size() - 1).getClosePrice());
            builder.append("\n");
        });
        builder.append("---------------------------------");
        return builder.toString();
    }
}