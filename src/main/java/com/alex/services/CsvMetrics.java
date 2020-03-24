package com.alex.services;

import com.alex.model.CSVFields;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CsvMetrics {

    @Getter
    @Setter
    private Map<Integer, List<CSVFields>> csvMetrics = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    private List<Object[]> csvList = new ArrayList<>();

    @Getter
    @Setter
    private Map<String, Map<Integer, List<CSVFields>>> csvMt4Metrics = new ConcurrentSkipListMap<>();

    public void saveMetrics(Integer fileName, String dateTime, Double ssValue, Double tfxValue, Double closePrice) {
        csvMetrics.putIfAbsent(fileName, new ArrayList<>());
        //csvMetrics.get(fileName).add(new CSVFields(dateTime, ssValue, tfxValue, closePrice));
    }

    public void saveMt4MetricsToList(String symbol, Integer period, String dateTime, Integer upZigZagLocalTrend,
                                     Integer upZigZagMainTrend, Integer sefc10Up, Integer hrbUp, Integer halfTrendUp, Integer bbUpTrend, Integer bbMainUpTrend, Integer bbUpTrendIndex, Integer bbDownTrendIndex,
                                     Integer brainTrend2StopUp, Integer brainTrend2StopMainUp, Double fl23, Integer fl23Switch, Double reversalValue,
                                     Double gLineValue, Double bLineValue,
                                     Double lastPrice, Double lastLowPrice, Double lastHighPrice){
        Object[] tmp = {
                symbol,
                period,
                dateTime,
                upZigZagLocalTrend,
                upZigZagMainTrend,
                sefc10Up,
                hrbUp,
                halfTrendUp,
                bbUpTrend,
                bbMainUpTrend,
                bbUpTrendIndex,
                bbDownTrendIndex,
                brainTrend2StopUp,
                brainTrend2StopMainUp,
                fl23,
                fl23Switch,
                reversalValue,
                gLineValue,
                bLineValue,
                lastPrice,
                lastLowPrice,
                lastHighPrice
        };
        csvList.add(tmp);
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
}
