package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Scheduling {


    @Autowired
    private TimeMetrics timeMetrics;

    @Autowired
    private FileOperations fileOperations;

    @Autowired
    private CSVOperations csvOperations;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TradeCondition tradeCondition;

    @Scheduled(cron = "0 51 23 ? * *")
    public void deleteRows() {
        csvOperations.deleteRowsForFile();
        dataHolder.clearFileList();
    }


    @Scheduled(cron = "10 0/5 * ? * *")
    //@Scheduled(cron = "10 0/1 * ? * *")
    public void saveFiles() {
        fileOperations.saveFilesToList();

        csvOperations.saveValuesToMap();

        tradeCondition.checkTradeCondition();

        timeMetrics.getCsvMetrics().clear();
    }
}