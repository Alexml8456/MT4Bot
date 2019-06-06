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
public class TimeMetrics {

    @Getter
    @Setter
    private Map<Integer, List<CSVFields>> csvMetrics = new ConcurrentSkipListMap<>();

    public void saveMetrics(Integer fileName, String dateTime, Double ssValue, Double tfxValue, Double closePrice) {
        csvMetrics.putIfAbsent(fileName, new ArrayList<>());
        csvMetrics.get(fileName).add(new CSVFields(dateTime, ssValue, tfxValue, closePrice));
    }

}
