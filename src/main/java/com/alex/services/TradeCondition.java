package com.alex.services;

import com.alex.csv.CSVMapping;
import com.alex.telegram.TelegramBot;
import com.alex.utils.DateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private CsvMetrics csvMetrics;

    private LocalDateTime lastConditionTime = DateTime.getGMTTimeMillis();

    private boolean bullMarket = false;

    private String oldDateTime = null;
    private String oldOrderDateTime = null;

    private boolean telegramPush = true;

    @Getter
    @Setter
    private Map<String, Boolean> symbolCondition = new HashMap<>();

    @Getter
    @Setter
    private Map<String, Map<String, Double>> orderStorage = new HashMap<>();

    public void checkTradeCondition() {
        LocalDateTime minutesBefore = DateTime.getGMTTimeMillis().truncatedTo(ChronoUnit.MINUTES).minusMinutes(14);

        log.info(getValues("Trading metrics!"));

        if (lastConditionTime.isBefore(minutesBefore)) {
            if (reEnterAfterSell() && bullMarket) {
                log.info("Re enter to buyCondition, after sell!");
                telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
                telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("First buyCondition after sell!"));
                bullMarket = false;
            } else if (firstFilteringBuyLevel()) {
                log.info("First buyCondition filtering level was passed!");
                if (secondFilteringBuyLevel()) {
                    //log.info("Second buyCondition filtering level was passed!");
                    lastConditionTime = DateTime.getGMTTimeMillis();
                    log.info(getValues("Second buyCondition filtering level was passed - time to Buy!"));
                    telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
                    telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-all conditions passed"));
//                    if (thirdFilteringBuyLevel()) {
//                        lastConditionTime = DateTime.getGMTTimeMillis();
//                        log.info(getValues("Third buyCondition filtering level was passed - time to Buy!"s));
//                        telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Buy-all conditions passed"));
//                    }
                }
            } else if (firstFilteringSellLevel()) {
                log.info("First sell filtering level was passed!");
                if (secondFilteringSellLevel()) {
                    //log.info("Second sell filtering level was passed!");
                    lastConditionTime = DateTime.getGMTTimeMillis();
                    log.info(getValues("Second sell filtering level was passed - time to Sell!"));
                    telegramBot.pushFile(dataHolder.getSubscriptions(), mt4Folder.concat("/ScreenShots/").concat("MT4.png"));
                    telegramBot.pushMessage(dataHolder.getSubscriptions(), getValues("Sell-all conditions passed"));
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

    public void checkSellBuyCondition(CSVMapping csvMapping) {
        String newDateTime = csvMapping.getDateTime();
        double drakeDsm15 = csvMapping.getDrakeDsm15();
        double drakeDsm30 = csvMapping.getDrakeDsm30();
        double drakeDsh1 = csvMapping.getDrakeDsh1();
        double drakeDsh4 = csvMapping.getDrakeDsh4();
        double lastPrice = csvMapping.getLastPrice();
        String condition = csvMapping.getCondition();
        if (StringUtils.isNotBlank(condition) && checkTime(oldDateTime, newDateTime)) {
            pushMessageForTrade(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice);
        }
        oldDateTime = newDateTime;
    }

    public void checkOrderCondition(CSVMapping csvMapping) {
        String newDateTime = csvMapping.getDateTime();
        if (checkTime(oldOrderDateTime, newDateTime)) {
            double highPrice = csvMapping.getHighPrice();
            double lowPrice = csvMapping.getLowPrice();
            double drakeDsm15 = csvMapping.getDrakeDsm15();
            double drakeDsm30 = csvMapping.getDrakeDsm30();
            double drakeDsh1 = csvMapping.getDrakeDsh1();
            double drakeDsh4 = csvMapping.getDrakeDsh4();
            int lastPrice = getInt(csvMapping.getLastPrice());
            List<Integer> buyList = new ArrayList<>(csvMetrics.getBuyOrders().keySet());
            for (int i = 0; i < buyList.size(); i++) {
                Integer buyOrder = (Integer) csvMetrics.getBuyOrders().get(buyList.get(i)).get("BuyOrderValues").get("Order");
                Boolean orderIsActivated = (Boolean) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("OrderActivated");
                Integer stopLossOrder = (Integer) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("SLOrder");
                Boolean stopLossOrderIsActivated = (Boolean) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("SLOrderActivated");
                Integer tpOrder = (Integer) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("TPOrder");
                Boolean tpOrderIsActivated = (Boolean) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("TPOrderActivated");
                LocalDateTime orderTime = (LocalDateTime) csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").get("OrderTime");
                if (buyOrder > lowPrice && stopLossOrder > lowPrice && stopLossOrderIsActivated.equals(false)) {
                    csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").put("OrderActivated", true);
                    csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").put("SLOrderActivated", true);
                    if (csvMetrics.getBuyOrders().size() > 1) {
                        log.info("Hit buy limit - {}, and stop loss order - {}", buyOrder, stopLossOrder);
                        String condition = "HitBuyStop";
                        pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, 0, stopLossOrder);
                    }
                } else if (tpOrder < highPrice && tpOrderIsActivated.equals(false) && orderIsActivated.equals(true) && stopLossOrderIsActivated.equals(false)) {
                    csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").put("TPOrderActivated", true);
                    log.info("Hit Buy take profit order - {}", tpOrder);
                    String condition = "HitBuyTP";
                    pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, tpOrder, 0);
                } else if (buyOrder > lowPrice && orderIsActivated.equals(false) && checkOrderTime(newDateTime, orderTime)) {
                    csvMetrics.getBuyOrders().get(buyOrder).get("BuyOrderValues").put("OrderActivated", true);
                    log.info("Hit Buy limit order - {}, stop loss order set to {}", buyOrder, stopLossOrder);
                    String condition = "HitBuyLimit";
                    pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, tpOrder, stopLossOrder);
                }
            }

            List<Integer> sellList = new ArrayList<>(csvMetrics.getSellOrders().keySet());
            for (int i = 0; i < sellList.size(); i++) {
                Integer sellOrder = (Integer) csvMetrics.getSellOrders().get(sellList.get(i)).get("SellOrderValues").get("Order");
                Boolean orderIsActivated = (Boolean) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("OrderActivated");
                Integer stopLossOrder = (Integer) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("SLOrder");
                Boolean stopLossOrderIsActivated = (Boolean) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("SLOrderActivated");
                Integer tpOrder = (Integer) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("TPOrder");
                Boolean tpOrderIsActivated = (Boolean) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("TPOrderActivated");
                LocalDateTime orderTime = (LocalDateTime) csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").get("OrderTime");
                if (sellOrder < highPrice && stopLossOrder < highPrice && stopLossOrderIsActivated.equals(false)) {
                    csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").put("OrderActivated", true);
                    csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").put("SLOrderActivated", true);
                    if (csvMetrics.getSellOrders().size() > 1) {
                        log.info("Hit sell limit - {}, and stop loss order - {}", sellOrder, stopLossOrder);
                        String condition = "HitSellStop";
                        pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, 0, stopLossOrder);
                    }
                } else if (tpOrder > lowPrice && tpOrderIsActivated.equals(false) && orderIsActivated.equals(true) && stopLossOrderIsActivated.equals(false)) {
                    csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").put("TPOrderActivated", true);
                    log.info("Hit Sell take profit order - {}", tpOrder);
                    String condition = "HitSellTP";
                    pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, tpOrder, 0);
                } else if (sellOrder < highPrice && orderIsActivated.equals(false) && checkOrderTime(newDateTime, orderTime)) {
                    csvMetrics.getSellOrders().get(sellOrder).get("SellOrderValues").put("OrderActivated", true);
                    log.info("Hit Sell limit order - {}, stop loss order set to {}", sellOrder, stopLossOrder);
                    String condition = "HitSellLimit";
                    pushMessageForOrder(condition, drakeDsm15, drakeDsm30, drakeDsh1, drakeDsh4, lastPrice, tpOrder, stopLossOrder);
                }
            }
        }
        oldOrderDateTime = newDateTime;
    }


    private boolean firstFilteringSellLevel() {
        int key = 5;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.18 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.46;
    }

    private boolean firstFilteringBuyLevel() {
        int key = 5;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.18 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.46;
    }

    private boolean secondFilteringSellLevel() {
        int key = 15;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.17 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.45;
    }

    private boolean secondFilteringBuyLevel() {
        int key = 15;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.17 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.45;
    }

    private boolean thirdFilteringSellLevel() {
        int key = 30;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() > 0.15 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() > 0.6;
    }

    private boolean thirdFilteringBuyLevel() {
        int key = 30;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.15 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.6;
    }

    private boolean reEnterAfterSell() {
        int key = 5;
        int mapSize = csvMetrics.getCsvMetrics().get(key).size();
        return true;
        //return timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getSsValue() < -0.2 && timeMetrics.getCsvMetrics().get(key).get(mapSize - 1).getTfxValue() < -0.3;
    }

    private String getValues(String direction) {
        StringBuilder builder = new StringBuilder();
        builder.append("--");
        builder.append(instrument);
        builder.append(" ");
        builder.append(direction);
        builder.append("--\n");
        csvMetrics.getCsvMetrics().forEach((key, values) -> {
            builder.append("P=");
            builder.append(valueMapping(key.toString()));
            builder.append("; SS=");
            //builder.append(round(values.get(values.size() - 1).getSsValue(), 2));
            builder.append("; TFX=");
            //builder.append(round(values.get(values.size() - 1).getTfxValue(), 2));
            builder.append("; CP=");
            //builder.append(round(values.get(values.size() - 1).getClosePrice(), 1));
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getInt(double value) {
        BigDecimal bd = new BigDecimal(value);
        return bd.intValue();
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

    private void checkTrade(String symbol, double h1, double h4) {
        if (sellSignal(h1, h4)) {
            saveTradeCondition(symbol, false);
        } else if (buySignal(h1, h4)) {
            saveTradeCondition(symbol, true);
        }
    }

    private void saveTradeCondition(String symbol, Boolean condition) {
        if (symbolCondition.containsKey(symbol)) {
            symbolCondition.put(symbol, condition);
        }
        symbolCondition.putIfAbsent(symbol, condition);
    }

    private void saveOrders(String symbol, Double buyOrder, Double sellOrder) {
        if (orderStorage.containsKey(symbol)) {
            orderStorage.get(symbol).put("buyOrder", buyOrder);
            orderStorage.get(symbol).put("sellOrder", sellOrder);
        } else {
            orderStorage.putIfAbsent(symbol, new HashMap<>());
            orderStorage.get(symbol).put("buyOrder", buyOrder);
            orderStorage.get(symbol).put("sellOrder", sellOrder);
        }
    }

    private void pushOrderMessage(String symbol, int direction, Double buyOrder, Double sellOrder) {
        if (orderStorage.containsKey(symbol)) {
            if (orderStorage.get(symbol).get("buyOrder").compareTo(buyOrder) != 0) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), "Buy order changed for symbol- " + symbol + " direction- " + orderDirection(direction) + " Buy order- " + buyOrder);
            } else if (orderStorage.get(symbol).get("sellOrder").compareTo(sellOrder) != 0) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), "Sell order changed for symbol- " + symbol + " direction- " + orderDirection(direction) + " Sell order- " + sellOrder);
            }
        }
    }

    private String orderDirection(Integer direction) {
        if (direction > 0) {
            return "Buy";
        } else {
            return "Sell";
        }
    }

    private boolean sellSignal(double h1, double h4) {
        return h1 > 80;
    }

    private boolean buySignal(double h1, double h4) {
        return h1 < 20;
    }

    private void pushDDSMessage(String symbol, double h1, double h4, double lastPrice) {
        if (symbolCondition.containsKey(symbol)) {
            if (buySignal(h1, h4) || sellSignal(h1, h4)) {
                if (symbolCondition.get(symbol).compareTo(buySignal(h1, h4)) != 0) {
                    if (buySignal(h1, h4)) {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "Buy stochastic pattern appeared for " + symbol + " at " + lastPrice);
                    } else {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "Sell stochastic pattern appeared for " + symbol + " at " + lastPrice);
                    }
                }
            }
        } else if (!symbolCondition.containsKey(symbol) && (buySignal(h1, h4) || sellSignal(h1, h4))) {
            if (buySignal(h1, h4)) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), "Buy stochastic pattern appeared for " + symbol + " at " + lastPrice);
            } else {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), "Sell stochastic pattern appeared for " + symbol + " at " + lastPrice);
            }
        }
    }

    private void pushMessageForTrade(String condition, double m15, double m30, double h1, double h4, double lastPrice) {
        telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForTrade(condition, m15, m30, h1, h4, lastPrice));
    }

    private void pushMessageForOrder(String condition, double m15, double m30, double h1, double h4, int lastPrice, int tpOrder, int slOrder) {
        Integer buyKey = getNearest(csvMetrics.getBuyOrders(), "BuyOrderValues", "Order");
        Integer sellKey = getNearest(csvMetrics.getSellOrders(), "SellOrderValues", "Order");
        Integer buySL = getNearest(csvMetrics.getBuyOrders(), "BuyOrderValues", "SLOrder");
        Integer sellSL = getNearest(csvMetrics.getSellOrders(), "SellOrderValues", "SLOrder");
        Integer buyTP = getNearest(csvMetrics.getBuyOrders(), "BuyOrderValues", "TPOrder");
        Integer sellTP = getNearest(csvMetrics.getSellOrders(), "SellOrderValues", "TPOrder");
        telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForOrder(condition, m15, m30, h1, h4, lastPrice, tpOrder, slOrder, buyKey, sellKey, buySL, sellSL, buyTP, sellTP));
    }

    private String buildMessageForTrade(String condition, double m15, double m30, double h1, double h4, double lastPrice) {
        StringBuilder builder = new StringBuilder();
        builder.append("m15=");
        builder.append(m15);
        builder.append(";");
        builder.append("m30=");
        builder.append(m30);
        builder.append("\n");
        builder.append("h1=");
        builder.append(h1);
        builder.append(";");
        builder.append("h4=");
        builder.append(h4);
        builder.append(";");
        builder.append("price=");
        builder.append(lastPrice);
        builder.append(";");
        builder.append(" ");
        builder.append("Condition=");
        builder.append(condition.replaceAll("\\s", ""));
        return builder.toString();
    }

    private String buildMessageForOrder(String condition, double m15, double m30, double h1, double h4, int lastPrice, int tpOrder, int slOrder, int buyOrder, int sellOrder, int buySL, int sellSL, int buyTP, int sellTP) {
        StringBuilder builder = new StringBuilder();
        builder.append("m15=");
        builder.append(m15);
        builder.append(";");
        builder.append("m30=");
        builder.append(m30);
        builder.append("\n");
        builder.append("h1=");
        builder.append(h1);
        builder.append(";");
        builder.append("h4=");
        builder.append(h4);
        builder.append("\n");
        builder.append("price=");
        builder.append(lastPrice);
        builder.append("\n");
        builder.append("Condition=");
        builder.append(condition.replaceAll("\\s", ""));
        builder.append("\n");
        builder.append("TP=");
        builder.append(tpOrder);
        builder.append(";");
        builder.append("SL=");
        builder.append(slOrder);
        builder.append("\n");
        builder.append("NB=");
        builder.append(buyOrder);
        builder.append(";");
        builder.append("NBSL=");
        builder.append(buySL);
        builder.append("\n");
        builder.append("NBTP=");
        builder.append(buyTP);
        builder.append("\n");
        builder.append("NS=");
        builder.append(sellOrder);
        builder.append(";");
        builder.append("NSSL=");
        builder.append(sellSL);
        builder.append("\n");
        builder.append("NSTP=");
        builder.append(sellTP);
        return builder.toString();
    }

    private Boolean checkTime(String oldTime, String newTime) {
        return (oldTime == null) || (!oldTime.equals(newTime));
    }

    private Boolean checkOrderTime(String currentTime, LocalDateTime orderTime) {
        LocalDateTime currentSchedulerTime = DateTime.getMT4OrderLastTime(currentTime);
        return !currentSchedulerTime.isEqual(orderTime);
    }

    private Integer getNearest(Map<Integer, Map<String, Map<String, Object>>> orders, String orderType, String orderKey) {
        List<LocalDateTime> timeList = new ArrayList<>();
        orders.entrySet().forEach(order -> {
            Boolean orderIsActivated = (Boolean) order.getValue().get(orderType).get("OrderActivated");
            Boolean stopLossOrderIsActivated = (Boolean) order.getValue().get(orderType).get("SLOrderActivated");
            Boolean tpOrderIsActivated = (Boolean) order.getValue().get(orderType).get("TPOrderActivated");
            if (orderIsActivated.equals(false) && stopLossOrderIsActivated.equals(false) && tpOrderIsActivated.equals(false)) {
                timeList.add((LocalDateTime) order.getValue().get(orderType).get("OrderTime"));
            }
        });
        if (!timeList.isEmpty()) {
            LocalDateTime maxTime = timeList.stream().max(LocalDateTime::compareTo).get();
            for (Map.Entry<Integer, Map<String, Map<String, Object>>> order : orders.entrySet()) {
                LocalDateTime orderTime = (LocalDateTime) order.getValue().get(orderType).get("OrderTime");
                if (orderTime.isEqual(maxTime)) {
                    return (Integer) order.getValue().get(orderType).get(orderKey);
                }
            }
        }
        return 0;
    }
}