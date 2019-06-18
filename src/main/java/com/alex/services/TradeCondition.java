package com.alex.services;

import com.alex.telegram.TelegramBot;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        LocalDateTime minutesBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(1);

        if (lastConditionTime.isBefore(minutesBefore)) {
            if (firstFilteringBuyLevel()) {
                log.info("First buy filtering level was passed!");
                if (secondFilteringBuyLevel()) {
                    log.info("Second buy filtering level was passed!");
                    if (thirdFilteringBuyLevel()) {
                        lastConditionTime = DateTime.getGMTTimeMillis();
                        log.info(getValues("Third buy filtering level was passed - time to Buy!"));
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-all conditions passed"));
                    }
                }
            } else if (firstFilteringSellLevel()) {
                log.info("First sell filtering level was passed!");
                if (secondFilteringSellLevel()) {
                    log.info("Second sell filtering level was passed!");
                    if (thirdFilteringSellLevel()) {
                        lastConditionTime = DateTime.getGMTTimeMillis();
                        log.info(getValues("Third sell filtering level was passed - time to Sell!"));
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-all conditions passed"));
                    }
                }
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
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 2).getSsValue() < -0.1 && conditionNearZero(mapSize, key);
    }

    private boolean conditionThirdToSell() {
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 2).getSsValue() > 0.1 && conditionNearZero(mapSize, key);
    }

    private boolean firstFilterBuyStage() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return conditionalBuyCompareSSTFX(mapSize, key) && conditionNearZero(mapSize, key);
    }

    private boolean firstFilterSellStage() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return conditionalSellCompareSSTFX(mapSize, key) && conditionNearZero(mapSize, key);
    }


    private boolean conditionNearZero(Integer mapSize, Integer key) {
        double lastValue = timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue();
        return lastValue <= 0.0999 && lastValue >= -0.0999;
    }

    private boolean conditionalSellCompareSSTFX(Integer mapSize, Integer key) {
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 2).getSsValue() > 0.1 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.1;
    }

    private boolean conditionalBuyCompareSSTFX(Integer mapSize, Integer key) {
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 2).getSsValue() < -0.1 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.1;
    }

    private boolean firstFilteringSellLevel() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.8;
    }

    private boolean firstFilteringBuyLevel() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.8;
    }

    private boolean secondFilteringSellLevel() {
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.6;
    }

    private boolean secondFilteringBuyLevel() {
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.6;
    }

    private boolean thirdFilteringSellLevel() {
        int key = 30;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.15 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.6;
    }

    private boolean thirdFilteringBuyLevel() {
        int key = 30;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.15 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.6;
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
            builder.append(round(values.get(values.size() - 1).getSsValue(), 2));
            builder.append("; TFX=");
            builder.append(round(values.get(values.size() - 1).getTfxValue(), 2));
            builder.append("; CP=");
            builder.append(round(values.get(values.size() - 1).getClosePrice(), 1));
            builder.append("\n");
        });
        builder.append("---------------------------------");
        return builder.toString();
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
