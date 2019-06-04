package com.alex.services;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class Scheduling {


    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private FileOperations fileOperations;

//    @Autowired
//    private TelegramBot telegramBot;


    @Scheduled(cron = "0 0/1 * ? * *")
    public void saveFiles() {
        fileOperations.saveFilesToList();

        dataHolder.getFileList().forEach((file) -> {
            log.info(file);

            try {
                Reader reader = Files.newBufferedReader(Paths.get(file));
                CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();
                CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(parser).build();


                reader.close();
                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
