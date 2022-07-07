package com.alex.services;

import com.alex.csv.CSVMapping;
import com.alex.model.CSVFields;
import com.alex.telegram.TelegramBot;
import com.alex.utils.DateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CsvMetrics {

    @Value("${tp.coefficient}")
    private int tpCoefficient;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private DataHolder dataHolder;

    @Getter
    @Setter
    private Map<Integer, List<CSVFields>> csvMetrics = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    private List<CSVMapping> csvList = new ArrayList<>();

    @Getter
    @Setter
    private Map<Integer, Map<String, Map<String, Object>>> buyOrders = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    private Map<Integer, Map<String, Map<String, Object>>> sellOrders = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    private Map<String, Map<Integer, List<CSVFields>>> csvMt4Metrics = new ConcurrentSkipListMap<>();

    public void saveMetrics(Integer fileName, String dateTime, Double ssValue, Double tfxValue, Double closePrice) {
        csvMetrics.putIfAbsent(fileName, new ArrayList<>());
        //csvMetrics.get(fileName).add(new CSVFields(dateTime, ssValue, tfxValue, closePrice));
    }


    public void saveMt4Metrics(String symbol, Integer period, String dateTime, Integer upZigZagLocalTrend,
                               Integer upZigZagMainTrend, Integer sefc10Up, Integer halfTrendUp, Integer bbUpTrend, Integer bbMainUpTrend, Integer bbUpTrendIndex, Integer bbDownTrendIndex,
                               Integer brainTrend2StopUp, Integer brainTrend2StopMainUp, Double lastPrice, Double lastLowPrice, Double lastHighPrice) {
        csvMt4Metrics.putIfAbsent(symbol, new ConcurrentSkipListMap<>());
        csvMt4Metrics.get(symbol).putIfAbsent(period, new ArrayList<>());
        csvMt4Metrics.get(symbol).get(period).add(new CSVFields(symbol, period, dateTime, upZigZagLocalTrend, upZigZagMainTrend
                , sefc10Up, halfTrendUp, bbUpTrend, bbMainUpTrend, bbUpTrendIndex, bbDownTrendIndex,
                brainTrend2StopUp, brainTrend2StopMainUp, lastPrice, lastLowPrice,
                lastHighPrice));
    }

    public void saveBuySellOrders() {
        int buyOrderH1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuyOrderH1());
        int buySLOrderH1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuySLOrderH1());
        double buySLPercentH1 = TradeCondition.round(((double) buyOrderH1 / buySLOrderH1 - 1) * 100, 1);
        int buyTPOrderH1 = TradeCondition.getInt(buyOrderH1 * (buySLPercentH1 * tpCoefficient) / 100 + buyOrderH1);
        int sellOrderH1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellOrderH1());
        int sellSLOrderH1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellSLOrderH1());
        double sellSLPercentH1 = TradeCondition.round(((double) sellSLOrderH1 / sellOrderH1 - 1) * 100, 1);
        int sellTPOrderH1 = TradeCondition.getInt(-sellOrderH1 * (sellSLPercentH1 * tpCoefficient) / 100 + sellOrderH1);
        int buyOrderH4 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuyOrderH4());
        int buySLOrderH4 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuySLOrderH4());
        double buySLPercentH4 = TradeCondition.round(((double) buyOrderH4 / buySLOrderH4 - 1) * 100, 1);
        int buyTPOrderH4 = TradeCondition.getInt(buyOrderH4 * (buySLPercentH4 * tpCoefficient) / 100 + buyOrderH4);
        int sellOrderH4 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellOrderH4());
        int sellSLOrderH4 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellSLOrderH4());
        double sellSLPercentH4 = TradeCondition.round(((double) sellSLOrderH4 / sellOrderH4 - 1) * 100, 1);
        int sellTPOrderH4 = TradeCondition.getInt(-sellOrderH4 * (sellSLPercentH4 * tpCoefficient) / 100 + sellOrderH4);
        int buyOrderD1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuyOrderD1());
        int buySLOrderD1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuySLOrderD1());
        double buySLPercentD1 = TradeCondition.round(((double) buyOrderD1 / buySLOrderD1 - 1) * 100, 1);
        int buyTPOrderD1 = TradeCondition.getInt(buyOrderD1 * (buySLPercentD1 * tpCoefficient) / 100 + buyOrderD1);
        int sellOrderD1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellOrderD1());
        int sellSLOrderD1 = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellSLOrderD1());
        double sellSLPercentD1 = TradeCondition.round(((double) sellSLOrderD1 / sellOrderD1 - 1) * 100, 1);
        int sellTPOrderD1 = TradeCondition.getInt(-sellOrderD1 * (sellSLPercentD1 * tpCoefficient) / 100 + sellOrderD1);
        LocalDateTime orderTime = DateTime.getMT4OrderLastTime(csvList.get(csvList.size() - 1).getDateTime());
        checkIsOrderPresent(buyOrderH1, "H1", buySLOrderH1, buySLPercentH1, buyTPOrderH1, sellOrderH1, sellSLOrderH1, sellSLPercentH1, sellTPOrderH1, orderTime);
        checkIsOrderPresent(buyOrderH4, "H4", buySLOrderH4, buySLPercentH4, buyTPOrderH4, sellOrderH4, sellSLOrderH4, sellSLPercentH4, sellTPOrderH4, orderTime);
        checkIsOrderPresent(buyOrderD1, "D1", buySLOrderD1, buySLPercentD1, buyTPOrderD1, sellOrderD1, sellSLOrderD1, sellSLPercentD1, sellTPOrderD1, orderTime);
    }

    private String buildMessageForNewOrder(String orderType, String timeFrame, int order, int orderTP, int orderSL, double slPercent) {
        StringBuilder builder = new StringBuilder();
        builder.append(orderType);
        builder.append(order);
        builder.append("\n");
        builder.append("SL:");
        builder.append(orderSL);
        builder.append(";");
        builder.append("TP:");
        builder.append(orderTP);
        builder.append("\n");
        builder.append("SLPercent:");
        builder.append(slPercent);
        builder.append("\n");
        builder.append("TimeFrame:");
        builder.append(timeFrame);
        return builder.toString();
    }

    private void checkIsOrderPresent(int buyOrder, String timeFrame, int buySLOrder, double buySLPercent, int buyTPOrder, int sellOrder, int sellSLOrder, double sellSLPercent, int sellTPOrder, LocalDateTime orderTime) {
        if (!buyOrders.containsKey(buyOrder)) {
            buyOrders.put(buyOrder, new HashMap<>());
            buyOrders.get(buyOrder).put("BuyOrderValues", new HashMap<>());
            buyOrders.get(buyOrder).get("BuyOrderValues").put("Order", buyOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("TimeFrame", timeFrame);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrder", buySLOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLPercent", buySLPercent);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("TPOrder", buyTPOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("TPOrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("OrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("OrderTime", orderTime);
            if (buyOrders.size() > 3) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForNewOrder("NewBuyOrder:", timeFrame, buyOrder, buyTPOrder, buySLOrder, buySLPercent));
            }
        }
        if (!sellOrders.containsKey(sellOrder)) {
            sellOrders.put(sellOrder, new HashMap<>());
            sellOrders.get(sellOrder).put("SellOrderValues", new HashMap<>());
            sellOrders.get(sellOrder).get("SellOrderValues").put("Order", sellOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("TimeFrame", timeFrame);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrder", sellSLOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLPercent", sellSLPercent);
            sellOrders.get(sellOrder).get("SellOrderValues").put("TPOrder", sellTPOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("TPOrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("OrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("OrderTime", orderTime);
            if (sellOrders.size() > 3) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForNewOrder("NewSellOrder:", timeFrame, sellOrder, sellTPOrder, sellSLOrder, sellSLPercent));
            }
        }
    }
}
