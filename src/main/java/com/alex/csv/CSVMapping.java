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
    private Integer upZigZagLocalTrend;

    @CsvBindByPosition(position = 4)
    private Integer upZigZagMainTrend;

    @CsvBindByPosition(position = 5)
    private Integer bbUpTrend;

    @CsvBindByPosition(position = 6)
    private Integer bbUpMainTrend;

    @CsvBindByPosition(position =7)
    private Integer bbUpTrendIndex;

    @CsvBindByPosition(position = 8)
    private Integer bbDownTrendIndex;

    @CsvBindByPosition(position = 9)
    private Integer brainTrend2StopUp;

    @CsvBindByPosition(position = 10)
    private Integer brainTrend2StopMainUp;

    @CsvBindByPosition(position = 11)
    private Double FL23;

    @CsvBindByPosition(position = 12)
    private Integer FL23Switch;

    @CsvBindByPosition(position = 13)
    private Double FL23H1;

    @CsvBindByPosition(position = 14)
    private Integer FL23SwitchH1;

    @CsvBindByPosition(position = 15)
    private Double FL23H4;

    @CsvBindByPosition(position = 16)
    private Integer FL23SwitchH4;

    @CsvBindByPosition(position = 17)
    private Double FL23D1;

    @CsvBindByPosition(position = 18)
    private Integer FL23SwitchD1;

    @CsvBindByPosition(position = 19)
    private Double DDS;

    @CsvBindByPosition(position = 20)
    private Double DDSH1;

    @CsvBindByPosition(position = 21)
    private Double DDSH4;

    @CsvBindByPosition(position = 22)
    private Double lastPrice;

    @CsvBindByPosition(position = 23)
    private Double lastLowPrice;

    @CsvBindByPosition(position = 24)
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

    public Integer getUpZigZagLocalTrend() {
        return upZigZagLocalTrend;
    }

    public Integer getUpZigZagMainTrend() {
        return upZigZagMainTrend;
    }

    public Integer getBbUpTrend() {
        return bbUpTrend;
    }

    public Integer getBbUpTrendIndex() {
        return bbUpTrendIndex;
    }

    public Integer getBbDownTrendIndex() {
        return bbDownTrendIndex;
    }

    public Integer getBrainTrend2StopUp() {
        return brainTrend2StopUp;
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

    public Integer getBrainTrend2StopMainUp() {
        return brainTrend2StopMainUp;
    }

    public Integer getBbUpMainTrend() {
        return bbUpMainTrend;
    }

    public Double getFL23() {
        return FL23;
    }

    public Integer getFL23Switch() {
        return FL23Switch;
    }

    public Integer getFL23SwitchH4() {
        return FL23SwitchH4;
    }

    public Double getFL23H4() {
        return FL23H4;
    }

    public Integer getFL23SwitchD1() {
        return FL23SwitchD1;
    }

    public Double getFL23D1() {
        return FL23D1;
    }

    public Double getFL23H1() {
        return FL23H1;
    }

    public Integer getFL23SwitchH1() {
        return FL23SwitchH1;
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
}
