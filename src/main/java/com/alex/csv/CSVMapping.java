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

    @CsvBindByName(column = "BuyOrderH1")
    private Double buyOrderH1;

    @CsvBindByName(column = "BuySLOrderH1")
    private Double buySLOrderH1;

    @CsvBindByName(column = "SellOrderH1")
    private Double sellOrderH1;

    @CsvBindByName(column = "SellSLOrderH1")
    private Double sellSLOrderH1;

    @CsvBindByName(column = "BuyOrderH4")
    private Double buyOrderH4;

    @CsvBindByName(column = "BuySLOrderH4")
    private Double buySLOrderH4;

    @CsvBindByName(column = "SellOrderH4")
    private Double sellOrderH4;

    @CsvBindByName(column = "SellSLOrderH4")
    private Double sellSLOrderH4;

    @CsvBindByName(column = "BuyOrderD1")
    private Double buyOrderD1;

    @CsvBindByName(column = "BuySLOrderD1")
    private Double buySLOrderD1;

    @CsvBindByName(column = "SellOrderD1")
    private Double sellOrderD1;

    @CsvBindByName(column = "SellSLOrderD1")
    private Double sellSLOrderD1;

    @CsvBindByName(column = "HighPrice")
    private Double highPrice;

    @CsvBindByName(column = "LowPrice")
    private Double lowPrice;

    @CsvBindByName(column = "LastPrice")
    private Double lastPrice;

    @CsvBindByName(column = "Condition")
    private String condition;

    public String getDateTime() { return dateTime; }

    public Double getBuyOrderH1() { return buyOrderH1; }

    public Double getBuySLOrderH1() { return buySLOrderH1; }

    public Double getSellOrderH1() { return sellOrderH1; }

    public Double getSellSLOrderH1() { return sellSLOrderH1; }

    public Double getBuyOrderH4() { return buyOrderH4; }

    public Double getBuySLOrderH4() { return buySLOrderH4; }

    public Double getSellOrderH4() { return sellOrderH4; }

    public Double getSellSLOrderH4() { return sellSLOrderH4; }

    public Double getBuyOrderD1() { return buyOrderD1; }

    public Double getBuySLOrderD1() { return buySLOrderD1; }

    public Double getSellOrderD1() { return sellOrderD1; }

    public Double getSellSLOrderD1() { return sellSLOrderD1; }

    public Double getHighPrice() { return highPrice; }

    public Double getLowPrice() { return lowPrice; }

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


    public String getAll() {
        StringBuilder builder = new StringBuilder();
        builder.append("Time:");
        builder.append(getDateTime());
        builder.append(", ");
        builder.append("GlobalTrend:");
        builder.append(getUpGlobalTrend());
        builder.append(", ");
        builder.append("ZigZagLocalTrend:");
        builder.append(getUpZigZagLocalTrend());
        builder.append(", ");
        builder.append("ZigZagMainTrend:");
        builder.append(getUpZigZagMainTrend());
        builder.append(", ");
        builder.append("HRB4Up:");
        builder.append(getHrb4UP());
        builder.append(", ");
        builder.append("HRBUp:");
        builder.append(getHrbUP());
        builder.append(", ");
        builder.append("HRBUpIndex:");
        builder.append(getHrbUpIndex());
        builder.append(", ");
        builder.append("HRBDownIndex:");
        builder.append(getHrbDownIndex());
        builder.append(", ");
        builder.append("HalfTrend:");
        builder.append(getHalfTrendUP());
        builder.append(", ");
        builder.append("BB4UpTrend:");
        builder.append(getBb4UpTrend());
        builder.append(", ");
        builder.append("BBUpTrend:");
        builder.append(getBbUpTrend());
        builder.append(", ");
        builder.append("BBUpIndex:");
        builder.append(getBbUpTrendIndex());
        builder.append(", ");
        builder.append("BBDownIndex:");
        builder.append(getBbDownTrendIndex());
        builder.append(", ");
        builder.append("BrainTrend2Stop:");
        builder.append(getBrainTrend2StopUp());
        builder.append(", ");
        builder.append("Drake15m:");
        builder.append(getDrakeDsm15());
        builder.append(", ");
        builder.append("Drake30m:");
        builder.append(getDrakeDsm30());
        builder.append(", ");
        builder.append("Drake1h:");
        builder.append(getDrakeDsh1());
        builder.append(", ");
        builder.append("Drake4h:");
        builder.append(getDrakeDsh4());
        builder.append(", ");
        builder.append("HighPrice:");
        builder.append(getHighPrice());
        builder.append(", ");
        builder.append("LowPrice:");
        builder.append(getLowPrice());
        builder.append(", ");
        builder.append("LastPrice:");
        builder.append(getLastPrice());
        builder.append(", ");
        builder.append("Condition:");
        builder.append(getCondition());
        return builder.toString();
    }
}
