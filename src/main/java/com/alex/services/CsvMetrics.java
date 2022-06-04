package com.alex.services;

import com.alex.csv.CSVMapping;
import com.alex.model.CSVFields;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CsvMetrics {

    @Getter
    @Setter
    private Map<Integer, List<CSVFields>> csvMetrics = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    private List<CSVMapping> csvList = new ArrayList<>();

    @Getter
    @Setter
    private Map<Double, Map<String, Map<String, Object>>> buyOrders = new HashMap<>();

    @Getter
    @Setter
    private Map<Double, Map<String, Map<String, Object>>> sellOrders = new HashMap<>();

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
        Double buyOrder = csvList.get(csvList.size() - 1).getBuyOrder();
        Double buySLOrder = csvList.get(csvList.size() - 1).getBuySLOrder();
        Double sellOrder = csvList.get(csvList.size() - 1).getSellOrder();
        Double sellSLOrder = csvList.get(csvList.size() - 1).getSellSLOrder();
        if (!buyOrders.containsKey(buyOrder)) {
            buyOrders.put(buyOrder, new HashMap<>());
            buyOrders.get(buyOrder).put("BuyOrderValues", new HashMap<>());
            buyOrders.get(buyOrder).get("BuyOrderValues").put("Order", buyOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrder", buySLOrder);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("OrderActivated", false);
            buyOrders.get(buyOrder).get("BuyOrderValues").put("SLOrderActivated", false);
        }
        if (!sellOrders.containsKey(sellOrder)) {
            sellOrders.put(sellOrder, new HashMap<>());
            sellOrders.get(sellOrder).put("SellOrderValues", new HashMap<>());
            sellOrders.get(sellOrder).get("SellOrderValues").put("Order", sellOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrder", sellSLOrder);
            sellOrders.get(sellOrder).get("SellOrderValues").put("OrderActivated", false);
            sellOrders.get(sellOrder).get("SellOrderValues").put("SLOrderActivated", false);
        }
    }
}
