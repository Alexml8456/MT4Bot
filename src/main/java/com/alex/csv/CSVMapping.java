package com.alex.csv;

import com.opencsv.bean.CsvBindByPosition;


public class CSVMapping {

    @CsvBindByPosition(position = 0)
    private String dateTime;

    @CsvBindByPosition(position = 1)
    private Integer upGlobalTrend;

    @CsvBindByPosition(position = 2)
    private Integer upLocalTrend;

    @CsvBindByPosition(position = 3)
    private Integer upZigZagLocalTrend;

    @CsvBindByPosition(position = 4)
    private Integer upZigZagMainTrend;

    @CsvBindByPosition(position = 5)
    private Integer x3GlobalUPIndex;

    @CsvBindByPosition(position = 6)
    private Integer x3GlobalDownIndex;

    @CsvBindByPosition(position = 7)
    private Integer hrb4UP;

    @CsvBindByPosition(position = 8)
    private Integer hrbUP;

    @CsvBindByPosition(position = 9)
    private Integer hrbUpIndex;

    @CsvBindByPosition(position = 10)
    private Integer hrbDownIndex;

    @CsvBindByPosition(position = 11)
    private Integer halfTrendUP;

    @CsvBindByPosition(position = 12)
    private Integer halfTrendUpIndex;

    @CsvBindByPosition(position = 13)
    private Integer halfTrendDownIndex;

    @CsvBindByPosition(position = 14)
    private Integer bb4UpTrend;

    @CsvBindByPosition(position = 15)
    private Integer bbUpTrend;

    @CsvBindByPosition(position = 16)
    private Integer bbUpTrendIndex;

    @CsvBindByPosition(position = 17)
    private Integer bbDownTrendIndex;

    @CsvBindByPosition(position = 18)
    private Integer brainTrend2StopUp;

    @CsvBindByPosition(position = 19)
    private Double drakeDsh1;

    @CsvBindByPosition(position = 20)
    private Double drakeDsh4;

    @CsvBindByPosition(position = 21)
    private Double lastPrice;

    @CsvBindByPosition(position = 22)
    private String condition;

    public String getDateTime() {
        return dateTime;
    }

    public Integer getUpGlobalTrend() {
        return upGlobalTrend;
    }

    public Integer getUpLocalTrend() {
        return upLocalTrend;
    }

    public Integer getUpZigZagLocalTrend() {
        return upZigZagLocalTrend;
    }

    public Integer getUpZigZagMainTrend() {
        return upZigZagMainTrend;
    }

    public Integer getX3GlobalUPIndex() {
        return x3GlobalUPIndex;
    }

    public Integer getX3GlobalDownIndex() {
        return x3GlobalDownIndex;
    }

    public Integer getHrb4UP() {
        return hrb4UP;
    }

    public Integer getHrbUP() {
        return hrbUP;
    }

    public Integer getHrbUpIndex() {
        return hrbUpIndex;
    }

    public Integer getHrbDownIndex() {
        return hrbDownIndex;
    }

    public Integer getHalfTrendUP() {
        return halfTrendUP;
    }

    public Integer getHalfTrendUpIndex() {
        return halfTrendUpIndex;
    }

    public Integer getHalfTrendDownIndex() {
        return halfTrendDownIndex;
    }

    public Integer getBb4UpTrend() {
        return bb4UpTrend;
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

    public Double getDrakeDsh1() {
        return drakeDsh1;
    }

    public Double getDrakeDsh4() {
        return drakeDsh4;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public String getCondition() {
        return condition;
    }
}
