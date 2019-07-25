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
import java.util.Comparator;

@Slf4j
@Service
public class TradeCondition {

    @Value("${mt4.files.folder}")
    private String mt4Folder;

    @Value("${instrument}")
    private String instrument;

    @Autowired
    private VolumeGenerationService volumeGenerationService;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TimeMetrics timeMetrics;

    private LocalDateTime lastConditionTime = DateTime.getGMTTimeMillis();

    private boolean bullMarket = false;

    public void checkTradeCondition() {
        LocalDateTime minutesBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(14);

        log.info(getValues("Trading metrics!"));

        if (lastConditionTime.isBefore(minutesBefore)) {
            if (reEnterAfterSell() && bullMarket) {
                log.info("Re enter to buy, after sell!");
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("First buy after sell!"));
                telegramBot.pushPhotoMessage(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
                bullMarket = false;
            } else if (firstFilteringBuyLevel()) {
                log.info("First buy filtering level was passed!");
                if (secondFilteringBuyLevel()) {
                    //log.info("Second buy filtering level was passed!");
                    lastConditionTime = DateTime.getGMTTimeMillis();
                    log.info(getValues("Second buy filtering level was passed - time to Buy!"));
                    telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-all conditions passed"));
                    telegramBot.pushPhotoMessage(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
//                    if (thirdFilteringBuyLevel()) {
//                        lastConditionTime = DateTime.getGMTTimeMillis();
//                        log.info(getValues("Third buy filtering level was passed - time to Buy!"s));
//                        telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-all conditions passed"));
//                    }
                }
            } else if (firstFilteringSellLevel()) {
                log.info("First sell filtering level was passed!");
                if (secondFilteringSellLevel()) {
                    //log.info("Second sell filtering level was passed!");
                    lastConditionTime = DateTime.getGMTTimeMillis();
                    log.info(getValues("Second sell filtering level was passed - time to Sell!"));
                    telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-all conditions passed"));
                    telegramBot.pushPhotoMessage(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
                    bullMarket = true;
//                    if (thirdFilteringSellLevel()) {
//                        lastConditionTime = DateTime.getGMTTimeMillis();
//                        log.info(getValues("Third sell filtering level was passed - time to Sell!"));
//                        telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-all conditions passed"));
//                        bullMarket = true;
//                    }
                }
            }
        }
    }

    private boolean firstFilteringSellLevel() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.18 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.46;
    }

    private boolean firstFilteringBuyLevel() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.18 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.46;
    }

    private boolean secondFilteringSellLevel() {
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.17 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.45;
    }

    private boolean secondFilteringBuyLevel() {
        int key = 15;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.17 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.45;
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

    private boolean reEnterAfterSell() {
        int key = 5;
        int mapSize = timeMetrics.getCsvMetrics().get(key).size();
        return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.3;
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
            builder.append(valueMapping(key.toString()));
            builder.append("; SS=");
            builder.append(round(values.get(values.size() - 1).getSsValue(), 2));
            builder.append("; TFX=");
            builder.append(round(values.get(values.size() - 1).getTfxValue(), 2));
            builder.append("; CP=");
            builder.append(round(values.get(values.size() - 1).getClosePrice(), 1));
            builder.append("\n");
        });
        builder.append(getVolume());
        return builder.toString();
    }

    private String getVolume() {
        StringBuilder builder = new StringBuilder();
        builder.append("--");
        builder.append(instrument);
        builder.append(" ");
        builder.append("Volume metrics!");
        builder.append("--\n");
        volumeGenerationService.getVolume().entrySet().stream()
                .sorted(Comparator.comparingInt(o -> Integer.valueOf(o.getKey())))
                .forEach(period -> {
                    LocalDateTime maxKey = period.getValue().keySet().stream().max(LocalDateTime::compareTo).get();
                    double buys = round(volumeGenerationService.getVolume().get(period.getKey()).get(getRelevantKey(period.getKey(), maxKey)).getBuy().doubleValue(), 1);
                    double sells = round(volumeGenerationService.getVolume().get(period.getKey()).get(getRelevantKey(period.getKey(), maxKey)).getSell().doubleValue(), 1);
                    builder.append("P=");
                    builder.append(valueMapping(period.getKey()));
                    builder.append("; TBuy=");
                    builder.append(buys);
                    builder.append("; TSell=");
                    builder.append(sells);
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

    private String valueMapping(String key) {
        switch (key) {
            case "240":
                return "4H";
            case "1440":
                return "1D";
            default:
                return key;
        }
    }

    private LocalDateTime getRelevantKey(String period, LocalDateTime key) {
        LocalDateTime currentTime = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES);
        if (currentTime.compareTo(key) > 0) {
            return key;
        } else
            return key.minusMinutes(Integer.valueOf(period));
    }
}