package com.alex.csv;

import com.opencsv.bean.CsvBindByPosition;


public class CSVMapping {

    @CsvBindByPosition(position = 0)
    private String symbol;

    @CsvBindByPosition(position = 1)
    private Integer period;

    @CsvBindByPosition(position = 2)
    private String dateTime;

    @CsvBindByPosition(position = 3)
    private Double DDS;

    @CsvBindByPosition(position = 4)
    private Double DDSH1;

    @CsvBindByPosition(position = 5)
    private Double DDSH4;

    @CsvBindByPosition(position = 6)
    private Integer buyDirection;

    @CsvBindByPosition(position = 7)
    private Double buyOrder;

    @CsvBindByPosition(position = 8)
    private Double sellOrder;

    @CsvBindByPosition(position = 9)
    private Double lastPrice;

    @CsvBindByPosition(position = 10)
    private Double lastLowPrice;

    @CsvBindByPosition(position = 11)
    private Double lastHighPrice;

    public String getSymbol() {
        return symbol;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public Double getLastLowPrice() {
        return lastLowPrice;
    }

    public Double getLastHighPrice() {
        return lastHighPrice;
    }

    public Double getDDS() {
        return DDS;
    }

    public Double getDDSH1() {
        return DDSH1;
    }

    public Double getDDSH4() {
        return DDSH4;
    }

    public Double getBuyOrder() {
        return buyOrder;
    }

    public Double getSellOrder() {
        return sellOrder;
    }

    public Integer getBuyDirection() {
        return buyDirection;
    }
}
