package com.alex.services;


import com.alex.csv.CSVMapping;
import com.alex.telegram.TelegramBot;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
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
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class CSVOperations {

    @Autowired
    private TelegramBot telegramBot;

    @Value("${instrument}")
    private String instrument;

    @Value("${clear.rows}")
    private Integer numberOfRows;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private CsvMetrics csvMetrics;

    @SuppressWarnings("unchecked assignment")
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
    }

    @SuppressWarnings("unchecked assignment")
    public void saveValuesToList(String filePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            CsvToBean<CSVMapping> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVMapping.class)
                    .withIgnoreQuotations(true)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true).build();

            for (CSVMapping csvMapping : csvToBean) {
                csvMetrics.saveMt4MetricsToList(csvMapping.getSymbol(), csvMapping.getPeriod(), csvMapping.getDateTime(), csvMapping.getUpZigZagLocalTrend(), csvMapping.getUpZigZagMainTrend(), csvMapping.getSefc10Up(),
                        csvMapping.getHrbUp(), csvMapping.getHalfTrendUp(), csvMapping.getBbUpTrend(), csvMapping.getBbUpMainTrend(), csvMapping.getBbUpTrendIndex(),
                        csvMapping.getBbDownTrendIndex(), csvMapping.getBrainTrend2StopUp(), csvMapping.getBrainTrend2StopMainUp(),
                        csvMapping.getFL23(), csvMapping.getFL23Switch(), csvMapping.getReversalValue(), csvMapping.getGLineValue(), csvMapping.getBLineValue(),
                        csvMapping.getFL23H1(), csvMapping.getFL23SwitchH1(),
                        csvMapping.getFL23H4(), csvMapping.getFL23SwitchH4(), csvMapping.getReversalValueH4(), csvMapping.getGLineValueH4(), csvMapping.getBLineValueH4(),
                        csvMapping.getFL23D1(), csvMapping.getFL23SwitchD1(),
                        csvMapping.getLastPrice(), csvMapping.getLastLowPrice(), csvMapping.getLastHighPrice());
            }
            log.info(Arrays.toString(csvMetrics.getCsvList().get(0)));

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

    public void checkTradeCondition(Object[] values) {
        try {
            String newValues = Arrays.toString(values).replaceAll("\\[", "").replaceAll("]", "");
            if (newValues.split(",")[0].equals("ETHUSD")) {
                double FL23H1 = Double.parseDouble(newValues.split(",")[19]);
                double FL23SwitchH1 = Double.parseDouble(newValues.split(",")[20]);
                double FL23H4 = Double.parseDouble(newValues.split(",")[21]);
                double FL23SwitchH4 = Double.parseDouble(newValues.split(",")[22]);

                if (FL23SwitchH1 > 0) {
                    if (FL23H1 > 0) {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "H1 - Check possibility to open buy trade!");
                    } else {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "H1 - Check possibility to open sell trade!");
                    }
                } else if (FL23SwitchH4 > 0) {
                    if (FL23H4 > 0) {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "H4 - Check possibility to open buy trade!");
                    } else {
                        telegramBot.pushMessage(dataHolder.getSubscriptions(), "H4 - Check possibility to open sell trade!");
                    }
                }
                //log.info(FL23H1 + " " + FL23SwitchH1 + " " + FL23H4 + " " + FL23SwitchH4);
            }
        } catch (Exception e) {
            log.error("Can't parse data from file. " + e.getMessage(), e);
        }
    }
}
