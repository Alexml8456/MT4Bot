package com.alex.csv;

import com.opencsv.bean.CsvBindByPosition;


public class CSVMapping {

    @CsvBindByPosition(position = 0)
    private String dateTime;

    @CsvBindByPosition(position = 1)
    private Double ssValue;

    @CsvBindByPosition(position = 2)
    private Double tfxValue;

    @CsvBindByPosition(position = 3)
    private Double closePrice;

    public String getDateTime() {
        return dateTime;
    }

    public Double getSsValue() {
        return ssValue;
    }

    public Double getTfxValue() {
        return tfxValue;
    }

    public Double getClosePrice() {
        return closePrice;
    }
}
