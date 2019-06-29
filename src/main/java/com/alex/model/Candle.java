package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Candle {

    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;

    public void update(BigDecimal value) {
        close = value;
        if (high.compareTo(value)<0){
            high = value;
        }
        if (low.compareTo(value)>0){
            low = value;
        }
    }

    public Candle(BigDecimal value) {
        open = value;
        close = value;
        high = value;
        low = value;
    }

    @Override
    public String toString() {
        return "O " + open + " H " + high + " L " + low + " C " + close;
    }
}
