package com.alex.services;


import com.alex.csv.CSVMapping;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@Service
public class CSVOperations {

    @Value("${instrument}")
    private String instrument;

    @Value("${clear.rows}")
    private Integer numberOfRows;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private CsvMetrics csvMetrics;

    /*@SuppressWarnings("unchecked assignment")
    public void saveValuesToMap(String filePath) {
        //String fineName = new File(file).getName().replaceFirst("[.][^.]+$", "");
        //Integer key = Integer.valueOf(fineName.split("_")[1]);
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<CSVMapping> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVMapping.class)
                    .withIgnoreQuotations(true)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true).build();

            for (CSVMapping csvMapping : csvToBean) {
                csvMetrics.saveMt4Metrics(csvMapping.getSymbol(), csvMapping.getPeriod(), csvMapping.getDateTime(), csvMapping.getUpZigZagLocalTrend(), csvMapping.getUpZigZagMainTrend(), csvMapping.getSefc10Up(),
                        csvMapping.getHalfTrendUp(), csvMapping.getBbUpTrend(), csvMapping.getBbUpMainTrend(), csvMapping.getBbUpTrendIndex(),
                        csvMapping.getBbDownTrendIndex(), csvMapping.getBrainTrend2StopUp(), csvMapping.getBrainTrend2StopMainUp(),
                        csvMapping.getLastPrice(), csvMapping.getLastLowPrice(), csvMapping.getLastHighPrice());
            }

        } catch (IOException e) {
            log.error("Can't read data from file. " + e.getMessage(), e);
        }
        //log.info("Values has been saved from file - {}", fineName);
    }*/

    @SuppressWarnings("unchecked assignment")
    void saveValuesToList(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            List<CSVMapping> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVMapping.class)
                    .withIgnoreQuotations(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            csvMetrics.setCsvList(csvToBean);
            log.info(csvMetrics.getCsvList().get(csvMetrics.getCsvList().size()-1).getAll());
        } catch (Exception e) {
            log.error("Can't read data from file. " + e.getMessage(), e);
        }
        //log.info("Values has been saved from file - {}", fineName);
    }

    public void deleteRowsForFile() {
        dataHolder.getFileList().stream().filter(f -> f.endsWith(".csv")).forEach((file) -> {
            try {
                CSVReader readFile = new CSVReader(new FileReader(file));
                List<String[]> allRows = readFile.readAll();
                if (allRows.size() > numberOfRows) {
                    while (allRows.size() > numberOfRows) {
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
