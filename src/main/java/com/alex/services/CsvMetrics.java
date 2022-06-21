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
        Integer buyOrder = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuyOrder());
        Integer buySLOrder = TradeCondition.getInt(csvList.get(csvList.size() - 1).getBuySLOrder());
        double buySLPercent = TradeCondition.round(((double) buyOrder / buySLOrder - 1) * 100, 1);
        Integer buyTPOrder = TradeCondition.getInt(buyOrder * (buySLPercent * tpCoefficient) / 100 + buyOrder);
        Integer sellOrder = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellOrder());
        Integer sellSLOrder = TradeCondition.getInt(csvList.get(csvList.size() - 1).getSellSLOrder());
        double sellSLPercent = TradeCondition.round(((double) sellSLOrder / sellOrder - 1) * 100, 1);
        Integer sellTPOrder = TradeCondition.getInt(-sellOrder * (sellSLPercent * tpCoefficient) / 100 + sellOrder);
        LocalDateTime orderTime = DateTime.getMT4OrderLastTime(csvList.get(csvList.size() - 1).getDateTime());
        if (!buyOrders.containsKey(buyOrder)) {
            buyOrders.put(buyOrder, new HashMap<>());
            buyOrders.get(buyOrder).put("BuyOrderValues", new HashMap<>());
            buyOrders.get(buyOrder).get("BuyOrderValues").put("Order", buyOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrder", buySLOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLPercent", buySLPercent);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("TPOrder", buyTPOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("TPOrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("OrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("OrderTime", orderTime);
            if (buyOrders.size() > 1) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForNewOrder("NewBuyOrder:", buyOrder, buyTPOrder, buySLOrder, buySLPercent));
            }
        }
        if (!sellOrders.containsKey(sellOrder)) {
            sellOrders.put(sellOrder, new HashMap<>());
            sellOrders.get(sellOrder).put("SellOrderValues", new HashMap<>());
            sellOrders.get(sellOrder).get("SellOrderValues").put("Order", sellOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrder", sellSLOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLPercent", sellSLPercent);
            sellOrders.get(sellOrder).get("SellOrderValues").put("TPOrder", sellTPOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("TPOrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("OrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("OrderTime", orderTime);
            if (sellOrders.size() > 1) {
                telegramBot.pushMessage(dataHolder.getSubscriptions(), buildMessageForNewOrder("NewSellOrder:", sellOrder, sellTPOrder, sellSLOrder, sellSLPercent));
            }
        }
    }

    private String buildMessageForNewOrder(String orderType, Integer order, Integer orderTP, Integer orderSL, double slPercent) {
        StringBuilder builder = new StringBuilder();
        builder.append(orderType);
        builder.append(order);
        builder.append("\n");
        builder.append("TP:");
        builder.append(orderTP);
        builder.append("SL:");
        builder.append(orderSL);
        builder.append("\n");
        builder.append("SLPercent:");
        builder.append(slPercent);
        return builder.toString();
    }
}
