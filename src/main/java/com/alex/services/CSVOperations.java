package com.alex.services;


import com.alex.csv.CSVMapping;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


@Slf4j
@Service
public class CSVOperations {

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TimeMetrics timeMetrics;

    @SuppressWarnings("unchecked assignment")
    public void saveValuesToMap() {
        dataHolder.getFileList().forEach((file) -> {
            String fineName = new File(file).getName().replaceFirst("[.][^.]+$", "");

            try (Reader reader = Files.newBufferedReader(Paths.get(file))) {
                CsvToBean<CSVMapping> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(CSVMapping.class)
                        .withIgnoreQuotations(true)
                        .withSkipLines(1)
                        .withIgnoreLeadingWhiteSpace(true).build();

                for (CSVMapping csvMapping : csvToBean) {
                    timeMetrics.saveMetrics(fineName, csvMapping.getDateTime(), csvMapping.getSsValue(), csvMapping.getTfxValue(), csvMapping.getClosePrice());
                }

            } catch (IOException e) {
                log.error("Can't read data from file. " + e.getMessage(), e);
            }
        });


    }

}
