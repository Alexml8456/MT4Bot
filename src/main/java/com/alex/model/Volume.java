package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Volume {

    private BigDecimal buy = new BigDecimal("0");
    private BigDecimal sell = new BigDecimal("0");

    public void update(String direction, BigDecimal volume) {
        if (direction.equals("b")) {
            buy = buy.add(volume);
        } else if (direction.equals("s")) {
            sell = sell.add(volume);
        }
    }

    public Volume(String direction, BigDecimal volume) {
        update(direction, volume);
    }

    @Override
    public String toString() {
        return "Buy " + buy + " Sell " + sell;
    }
}
