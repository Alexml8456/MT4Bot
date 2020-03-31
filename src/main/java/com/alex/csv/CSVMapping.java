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
    private Integer sefc10Up;

    @CsvBindByPosition(position = 6)
    private Integer hrbUp;

    @CsvBindByPosition(position = 7)
    private Integer halfTrendUp;

    @CsvBindByPosition(position = 8)
    private Integer bbUpTrend;

    @CsvBindByPosition(position = 9)
    private Integer bbUpMainTrend;

    @CsvBindByPosition(position = 10)
    private Integer bbUpTrendIndex;

    @CsvBindByPosition(position = 11)
    private Integer bbDownTrendIndex;

    @CsvBindByPosition(position = 12)
    private Integer brainTrend2StopUp;

    @CsvBindByPosition(position = 13)
    private Integer brainTrend2StopMainUp;

    @CsvBindByPosition(position = 14)
    private Double FL23;

    @CsvBindByPosition(position = 15)
    private Integer FL23Switch;

    @CsvBindByPosition(position = 16)
    private Double reversalValue;

    @CsvBindByPosition(position = 17)
    private Double GLineValue;

    @CsvBindByPosition(position = 18)
    private Double BLineValue;

    @CsvBindByPosition(position = 19)
    private Double FL23H1;

    @CsvBindByPosition(position = 20)
    private Integer FL23SwitchH1;

    @CsvBindByPosition(position = 21)
    private Double FL23H4;

    @CsvBindByPosition(position = 22)
    private Integer FL23SwitchH4;

    @CsvBindByPosition(position = 23)
    private Double reversalValueH4;

    @CsvBindByPosition(position = 24)
    private Double GLineValueH4;

    @CsvBindByPosition(position = 25)
    private Double BLineValueH4;

    @CsvBindByPosition(position = 26)
    private Double FL23D1;

    @CsvBindByPosition(position = 27)
    private Integer FL23SwitchD1;

    @CsvBindByPosition(position = 28)
    private Double lastPrice;

    @CsvBindByPosition(position = 29)
    private Double lastLowPrice;

    @CsvBindByPosition(position = 30)
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

    public Integer getBrainTrend2StopMainUp() {
        return brainTrend2StopMainUp;
    }

    public Integer getBbUpMainTrend() {
        return bbUpMainTrend;
    }

    public Double getGLineValue() {
        return GLineValue;
    }

    public Double getBLineValue() {
        return BLineValue;
    }

    public Double getReversalValue() {
        return reversalValue;
    }

    public Double getFL23() {
        return FL23;
    }

    public Integer getFL23Switch() {
        return FL23Switch;
    }

    public Integer getHrbUp() {
        return hrbUp;
    }

    public Double getBLineValueH4() {
        return BLineValueH4;
    }

    public Double getGLineValueH4() {
        return GLineValueH4;
    }

    public Double getReversalValueH4() {
        return reversalValueH4;
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
}
