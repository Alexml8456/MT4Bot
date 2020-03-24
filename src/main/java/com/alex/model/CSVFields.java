package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CSVFields {
    private String symbol;
    private Integer period;
    private String dateTime;
    private Integer upZigZagLocalTrend;
    private Integer upZigZagMainTrend;
    private Integer sefc10Up;
    private Integer halfTrendUp;
    private Integer bbUpTrend;
    private Integer bbMainUpTrend;
    private Integer bbUpTrendIndex;
    private Integer bbDownTrendIndex;
    private Integer brainTrend2StopUp;
    private Integer brainTrend2StopMainUp;
    private Double lastPrice;
    private Double lastLowPrice;
    private Double lastHighPrice;

}
