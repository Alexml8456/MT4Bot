package com.alex.csv;

import com.opencsv.bean.CsvBindByName;


public class CSVMapping {

    @CsvBindByName(column = "Time")
    private String dateTime;

    @CsvBindByName(column = "UPGlobalTrend")
    private Integer upGlobalTrend;

    @CsvBindByName(column = "UPLocalTrend")
    private Integer upLocalTrend;

    @CsvBindByName(column = "UPZigZagLocalTrend")
    private Integer upZigZagLocalTrend;

    @CsvBindByName(column = "UPZigZagMainTrend")
    private Integer upZigZagMainTrend;

    @CsvBindByName(column = "X3GlobalUPIndex")
    private Integer x3GlobalUPIndex;

    @CsvBindByName(column = "X3GlobalDownIndex")
    private Integer x3GlobalDownIndex;

    @CsvBindByName(column = "HRB4UP")
    private Integer hrb4UP;

    @CsvBindByName(column = "HRBUP")
    private Integer hrbUP;

    @CsvBindByName(column = "HRBUp-index")
    private Integer hrbUpIndex;

    @CsvBindByName(column = "HRBDown-index")
    private Integer hrbDownIndex;

    @CsvBindByName(column = "HalfTrendUP")
    private Integer halfTrendUP;

    @CsvBindByName(column = "HalfTrendUp-index")
    private Integer halfTrendUpIndex;

    @CsvBindByName(column = "HalfTrendDown-index")
    private Integer halfTrendDownIndex;

    @CsvBindByName(column = "BB4UpTrend")
    private Integer bb4UpTrend;

    @CsvBindByName(column = "BBUpTrend")
    private Integer bbUpTrend;

    @CsvBindByName(column = "BBUpTrend-index")
    private Integer bbUpTrendIndex;

    @CsvBindByName(column = "BBDownTrend-index")
    private Integer bbDownTrendIndex;

    @CsvBindByName(column = "BrainTrend2StopUP")
    private Integer brainTrend2StopUp;

    @CsvBindByName(column = "DrakeDSM15")
    private Double drakeDsm15;

    @CsvBindByName(column = "DrakeDSM30")
    private Double drakeDsm30;

    @CsvBindByName(column = "DrakeDSH1")
    private Double drakeDsh1;

    @CsvBindByName(column = "DrakeDSH4")
    private Double drakeDsh4;

    @CsvBindByName(column = "LastPrice")
    private Double lastPrice;

    @CsvBindByName(column = "Condition")
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

    public Double getDrakeDsm15() {
        return drakeDsm15;
    }

    public Double getDrakeDsm30() {
        return drakeDsm30;
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
