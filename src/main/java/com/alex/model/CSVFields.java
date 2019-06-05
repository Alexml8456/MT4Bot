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
    private String time;
    private Double ssValue;
    private Double tfxValue;
    private Double closePrice;

}
