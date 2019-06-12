package com.alex.services;


import com.alex.csv.CSVMapping;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@Service
public class CSVOperations {

    @Value("${instrument}")
    private String instrument;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TimeMetrics timeMetrics;

    @SuppressWarnings("unchecked assignment")
    public void saveValuesToMap() {
        dataHolder.getFileList().forEach((file) -> {
            String fineName = new File(file).getName().replaceFirst("[.][^.]+$", "");
            Integer key = Integer.valueOf(fineName.split("_")[1]);
            if (fineName.contains(instrument)) {
                try (Reader reader = Files.newBufferedReader(Paths.get(file))) {
                    CsvToBean<CSVMapping> csvToBean = new CsvToBeanBuilder(reader)
                            .withType(CSVMapping.class)
                            .withIgnoreQuotations(true)
                            .withSkipLines(1)
                            .withIgnoreLeadingWhiteSpace(true).build();

                    for (CSVMapping csvMapping : csvToBean) {
                        timeMetrics.saveMetrics(key, csvMapping.getDateTime(), csvMapping.getSsValue(), csvMapping.getTfxValue(), csvMapping.getClosePrice());
                    }

                } catch (IOException e) {
                    log.error("Can't read data from file. " + e.getMessage(), e);
                }
                log.info("Values has been saved from file - {}", fineName);
            }
        });
    }

    public void deleteRowsForFile() {
        dataHolder.getFileList().forEach((file) -> {
            try {
                CSVReader readFile = new CSVReader(new FileReader(file));
                List<String[]> allRows = readFile.readAll();
                if (allRows.size() > 100) {
                    while (allRows.size() > 100) {
                        allRows.remove(1);
                    }
                    CSVWriter writer = new CSVWriter(new FileWriter(file), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
                    writer.writeAll(allRows);
                    writer.close();
                    log.info("File {} has been cleaned", file);
                }
                readFile.close();
            } catch (IOException e) {
                log.error("Can't delete data from file. " + e.getMessage(), e);
            }
        });
    }
}
