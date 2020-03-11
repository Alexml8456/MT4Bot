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
    private Integer upGlobalX3Trend;

    @CsvBindByPosition(position = 4)
    private Integer upLocalX3Trend;

    @CsvBindByPosition(position = 5)
    private Integer upZigZagLocalTrend;

    @CsvBindByPosition(position = 6)
    private Integer upZigZagMainTrend;

    @CsvBindByPosition(position = 7)
    private Integer sefc10Up;

    @CsvBindByPosition(position = 8)
    private Integer halfTrendUp;

    @CsvBindByPosition(position = 9)
    private Integer bbUpTrend;

    @CsvBindByPosition(position = 10)
    private Integer bbUpMainTrend;

    @CsvBindByPosition(position = 11)
    private Integer bbUpTrendIndex;

    @CsvBindByPosition(position = 12)
    private Integer bbDownTrendIndex;

    @CsvBindByPosition(position = 13)
    private Integer brainTrend2StopUp;

    @CsvBindByPosition(position = 14)
    private Integer brainTrend2StopMainUp;

    @CsvBindByPosition(position = 15)
    private Integer kx45ChannelUP;

    @CsvBindByPosition(position = 16)
    private Integer kx430ChannelUP;

    @CsvBindByPosition(position = 17)
    private Integer kx4ChannelUP;

    @CsvBindByPosition(position = 18)
    private Integer kx4ChannelBuyIndex;

    @CsvBindByPosition(position = 19)
    private Integer kx4ChannelSellIndex;

    @CsvBindByPosition(position = 20)
    private Integer kx4LineUP;

    @CsvBindByPosition(position = 21)
    private Integer kx4LineBuyIndex;

    @CsvBindByPosition(position = 22)
    private Integer kx4LineSellIndex;

    @CsvBindByPosition(position = 23)
    private Integer kx4FLineUP;

    @CsvBindByPosition(position = 24)
    private Integer kx4FLineBuyIndex;

    @CsvBindByPosition(position = 25)
    private Integer kx4FLineSellIndex;

    @CsvBindByPosition(position = 26)
    private Double lastPrice;

    @CsvBindByPosition(position = 27)
    private Double lastLowPrice;

    @CsvBindByPosition(position = 28)
    private Double lastHighPrice;

    @CsvBindByPosition(position = 29)
    private String condition;

    public String getSymbol() {
        return symbol;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Integer getUpGlobalX3Trend() {
        return upGlobalX3Trend;
    }

    public Integer getUpLocalX3Trend() {
        return upLocalX3Trend;
    }

    public Integer getUpZigZagLocalTrend() {
        return upZigZagLocalTrend;
    }

    public Integer getUpZigZagMainTrend() {
        return upZigZagMainTrend;
    }

    public Integer getSefc10Up() {
        return sefc10Up;
    }

    public Integer getHalfTrendUp() {
        return halfTrendUp;
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

    public String getCondition() {
        return condition;
    }

    public Integer getBrainTrend2StopMainUp() {
        return brainTrend2StopMainUp;
    }

    public Integer getBbUpMainTrend() {
        return bbUpMainTrend;
    }

    public Integer getKx4ChannelUP() {
        return kx4ChannelUP;
    }

    public Integer getKx4ChannelBuyIndex() {
        return kx4ChannelBuyIndex;
    }

    public Integer getKx4ChannelSellIndex() {
        return kx4ChannelSellIndex;
    }

    public Integer getKx4LineSellIndex() {
        return kx4LineSellIndex;
    }

    public Integer getKx4LineBuyIndex() {
        return kx4LineBuyIndex;
    }

    public Integer getKx4LineUP() {
        return kx4LineUP;
    }

    public Integer getKx4FLineUP() {
        return kx4FLineUP;
    }

    public Integer getKx4FLineBuyIndex() {
        return kx4FLineBuyIndex;
    }

    public Integer getKx4FLineSellIndex() {
        return kx4FLineSellIndex;
    }

    public Integer getKx430ChannelUP() {
        return kx430ChannelUP;
    }

    public Integer getKx45ChannelUP() {
        return kx45ChannelUP;
    }
}
